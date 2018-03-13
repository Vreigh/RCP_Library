package library01.dataprovider.book;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import library01.bookapi.BookProvider;
import library01.bookapi.IBook;
import library01.model.Book;
import library01.model.BookEdition;

public class BookProviderXML implements BookProvider {
	private File source;
	
	public BookProviderXML(File source) throws Exception {
		this.source = source;
		
		List<BookEdition> editions = loadEditions();
		List<IBook> books = loadBooks(editions);
		
		// Tutaj dodtakowa walidacja, np sprawdzanie, czy id i eid są unikalne
	}
	
	private BookEdition getBookEditionFromNode(Node node) throws Exception{
		if(node.getNodeType() == Node.ELEMENT_NODE){
			Element eElement = (Element) node;
			
			String id = eElement.getAttribute("id");
			String title = eElement.getAttribute("title");
			String author = eElement.getAttribute("author");
			String genre = eElement.getAttribute("genre");
			Integer publishYear = Integer.valueOf(eElement.getAttribute("publishYear"));
			
			return new BookEdition(id, title, author, genre, publishYear);
		}else {
			throw new Exception("XML file format error!");
		}
	}
	
	private Book getBookFromNode(Node node) throws Exception{
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) node;
			
			String id = eElement.getAttribute("id");
			String eId = eElement.getAttribute("eId");
			Boolean available = Boolean.valueOf(eElement.getAttribute("available"));
			
			return new Book(id, eId, available);
		}else {
			throw new Exception("XML file format error!");
		}
	}
	
	
	private List<BookEdition> loadEditions() throws Exception{
		synchronized(source) {
			List<BookEdition> editions = new ArrayList<BookEdition>();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);

			NodeList nList = doc.getElementsByTagName("edition");

			for(int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				editions.add(getBookEditionFromNode(nNode));
			}
			return editions;
		}
	}
	
	private List<IBook> loadBooks(List<BookEdition> editions) throws Exception{ 
		synchronized(source) {
			List<IBook> books = new ArrayList<IBook>();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);

			NodeList nList = doc.getElementsByTagName("book");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				Book book = getBookFromNode(nNode);
				book.setEdition(editions.stream().filter(s -> s.getId().equals(book.getEId())).findFirst());
				books.add(book);
			}
			return books;
		}
	}
	
	public List<IBook> getBooks() throws Exception{
		List<BookEdition> editions = loadEditions();
		List<IBook> books = loadBooks(editions);
		return books;
	}
	public Optional<IBook> getBookById(String id) throws Exception{
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/bookData/books/book[@id=\"" + id + "\"]");
			
			NodeList matchingBooks = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
			if(matchingBooks.getLength() == 0) {
				return Optional.empty();
			}else if(matchingBooks.getLength() == 1) {
				Node book = matchingBooks.item(0);
				return Optional.of(getBookFromNode(book));
			}else throw new Exception("XML file format error - multiple elements with same id!");
		}
	}
	public Optional<String> createBook(String id, String eId, String title, String author, 
			String genre, Integer publishYear, Boolean available) throws Exception{
		
		if(getBookById(id).isPresent()) {
			return Optional.of("Book with given id already exists!");
		}
		// getBookEditionById + title set, to blad
		
		Book book = new Book(id, eId, title, author, genre, publishYear, available);
		if(book.editionSet()) {
			addNewBookEditionXML(book.getEdition().get());
		}
		
		addNewBookXML(book);
		
		return Optional.empty();
	}
	private void addNewBookEditionXML(BookEdition data) throws Exception {
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			Node editions = ((NodeList)doc.getElementsByTagName("editions")).item(0);
			
			Node editionNode = doc.createElement("edition");
			Element edition = (Element) editionNode;
			edition.setAttribute("id", data.getId());
			edition.setAttribute("title", data.getTitle());
			edition.setAttribute("author", data.getAuthor());
			edition.setAttribute("genre", data.getGenre());
			edition.setAttribute("publishYear", String.valueOf(data.getPublishYear()));
			
			editions.appendChild(editionNode);
			saveDocument(doc);
		}
	}
	private void addNewBookXML(Book data) throws Exception {
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			Node books = ((NodeList)doc.getElementsByTagName("books")).item(0);
			
			Node bookNode = doc.createElement("book");
			Element book = (Element) bookNode;
			book.setAttribute("id", data.getId());
			book.setAttribute("eId", data.getEId());
			book.setAttribute("available", String.valueOf(data.getAvailable()));
			
			System.out.println("Hello World!");
			
			books.appendChild(bookNode);
			saveDocument(doc);
		}
	}
	
	private void saveDocument(Document doc) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource DOMsource = new DOMSource(doc);
		StreamResult result = new StreamResult(source);
		transformer.transform(DOMsource, result);
	}
	
	public void deleteBook(String id) throws Exception {
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			Node books = ((NodeList)doc.getElementsByTagName("books")).item(0);
			
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/bookData/books/book[@id=\"" + id + "\"]");
			
			NodeList matchingBooks = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
			if(matchingBooks.getLength() == 0) {
				//;
			}else if(matchingBooks.getLength() == 1) {
				Node book = matchingBooks.item(0);
				books.removeChild(book);
				saveDocument(doc);
			}else throw new Exception("XML file format error - multiple elements with same id!");
		}
	}
	public Optional<String> updateBook(String id, String nId, String eId, String title, String author, 
			String genre, Integer publishYear, Boolean available) throws Exception{
		Book.validate(nId, eId);
		BookEdition.validate(title, author, genre, publishYear);
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression oldBookSearch = xpath.compile("/bookData/books/book[@id=\"" + id + "\"]");
			
			NodeList matchingBooks = (NodeList)oldBookSearch.evaluate(doc, XPathConstants.NODESET);
			if(matchingBooks.getLength() == 0) {
				return Optional.of("Requested Book not found, cant update");
			}else if(matchingBooks.getLength() == 1) {
				Node bookNode = matchingBooks.item(0);
				if((nId != null) && (!nId.equals(id))) {
					XPathExpression newBookSearch = xpath.compile("/bookData/books/book[@id=\"" + nId + "\"]");
					matchingBooks = (NodeList)newBookSearch.evaluate(doc, XPathConstants.NODESET);
					if(matchingBooks.getLength() == 1) {
						return Optional.of("This new id is already taken");
					}else if(matchingBooks.getLength() > 1) {
						throw new Exception("XML file format error - multiple elements with same id!");
					}
				}
				Element book = (Element) bookNode;
				Node editionToUpdate = null;
				
				if(eId != null) {
					XPathExpression editionSearch = xpath.compile("/bookData/editions/edition[@id=\"" + eId + "\"]");
					NodeList matchingEditions = (NodeList)editionSearch.evaluate(doc, XPathConstants.NODESET);
					if(matchingEditions.getLength() == 0) {
						//
					}else if(matchingEditions.getLength() == 1) {
						editionToUpdate = matchingEditions.item(0);
					}else throw new Exception("XML file format error - multiple elements with same id!"); 
				}else {
					XPathExpression editionSearch = xpath.compile("/bookData/editions/edition[@id=\"" + book.getAttribute("eId") + "\"]");
					NodeList matchingEditions = (NodeList)editionSearch.evaluate(doc, XPathConstants.NODESET);
					if(matchingEditions.getLength() == 0) {
						// czyli nie updatujemy zadnych
					}else if(matchingEditions.getLength() == 1) {
						editionToUpdate = matchingEditions.item(0);
					}else throw new Exception("XML file format error - multiple elements with same id!");
				}
				
				if(editionToUpdate != null) {
					Element edition = (Element) editionToUpdate;
					if(title != null) edition.setAttribute("title", title);
					if(author != null) edition.setAttribute("title", author);
					if(genre != null) edition.setAttribute("title", genre);
					if(publishYear != null) edition.setAttribute("publishYear", String.valueOf(publishYear));
				}
				
				if(nId != null) book.setAttribute("id", nId);
				if(eId != null) book.setAttribute("eId", eId);
				if(available != null) book.setAttribute("available", String.valueOf(available));
				
			}else throw new Exception("XML file format error - multiple elements with same id!");
			
			saveDocument(doc);
			return Optional.empty();
		}
	}
}

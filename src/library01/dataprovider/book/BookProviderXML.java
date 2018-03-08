package library01.dataprovider.book;

import java.io.File;
import java.io.FileNotFoundException;
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

import library01.model.Book;
import library01.model.BookEdition;
import library01.model.BookUpdateData;

public class BookProviderXML implements BookProvider {
	private File source;
	
	public BookProviderXML(File source) throws Exception {
		this.source = source;
		
		List<BookEdition> editions = loadEditions();
		List<Book> books = loadBooks();
		
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
			
			BookUpdateData data = new BookUpdateData(null, id, title, author, genre, publishYear, null);
			Optional<String> error = data.validateEdition();
			if(error.isPresent()) {
				throw new Exception("XML file format error!");
			}
			return new BookEdition(data);
			
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
			
			BookUpdateData data = new BookUpdateData(id, eId, null, null, null, null, available);
			Optional<String> error = data.validateCreate();
			if(error.isPresent()) {
				throw new Exception("XML file format error!");
			}
			return new Book(data);
			
		}else {
			throw new Exception("XML file format error!");
		}
	}
	
	
	private List<BookEdition> loadEditions() throws Exception{
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
	
	private List<Book> loadBooks() throws Exception{
		List<Book> books = new ArrayList<Book>();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(source);

		NodeList nList = doc.getElementsByTagName("book");

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			books.add(getBookFromNode(nNode));
		}
		return books;
	}
	
	public List<Book> getBooks() throws Exception{
		List<BookEdition> editions = loadEditions();
		List<Book> books = loadBooks();
		for(Book book : books) {
			Optional<BookEdition> edition = editions.stream().filter(s -> s.getId().equals(book.getEId())).findFirst();
			book.setEdition(edition);
		}
		return books;
	}
	public Optional<Book> getBookById(String id) throws Exception{
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
	public Optional<String> addNewBook(BookUpdateData data) throws Exception{
		if(getBookById(data.id).isPresent()) {
			return Optional.of("Book with given id already exists!");
		}
		
		Optional<String> error = data.validateCreateWithEdition();
		if(error.isPresent()) return error;
		
		if(data.title != null) {
			addNewBookEditionXML(data);
		}
		
		addNewBookXML(data);
		
		return Optional.empty();
	}
	private void addNewBookEditionXML(BookUpdateData data) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(source);
		
		Node editions = ((NodeList)doc.getElementsByTagName("editions")).item(0);
		
		Node editionNode = doc.createElement("edition");
		Element edition = (Element) editionNode;
		edition.setAttribute("id", data.eId);
		edition.setAttribute("title", data.title);
		edition.setAttribute("author", data.author);
		edition.setAttribute("genre", data.genre);
		edition.setAttribute("publishYear", String.valueOf(data.publishYear));
		
		editions.appendChild(editionNode);
		saveDocument(doc);
	}
	private void addNewBookXML(BookUpdateData data) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(source);
		
		Node books = ((NodeList)doc.getElementsByTagName("books")).item(0);
		
		Node bookNode = doc.createElement("book");
		Element book = (Element) bookNode;
		book.setAttribute("id", data.eId);
		book.setAttribute("eId", data.eId);
		book.setAttribute("available", String.valueOf(data.available));
		
		System.out.println("Hello World!");
		
		books.appendChild(bookNode);
		saveDocument(doc);	
	}
	
	private void saveDocument(Document doc) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource DOMsource = new DOMSource(doc);
		StreamResult result = new StreamResult(source);
		transformer.transform(DOMsource, result);
	}
	
	public void deleteBook(String id) throws Exception {
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
	public Optional<String> updateBook(String id, BookUpdateData update) throws Exception{
		Optional<String> error = update.validatePresent();
		if(error.isPresent()) return error;
		
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
			if((update.id != null) && (!update.id.equals(id))) {
				XPathExpression newBookSearch = xpath.compile("/bookData/books/book[@id=\"" + update.id + "\"]");
				matchingBooks = (NodeList)newBookSearch.evaluate(doc, XPathConstants.NODESET);
				if(matchingBooks.getLength() == 1) {
					return Optional.of("This new id is already taken");
				}else if(matchingBooks.getLength() > 1) {
					throw new Exception("XML file format error - multiple elements with same id!");
				}
			}
			Element book = (Element) bookNode;
			Node editionToUpdate = null;
			
			if(update.eId != null) {
				XPathExpression editionSearch = xpath.compile("/bookData/editions/edition[@id=\"" + update.eId + "\"]");
				NodeList matchingEditions = (NodeList)editionSearch.evaluate(doc, XPathConstants.NODESET);
				if(matchingEditions.getLength() == 0) {
					// nie updatujemy zadnego
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
				if(update.title != null) edition.setAttribute("title", update.title);
				if(update.author != null) edition.setAttribute("title", update.author);
				if(update.genre != null) edition.setAttribute("title", update.genre);
				if(update.publishYear != null) edition.setAttribute("publishYear", String.valueOf(update.publishYear));
			}
			
			if(update.id != null) book.setAttribute("id", update.id);
			if(update.eId != null) book.setAttribute("eId", update.eId);
			if(update.available != null) book.setAttribute("available", String.valueOf(update.available));
			
		}else throw new Exception("XML file format error - multiple elements with same id!");
		
		saveDocument(doc);
		return Optional.empty();
	}
}

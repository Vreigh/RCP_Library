package library01.dataprovider.book;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import library01.bookapi.BookProvider;
import library01.bookapi.IBook;
import library01.model.Book;
import library01.model.BookEdition;
import library01.tasks.CheckerXMLTask;

public class BookProviderXML implements BookProvider {
	private File source;
	
	public BookProviderXML() throws Exception{
		source = new File("eclipse-workspace/Library01/src/library01/data/data.xml");
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(source);
		doc.getDocumentElement().normalize();
		
		List<BookEdition> editions = loadEditions(doc);
		List<IBook> books = loadBooks(editions, doc);
		
		saveDocument(doc);
		
		CheckerXMLTask checker = new CheckerXMLTask(source);
		checker.start();
	}
	
	private BookEdition getBookEditionFromNode(Node node) throws ConfigurationException{
		if(node.getNodeType() == Node.ELEMENT_NODE){
			Element eElement = (Element) node;
			
			String id = eElement.getAttribute("id");
			String title = eElement.getAttribute("title");
			String author = eElement.getAttribute("author");
			String genre = eElement.getAttribute("genre");
			Integer publishYear = Integer.valueOf(eElement.getAttribute("publishYear"));
			String description = eElement.getAttribute("description");
			
			return new BookEdition(id, title, author, genre, publishYear, description);
		}else {
			throw new ConfigurationException("XML file format error!");
		}
	}
	
	private Book getBookFromNode(Node node) throws ConfigurationException{
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) node;
			
			String id = eElement.getAttribute("id");
			String eId = eElement.getAttribute("eId");
			Integer condition = Integer.valueOf(eElement.getAttribute("condition"));
			Boolean available = Boolean.valueOf(eElement.getAttribute("available"));
			
			return new Book(id, eId, condition, available);
		}else {
			throw new ConfigurationException("XML file format error!");
		}
	}
	
	
	private List<BookEdition> loadEditions(Document doc) throws ConfigurationException{
		List<BookEdition> editions = new ArrayList<BookEdition>();
		
		NodeList nList = doc.getElementsByTagName("edition");

		for(int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
			editions.add(getBookEditionFromNode(nNode));
		}
		return editions;
	}
	
	private List<IBook> loadBooks(List<BookEdition> editions, Document doc) throws ConfigurationException{ 
		List<IBook> books = new ArrayList<IBook>();
		
		NodeList nList = doc.getElementsByTagName("book");

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			Book book = getBookFromNode(nNode);
			book.setEdition(editions.stream().filter(s -> s.getId().equals(book.getEId())).findFirst());
			books.add(book);
		}
		return books;
	}
	
	public List<IBook> getBooks() throws ParserConfigurationException, ConfigurationException, SAXException, IOException{
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			List<BookEdition> editions = loadEditions(doc);
			List<IBook> books = loadBooks(editions, doc);
			return books;
		}
	}
	
	private Optional<Node> getBookEditionByIdXML(Document doc, String id) throws XPathExpressionException, ConfigurationException{
		if(id == null) return Optional.empty();
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("/bookData/editions/edition[@id=\"" + id + "\"]");
		
		NodeList matching = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		if(matching.getLength() == 0) {
			return Optional.empty();
		}else if(matching.getLength() == 1) {
			Node edition = matching.item(0);
			return Optional.of(edition);
		}else throw new ConfigurationException("XML file format error - multiple elements with same id!");
	}
	
	private Optional<Node> getBookByIdXML(Document doc, String id) throws ConfigurationException, XPathExpressionException{
		if(id == null) return Optional.empty();
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("/bookData/books/book[@id=\"" + id + "\"]");
		
		NodeList matchingBooks = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		if(matchingBooks.getLength() == 0) {
			return Optional.empty();
		}else if(matchingBooks.getLength() == 1) {
			Node book = matchingBooks.item(0);
			return Optional.of(book);
		}else throw new ConfigurationException("XML file format error - multiple elements with same id!");
	}
	
	public Optional<IBook> getBookById(String id) throws ParserConfigurationException, ConfigurationException, XPathExpressionException, SAXException, IOException{
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			Optional<Node> bookNode = getBookByIdXML(doc, id);
			if(bookNode.isPresent()) {
				Book book = getBookFromNode(bookNode.get());
				Optional<Node> editionNode = getBookEditionByIdXML(doc, book.getEId());
				if(editionNode.isPresent()) {
					book.setEdition(Optional.of(getBookEditionFromNode(editionNode.get())));
				}else {
					book.setEdition(Optional.empty());
				}
				return Optional.of(book);
			}else return Optional.empty();
		}
	}
	public Optional<String> createBook(String id, String eId, Integer condition, String title, String author, 
			String genre, Integer publishYear, String description, Boolean available) throws Exception{
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			if(getBookByIdXML(doc, id).isPresent()) {
				return Optional.of("Book with given id already exists!");
			}
			if((getBookEditionByIdXML(doc, eId).isPresent()) && (title != null)) {
				return Optional.of("Book edition with given EID already exists - please fill only ID and EID fields");
			}
			
			Book book = new Book(id, eId, condition, title, author, genre, publishYear, description, available);
			if(book.editionSet()) {
				addNewBookEditionXML(doc, book.getEdition().get());
			}
			
			addNewBookXML(doc, book);
			
			return Optional.empty();
		}
	}
	private void addNewBookEditionXML(Document doc, BookEdition data) throws TransformerException {
		Node editions = ((NodeList)doc.getElementsByTagName("editions")).item(0);
		
		Node editionNode = doc.createElement("edition");
		Element edition = (Element) editionNode;
		edition.setAttribute("id", data.getId());
		edition.setAttribute("title", data.getTitle());
		edition.setAttribute("author", data.getAuthor());
		edition.setAttribute("genre", data.getGenre());
		edition.setAttribute("publishYear", String.valueOf(data.getPublishYear()));
		edition.setAttribute("description", data.getDescription());
		
		editions.appendChild(editionNode);
		saveDocument(doc);
	}
	private void addNewBookXML(Document doc, Book data) throws Exception {
		Node books = ((NodeList)doc.getElementsByTagName("books")).item(0);
		
		Node bookNode = doc.createElement("book");
		Element book = (Element) bookNode;
		book.setAttribute("id", data.getId());
		book.setAttribute("eId", data.getEId());
		book.setAttribute("condition", String.valueOf(data.getCondition()));
		book.setAttribute("available", String.valueOf(data.getAvailable()));
		
		books.appendChild(bookNode);
		saveDocument(doc);
	}
	
	private void saveDocument(Document doc) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource DOMsource = new DOMSource(doc);
		StreamResult result = new StreamResult(source);
		transformer.transform(DOMsource, result);
	}
	
	public void deleteBook(String id) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, ConfigurationException, TransformerException {
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
			}else throw new ConfigurationException("XML file format error - multiple elements with same id!");
		}
	}
	public Optional<String> updateBook(String id, String nId, String eId, Integer condition, String title, String author, 
			String genre, Integer publishYear, String description, Boolean available) throws ParserConfigurationException, ConfigurationException, XPathExpressionException, SAXException, IOException, TransformerException{
		Book.validate(nId, eId, condition);
		BookEdition.validate(title, author, genre, publishYear, description);
		synchronized(source) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			
			Optional<Node> bookNode = getBookByIdXML(doc, id);
			if(!bookNode.isPresent()) {
				return Optional.of("Requested Book not found, cant update");
			}else {
				Element book = (Element)bookNode.get();
				if((nId != null) && (!nId.equals(id))) {
					if(getBookByIdXML(doc, nId).isPresent()) {
						return Optional.of("This new id is already taken");
					}
				}
				Optional<Node> editionToUpdate = Optional.empty();
				
				if(eId != null) {
					editionToUpdate = getBookEditionByIdXML(doc, eId);
				}else {
					editionToUpdate = getBookEditionByIdXML(doc, book.getAttribute("eId"));
				}
				
				if(editionToUpdate.isPresent()) {
					Element edition = (Element) editionToUpdate.get();
					if(title != null) edition.setAttribute("title", title);
					if(author != null) edition.setAttribute("author", author);
					if(genre != null) edition.setAttribute("genre", genre);
					if(publishYear != null) edition.setAttribute("publishYear", String.valueOf(publishYear));
					if(description != null) edition.setAttribute("description", description);
				}
				
				if(nId != null) book.setAttribute("id", nId);
				if(eId != null) book.setAttribute("eId", eId);
				if(condition != null) book.setAttribute("condition", String.valueOf(condition));
				if(available != null) book.setAttribute("available", String.valueOf(available));
				
				saveDocument(doc);
				return Optional.empty();
			}
		}
	}
}

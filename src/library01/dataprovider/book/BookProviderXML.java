package library01.dataprovider.book;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import library01.model.Book;
import library01.model.BookEdition;
import library01.model.BookUpdateData;

public class BookProviderXML implements BookProvider {
	private File source;
	
	public BookProviderXML(File source) {
		this.source = source;
	}
	
	private List<BookEdition> loadEditions(){
		try {
			List<BookEdition> editions = new ArrayList<BookEdition>();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("edition");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
	
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					String id = eElement.getAttribute("id");
					String title = eElement.getElementsByTagName("title").item(0).getTextContent();
					String author = eElement.getElementsByTagName("author").item(0).getTextContent();
					String genre = eElement.getElementsByTagName("genre").item(0).getTextContent();
					Integer publishYear = Integer.valueOf(eElement.getElementsByTagName("publishYear").item(0).getTextContent());
					
					BookUpdateData data = new BookUpdateData(null, id, title, author, genre, publishYear, null);
					Optional<String> error = data.validateEdition();
					if(error.isPresent()) {
						throw new Exception("XML file format error!");
					}
					editions.add(new BookEdition(data));
					
				}else {
					throw new Exception("XML file format error!");
				}
			}
			return editions;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<Book> loadBooks(){
		try {
			List<Book> books = new ArrayList<Book>();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("book");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
	
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					String id = eElement.getAttribute("id");
					String eId = eElement.getElementsByTagName("eId").item(0).getTextContent();
					Boolean available = Boolean.valueOf(eElement.getElementsByTagName("available").item(0).getTextContent());
					
					BookUpdateData data = new BookUpdateData(id, eId, null, null, null, null, available);
					Optional<String> error = data.validateCreate();
					if(error.isPresent()) {
						throw new Exception("XML file format error!");
					}
					books.add(new Book(data));
					
				}else {
					throw new Exception("XML file format error!");
				}
			}
			return books;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Book> getBooks(){
		List<BookEdition> editions = loadEditions();
		List<Book> books = loadBooks();
		for(Book book : books) {
			Optional<BookEdition> edition = editions.stream().filter(s -> s.getId().equals(book.getEId())).findFirst();
			book.setEdition(edition);
		}
		return books;
	}
	public Optional<Book> getBookById(String id){
		return null;
	}
	public Optional<String> addNewBook(BookUpdateData data){
		return null;
	}
	public void deleteBook(String id) {
		//
	}
	public Optional<String> updateBook(String id, BookUpdateData update){
		return null;
	}
}

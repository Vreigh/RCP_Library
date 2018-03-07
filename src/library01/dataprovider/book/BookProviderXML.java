package library01.dataprovider.book;

import java.io.File;
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
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(source);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("edition");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
	
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("Book id : " + eElement.getAttribute("id"));
					System.out.println("EID : " + eElement.getElementsByTagName("eId").item(0).getTextContent());
					System.out.println("Available : " + eElement.getElementsByTagName("available").item(0).getTextContent());
					System.out.println();
				}
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Book> getBooks(){
		/*List<BookEdition> editions = loadEditions();
		List<Book> books = loadBooks();
		for(Book book : books) {
			Optional<BookEdition> edition = editions.stream().filter(s -> s.getId().equals(book.getEId())).findFirst();
			book.setEdition(edition);
		}*/
		return null;
	}
	public Optional<Book> getBookById(String id){
		return null;
	}
	public Optional<String> addNewBook(BookUpdateData data){
		return null;
	}
	public void deleteBook(String id) {
		
	}
	public Optional<String> updateBook(String id, BookUpdateData update){
		return null;
	}
}

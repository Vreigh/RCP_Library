package library01.dataprovider;

import java.io.File;
import java.util.List;
import java.util.Optional;

import library01.dataprovider.book.BookProvider;
import library01.dataprovider.book.BookProviderMock;
import library01.dataprovider.book.BookProviderXML;
import library01.model.Book;
import library01.model.BookUpdateData;

public enum DataProvider {
	INSTANCE;
	
	private BookProvider bookProvider;
	
	private DataProvider() {
		try {
			// tutaj ladowanie configa i wybor odpowiedniego BookProvider-a
			bookProvider = new BookProviderXML(new File("eclipse-workspace/Library01/src/library01/data/data.xml"));
			//bookProvider = new BookProviderMock();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public List<Book> getBooks(){
		try {
			return bookProvider.getBooks();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	public Optional<Book> getBookById(String id){
		try {
			return bookProvider.getBookById(id);
		}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	public Optional<String> addNewBook(BookUpdateData data) {
		try {
			return bookProvider.addNewBook(data);
		}catch(Exception e) {
			return Optional.of(e.getMessage());
		}
	}
	public void deleteBook(String id) {
		try {
			bookProvider.deleteBook(id);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public Optional<String> updateBook(String id, BookUpdateData update) {
		try {
			return bookProvider.updateBook(id, update);
		}catch(Exception e) {
			return Optional.of(e.getMessage());
		}
	}
}

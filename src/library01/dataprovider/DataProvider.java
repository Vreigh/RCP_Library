package library01.dataprovider;

import java.util.List;
import java.util.Optional;
import library01.bookapi.BookProvider;
import library01.bookapi.IBook;
import library01.setup.Setupper;


public class DataProvider {
	public static DataProvider INSTANCE;
	
	private BookProvider bookProvider;
	
	public static void newInstance(Setupper setup) {
		INSTANCE = new DataProvider(setup);
	}
	
	private DataProvider(Setupper setup) {
		bookProvider = setup.getBookProvider();
	}
	
	public List<IBook> getBooks(){
		try {
			return bookProvider.getBooks();
		}catch(Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	public Optional<IBook> getBookById(String id){
		try {
			return bookProvider.getBookById(id);
		}catch(Throwable e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	public Optional<String> createBook(String id, String eId, String title, String author, 
			String genre, Integer publishYear, Boolean Available) {
		try {
			return bookProvider.createBook(id, eId, title, author, genre,  publishYear, Available);
		}catch(Throwable e) {
			return Optional.of(e.getMessage());
		}
	}
	public void deleteBook(String id) {
		try {
			bookProvider.deleteBook(id);
		}catch(Throwable e) {
			e.printStackTrace();
		}
		
	}
	public Optional<String> updateBook(String id,String nId, String eId, String title, String author, 
			String genre, Integer publishYear, Boolean Available) {
		try {
			return bookProvider.updateBook(id,nId, eId, title, author, genre, publishYear, Available);
		}catch(Throwable e) {
			return Optional.of(e.getMessage());
		}
	}
}

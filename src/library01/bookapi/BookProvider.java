package library01.bookapi;

import java.util.List;
import java.util.Optional;


public interface BookProvider {
	public List<IBook> getBooks() throws Throwable;
	
	public Optional<IBook> getBookById(String id) throws Throwable;
	
	public Optional<String> createBook(String id, String eId, Integer condition, String title, String author, 
			String genre, Integer publishYear, String description, Boolean Available) throws Throwable;
	
	public Optional<String> updateBook(String id, String nId, String eId, Integer condition, String title, String author, 
			String genre, Integer publishYear, String description, Boolean Available) throws Throwable;
	
	public void deleteBook(String id) throws Throwable;
	
}

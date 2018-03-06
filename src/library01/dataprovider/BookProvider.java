package library01.dataprovider;

import java.util.List;
import library01.model.Book;

public interface BookProvider {
	public List<Book> getBooks();
	public Book getBookById();
	public void addNewBook();
	public void deleteBook();
	public void updateBook();
	
}

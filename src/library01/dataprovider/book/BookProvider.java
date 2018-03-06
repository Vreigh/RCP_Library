package library01.dataprovider.book;

import java.util.List;
import java.util.Optional;

import library01.model.Book;

public interface BookProvider {
	public List<Book> getBooks();
	public Optional<Book> getBookById(String id);
	// TO DO: jakie argumenty to przyjmuje?
	public void addNewBook();
	public void deleteBook();
	public void updateBook();
}

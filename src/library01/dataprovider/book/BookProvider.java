package library01.dataprovider.book;

import java.util.List;
import java.util.Optional;

import library01.model.Book;
import library01.model.BookUpdateData;

public interface BookProvider {
	public List<Book> getBooks();
	public Optional<Book> getBookById(String id);
	// TO DO: jakie argumenty to przyjmuje?
	public Optional<String> addNewBook(BookUpdateData data);
	public void deleteBook(String id);
	public Boolean updateBook(String id, BookUpdateData update);
}

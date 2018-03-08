package library01.dataprovider.book;

import java.util.List;
import java.util.Optional;

import library01.model.Book;
import library01.model.BookUpdateData;

public interface BookProvider {
	public List<Book> getBooks() throws Exception;
	public Optional<Book> getBookById(String id) throws Exception;
	public Optional<String> addNewBook(BookUpdateData data) throws Exception;
	public void deleteBook(String id) throws Exception;
	public Optional<String> updateBook(String id, BookUpdateData update) throws Exception;
}

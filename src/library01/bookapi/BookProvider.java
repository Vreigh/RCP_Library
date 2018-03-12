package library01.bookapi;

import java.util.List;
import java.util.Optional;

import library01.model.Book;
import library01.model.BookUpdateData;

public interface BookProvider {
	public List<IBook> getBooks() throws Throwable;
	public Optional<IBook> getBookById(String id) throws Throwable;
	public Optional<String> addNewBook(BookUpdateData data) throws Throwable;
	public Optional<String> updateBook(String id, BookUpdateData update) throws Throwable;
	public void deleteBook(String id) throws Throwable;
	
}

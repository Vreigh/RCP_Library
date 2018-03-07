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
		// tutaj ladowanie configa i wybor odpowiedniego BookProvider-a
		bookProvider = new BookProviderXML(new File("eclipse-workspace/Library01/src/library01/data/NewFile.xml"));
	}
	
	public List<Book> getBooks(){
		return bookProvider.getBooks();
	}
	public Optional<Book> getBookById(String id){
		return bookProvider.getBookById(id);
	}
	public Optional<String> addNewBook(BookUpdateData data) {
		return bookProvider.addNewBook(data);
	}
	public void deleteBook(String id) {
		bookProvider.deleteBook(id);
	}
	public Optional<String> updateBook(String id, BookUpdateData update) {
		return bookProvider.updateBook(id, update);
	}
}

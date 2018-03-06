package library01.dataprovider;

import java.util.List;
import java.util.Optional;

import library01.dataprovider.book.BookProvider;
import library01.dataprovider.book.BookProviderMock;
import library01.model.Book;

public enum DataProvider {
	INSTANCE;
	
	private BookProvider bookProvider;
	
	private DataProvider() {
		// wybor odpowiedniego BookProvider-a
		bookProvider = new BookProviderMock();
	}
	
	public List<Book> getBooks(){
		return bookProvider.getBooks();
	}
	public Optional<Book> getBookById(String id){
		return bookProvider.getBookById(id);
	}
	public void addNewBook() {
		bookProvider.addNewBook();
	}
	public void deleteBook() {
		bookProvider.deleteBook();
	}
	public void updateBook() {
		bookProvider.updateBook();
	}
}

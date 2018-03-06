package library01.dataprovider.book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import library01.model.Book;

public class BookProviderMock implements BookProvider {
	private List<Book> books;
	
	public BookProviderMock() {
		books = new ArrayList<Book>();
		books.add(new Book("7354-XYZ", "Nation", "Terry Pratchett", "science-fiction", 2008, true));
		books.add(new Book("8254-XYZ", "Nation", "Terry Pratchett", "science-fiction", 2008, false));
		books.add(new Book("1324-ZYZ", "Green Mile", "Stephen King", "dark fantasy", 1996, true));
		books.add(new Book("1325-ZYZ", "Green Mile", "Stephen King", "dark fantasy", 1996, true));
		books.add(new Book("8124-XYI", "Enneagram", "Helen Palmer", "psychology", 1988, true));
		books.add(new Book("7124-XYZ", "Zbrodnia i Kara", "Fiodor Dostojewski", "proza psychologiczna", 1866, true));
		books.add(new Book("8564-XUZ", "Metro 2033", "Dmitrij Głuchowski", "fantasy", 2005, false));
		books.add(new Book("7124-XAZ", "Analiza 2", "Zbigniew Skoczylas", "matematyka", 2011, true));
		books.add(new Book("7524-XAZ", "Analiza 2", "Zbigniew Skoczylas", "matematyka", 2011, false));
		books.add(new Book("9024-XAZ", "Analiza 2", "Zbigniew Skoczylas", "matematyka", 2011, true));
		books.add(new Book("8211-AYZ", "The Colour of Magic", "Terry Pratchett", "fantasy", 1983, false));
		books.add(new Book("1254-XYZ", "Pet Sematary", "Stephen King", "horror", 1983, true));
		books.add(new Book("3431-XYZ", "Pet Sematary", "Stephen King", "horror", 1983, false));
	}
	
	public List<Book> getBooks(){
		return Collections.unmodifiableList(books);
	}
	public Optional<Book> getBookById(String id) {
		return books.stream().filter(s -> s.getId().equals(id)).findFirst();
	}
	// TO DO
	public void addNewBook() {
		
	}
	public void deleteBook() {
		
	}
	public void updateBook() {
		
	}
}

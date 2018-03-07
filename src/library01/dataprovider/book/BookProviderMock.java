package library01.dataprovider.book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import library01.model.Book;
import library01.model.BookEdition;
import library01.model.BookUpdateData;

public class BookProviderMock implements BookProvider {
	private List<Book> books;
	private List<BookEdition> editions;
	
	public BookProviderMock() {
		editions = new ArrayList<BookEdition>();
		editions.add(new BookEdition("7354-XYZ", "Nation", "Terry Pratchett", "science-fiction", 2008));
		editions.add(new BookEdition("1324-ZYZ", "Green Mile", "Stephen King", "dark fantasy", 1996));
		editions.add(new BookEdition("8124-XYI", "Enneagram", "Helen Palmer", "psychology", 1988));
		editions.add(new BookEdition("7124-XAZ", "Analiza 2", "Zbigniew Skoczylas", "matematyka", 2011));
		editions.add(new BookEdition("8211-AYZ", "The Colour of Magic", "Terry Pratchett", "fantasy", 1983));
		editions.add(new BookEdition("1254-XYZ", "Pet Sematary", "Stephen King", "horror", 1983));
		editions.add(new BookEdition("7124-XYZ", "Zbrodnia i Kara", "Fiodor Dostojewski", "proza psychologiczna", 1866));
		
		books = new ArrayList<Book>();
		books.add(new Book("7239357", "7354-XYZ", true));
		books.add(new Book("3242423", "7354-XYZ", false));
		books.add(new Book("723234357", "7354-XYZ", false));
		books.add(new Book("73453457", "1324-ZYZ", true));
		books.add(new Book("72123357", "1324-ZYZ", false));
		books.add(new Book("72234357", "8124-XYI", true));
		books.add(new Book("239357", "8124-XYI", false));
		books.add(new Book("12313357", "7124-XAZ", true));
		books.add(new Book("1123239357", "7124-XAZ", true));
		books.add(new Book("1239357", "8211-AYZ", false));
		books.add(new Book("23549357", "8211-AYZ", true));
		books.add(new Book("123219357", "1254-XYZ", true));
		books.add(new Book("2429357", "1254-XYZ", false));
		books.add(new Book("123357", "7124-XYZ", true));
		books.add(new Book("123357", "1111-XYZ", true));
		
		for(Book book : books) {
			Optional<BookEdition> edition = getBookEditionById(book.getEId());
			book.setEdition(edition);
		}
	}
	
	public List<Book> getBooks(){
		List<Book> booksClone = new ArrayList<Book>(books);
		List<BookEdition> editionsClone = new ArrayList<BookEdition>(editions);
		for(Book book : booksClone) {
			Optional<BookEdition> edition = getBookEditionById(editionsClone, book.getEId());
			book.setEdition(edition);
		}
		return booksClone;
	}
	
	public Optional<Book> getBookById(String id) {
		return books.stream().filter(s -> s.getId().equals(id)).findFirst();
	}
	
	private Optional<BookEdition> getBookEditionById(String id){
		return editions.stream().filter(s -> s.getId().equals(id)).findFirst();
	}
	private Optional<BookEdition> getBookEditionById(List<BookEdition> editions, String id){
		return editions.stream().filter(s -> s.getId().equals(id)).findFirst();
	} 
	// TO DO
	
	public void addNewBook() {
		
	}
	public void deleteBook() {
		
	}
	
	public Boolean updateBook(String id, BookUpdateData update) {
		Optional<Book> old = getBookById(id);
		if(!old.isPresent()) return false; // jeśli nie ma książki, którą chcemy edytować
		if((update.id != null) || (update.id != id)) {
			if(getBookById(update.id).isPresent()) return false; // nowe id zajęte
		}
		if(update.eId != null) {
			Optional<BookEdition> edition = getBookEditionById(update.eId);
			old.get().setEdition(edition);
		}
		// ok
		return old.get().update(update);
	}
}
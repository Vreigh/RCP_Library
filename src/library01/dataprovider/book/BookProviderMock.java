package library01.dataprovider.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import library01.bookapi.BookProvider;
import library01.bookapi.IBook;
import library01.model.Book;
import library01.model.BookEdition;

public class BookProviderMock implements BookProvider {
	private List<Book> books;
	private List<BookEdition> editions;
	
	public BookProviderMock() {
		editions = new ArrayList<BookEdition>();
		editions.add(new BookEdition("7354-XYZ", "Nation", "Terry Pratchett", "science-fiction", 2008, "example"));
		editions.add(new BookEdition("1324-ZYZ", "Green Mile", "Stephen King", "dark fantasy", 1996, "example"));
		editions.add(new BookEdition("8124-XYI", "Enneagram", "Helen Palmer", "psychology", 1988, "example"));
		editions.add(new BookEdition("7124-XAZ", "Analiza 2", "Zbigniew Skoczylas", "matematyka", 2011, "example"));
		editions.add(new BookEdition("8211-AYZ", "The Colour of Magic", "Terry Pratchett", "fantasy", 1983, "example"));
		editions.add(new BookEdition("1254-XYZ", "Pet Sematary", "Stephen King", "horror", 1983, "example"));
		editions.add(new BookEdition("7124-XYZ", "Zbrodnia i Kara", "Fiodor Dostojewski", "proza psychologiczna", 1866, "example"));
		
		books = new ArrayList<Book>();
		books.add(new Book("7239357", "7354-XYZ", 2, true));
		books.add(new Book("3242423", "7354-XYZ", 3, false));
		books.add(new Book("723234357", "7354-XYZ", 1, false));
		books.add(new Book("73453457", "1324-ZYZ", 4, true));
		books.add(new Book("72123357", "1324-ZYZ", 0, false));
		books.add(new Book("72234357", "8124-XYI", 3, true));
		books.add(new Book("239357", "8124-XYI", 5, false));
		books.add(new Book("12313357", "7124-XAZ", 2, true));
		books.add(new Book("1123239357", "7124-XAZ", 3, true));
		books.add(new Book("1239357", "8211-AYZ", 2, false));
		books.add(new Book("23549357", "8211-AYZ", 1, true));
		books.add(new Book("123219357", "1254-XYZ", 2, true));
		books.add(new Book("2429357", "1254-XYZ", 4, false));
		books.add(new Book("123357", "7124-XYZ", 1, true));
		books.add(new Book("8867357", "1111-XYZ", 0, true));
		
		/*for(Book book : books) {
			Optional<BookEdition> edition = getBookEditionById(book.getEId());
			book.setEdition(edition);
		}*/
	}
	
	public List<IBook> getBooks(){
		for(Book book : books) {
			Optional<BookEdition> edition = getBookEditionById(book.getEId());
			book.setEdition(edition);
		}
		return new ArrayList<IBook>(books);
	}
	
	public Optional<IBook> getBookById(String id) {
		if(id == null) return Optional.empty();
		Optional<Book> book = books.stream().filter(s -> s.getId().equals(id)).findFirst();
		if(book.isPresent()) {
			Optional<BookEdition> edition = getBookEditionById(book.get().getEId());
			book.get().setEdition(edition);
			return Optional.of(book.get());
		}else return Optional.empty();
	}
	
	private Optional<BookEdition> getBookEditionById(String id){
		if(id == null) return Optional.empty();
		return editions.stream().filter(s -> s.getId().equals(id)).findFirst();
	}
	
	public Optional<String> createBook(String id, String eId, Integer condition, String title, String author, 
			String genre, Integer publishYear, String description, Boolean available) throws IllegalArgumentException  {
		if(getBookById(id).isPresent()) {
			return Optional.of("Book with given id already exists!");
		}
		if(getBookEditionById(eId).isPresent() && title != null) {
			return Optional.of("Book edition with given EID already exists - please fill only ID and EID fields");
		}
		Book book = new Book(id, eId, condition, title, author, genre, publishYear, description, available);
		if(book.editionSet()) {
			editions.add(book.getEdition().get());
		}
		
		books.add(book);
		
		return Optional.empty();
	}
	public void deleteBook(String id) {
		Optional<Book> book = books.stream().filter(s -> s.getId().equals(id)).findFirst();
		if(book.isPresent()) books.remove(book.get());
	}
	
	public Optional<String> updateBook(String id, String nId, String eId, Integer condition, String title, String author, 
			String genre, Integer publishYear, String description, Boolean available) throws IllegalArgumentException{

		Optional<Book> old = books.stream().filter(s -> s.getId().equals(id)).findFirst();
		if(!old.isPresent()) return Optional.of("Requested Book not found, cant update");
		
		if((nId != null) && (!nId.equals(id))) {
			if(getBookById(nId).isPresent()) return Optional.of("This new id is already taken");
		}
		
		if(eId != null) {
			Optional<BookEdition> edition = getBookEditionById(eId);
			old.get().setEdition(edition);
		}
		// ok
		old.get().update(id, eId, condition, title, author, genre, publishYear, description, available);
		return Optional.empty();
	}
}

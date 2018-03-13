package library01.model;

import java.beans.PropertyChangeSupport;
import java.util.Optional;

import library01.bookapi.IBook;

public class BookEdition {
	private String id;
	private String title;
	private String author;
	private String genre;
	private Integer publishYear;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	
	public BookEdition(String id, String title, String author, String genre, Integer publishYear) throws IllegalArgumentException  {
		if((id == null) || (title == null) || (author == null) || (genre == null) || (publishYear == null)) 
			throw new IllegalArgumentException("Please fill only ID and EID fields or all the fields");
		validateAll(id, title, author, genre, publishYear);
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publishYear = publishYear;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
    	return title;
    }
    
    public String getAuthor() {
    	return author;
    }
    
    public String getGenre() {
    	return genre;
    }
    
    public Integer getPublishYear() {
    	return publishYear;
    }
    
    private void setTitle(String title) { 
        propertyChangeSupport.firePropertyChange("title", this.title,
                this.title = title);
    }
    
    private void setAuthor(String author) {
        propertyChangeSupport.firePropertyChange("author", this.author,
                this.author = author);
    }
    
    private void setGenre(String genre) {
        propertyChangeSupport.firePropertyChange("genre", this.genre,
                this.genre = genre);
    }
    
    private void setPublishYear(Integer publishYear) {
        propertyChangeSupport.firePropertyChange("publishYear", this.publishYear,
                this.publishYear = publishYear);
    }
    
    public static void validateAll(String id, String title, String author, String genre, Integer publishYear) throws IllegalArgumentException{
    	if((id != null)&&(id.length() < 3)) throw new IllegalArgumentException("The EID field must be at least 3 characters long!");
    	validate(title, author, genre, publishYear);
    }
    
    public static void validate(String title, String author, String genre, Integer publishYear) throws IllegalArgumentException{
    	if((title != null) && (title.length() < 2)) throw new IllegalArgumentException("The Title field must be at least 2 characters long!"); 
		if((author != null) && (author.length() < 3)) throw new IllegalArgumentException("The Author field must be at least 3 characters long!");
		if((genre != null) && (genre.length() < 3)) throw new IllegalArgumentException("The ID field must be at least 3 characters long!");
		if((publishYear != null) && ((publishYear > 2020) || (publishYear < -10000))) throw new IllegalArgumentException("The Publish Year must be between 10000bc and 2020");
    }
    
    void update(String title, String author, String genre, Integer publishYear) {
    	if(title != null) this.setTitle(title);
    	if(author != null) this.setAuthor(author);
    	if(genre != null) this.setGenre(genre);
    	if(publishYear != null) this.setPublishYear(publishYear);
    }
}

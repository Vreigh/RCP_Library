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
	private String description;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	
	public BookEdition(String id, String title, String author, String genre, Integer publishYear, String description) throws IllegalArgumentException  {
		if((id == null) || (title == null) || (author == null) || (genre == null) || (publishYear == null) || (description == null)) 
			throw new IllegalArgumentException("Book Edition - Please fill only ID, EID and Condition fields or all the fields");
		validateAll(id, title, author, genre, publishYear, description);
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publishYear = publishYear;
		this.description = description;
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
    
    public String getDescription() {
    	return description;
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
    
    private void setDescription(String description) {
        propertyChangeSupport.firePropertyChange("description", this.description,
                this.description = description);
    }
    
    public static void validateAll(String id, String title, String author, String genre, Integer publishYear, String description) throws IllegalArgumentException{
    	if((id != null)&&(id.length() < 3)) throw new IllegalArgumentException("The EID field must be at least 3 characters long!");
    	validate(title, author, genre, publishYear, description);
    }
    
    public static void validate(String title, String author, String genre, Integer publishYear, String description) throws IllegalArgumentException{
    	if((title != null) && (title.length() < 2)) throw new IllegalArgumentException("The Title field must be at least 2 characters long!"); 
		if((author != null) && (author.length() < 3)) throw new IllegalArgumentException("The Author field must be at least 3 characters long!");
		if((genre != null) && (genre.length() < 3)) throw new IllegalArgumentException("The ID field must be at least 3 characters long!");
		if((publishYear != null) && ((publishYear > 2020) || (publishYear < -10000))) throw new IllegalArgumentException("The Publish Year must be between 10000bc and 2020");
    }
    
    void update(String title, String author, String genre, Integer publishYear, String description) {
    	if(title != null) this.setTitle(title);
    	if(author != null) this.setAuthor(author);
    	if(genre != null) this.setGenre(genre);
    	if(publishYear != null) this.setPublishYear(publishYear);
    	if(description != null) this.setDescription(description);
    }
}

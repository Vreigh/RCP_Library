package library01.model;

import java.beans.PropertyChangeSupport;

public class BookEdition {
	private String id;
	private String title;
	private String author;
	private String genre;
	private Integer publishYear;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	
	public BookEdition(String id, String title, String author, String genre, Integer publishYear) {
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
    
    Boolean update(BookUpdateData update) {
    	if(update.title != null) this.setTitle(update.title);
    	if(update.author != null) this.setAuthor(update.author);
    	if(update.genre != null) this.setGenre(update.genre);
    	if(update.publishYear != null) this.setPublishYear(update.publishYear);
    	return true;
    }
}
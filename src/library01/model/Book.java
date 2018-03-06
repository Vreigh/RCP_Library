package library01.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

public class Book {
	private String id;
	private String title;
	private String author;
	private String genre;
	private Integer publishYear;
	private Boolean available;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	
	public Book() {
	}
	
	public Book(String id, String title, String author, String genre, Integer publishYear, Boolean available) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publishYear = publishYear;
		this.available = available;
	}
	
	public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
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
    
    public Boolean getAvailable() {
    	return available;
    }
    
    public void setId(String id) {
        propertyChangeSupport.firePropertyChange("id", this.id,
                this.id = id);
    }
    
    public void setTitle(String title) {
        propertyChangeSupport.firePropertyChange("title", this.title,
                this.title = title);
    }
    
    public void setAuthor(String author) {
        propertyChangeSupport.firePropertyChange("author", this.author,
                this.author = author);
    }
    
    public void setGenre(String genre) {
        propertyChangeSupport.firePropertyChange("genre", this.genre,
                this.genre = genre);
    }
    
    public void setPublishYear(Integer publishYear) {
        propertyChangeSupport.firePropertyChange("publishYear", this.publishYear,
                this.publishYear = publishYear);
    }
    
    public void setAvailable(Boolean available) {
        propertyChangeSupport.firePropertyChange("available", this.available,
                this.available = available);
    }
    
    @Override
    public String toString() {
        return id + ": " + title + ", by " + author;
    }
	
	
}

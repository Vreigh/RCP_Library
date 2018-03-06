package library01.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

public class Book {
	private String title;
	private String author;
	private String genre;
	private Date publishDate;
	private Boolean available;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	
	public Book() {
	}
	
	public Book(String title, String author, String genre, Date publishDate, Boolean available) {
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publishDate = publishDate;
		this.available = available;
	}
	
	public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
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
    
    public Date getPublishDate() {
    	return publishDate;
    }
    
    public Boolean getAvailable() {
    	return available;
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
    
    public void setPublishDate(Date publishDate) {
        propertyChangeSupport.firePropertyChange("publishDate", this.publishDate,
                this.publishDate = publishDate);
    }
    
    public void setAvailable(Boolean available) {
        propertyChangeSupport.firePropertyChange("available", this.available,
                this.available = available);
    }
    
    @Override
    public String toString() {
        return title + ", by " + author;
    }
	
	
}

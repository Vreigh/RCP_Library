package library01.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Optional;

import library01.bookapi.IBook;

public class Book implements IBook {
	private String id;
	private String eId;
	private Optional<BookEdition> edition;
	private Boolean available;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	
	public Book() {
	}
	
	public Book(String id, String eId, Boolean available) throws IllegalArgumentException {
		if((id == null) || (eId == null) || (available == null)) throw new IllegalArgumentException("Please fill only ID and EID fields or all the fields");
		validate(id, eId);
		this.id = id;
		this.eId = eId;
		this.available = available;
	}
	
	public Book(String id, String eId, String title, String author, String genre, Integer publishYear, Boolean available) throws IllegalArgumentException {
		this(id, eId, available);
		if((title == null) && (author == null) && (genre == null) && (publishYear == null)) return;
		this.setEdition(Optional.of(new BookEdition(eId, title, author, genre, publishYear)));
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
    
    public String getEId() {
    	return eId;
    }
    
    public String getTitle() {
    	if(edition.isPresent()) {
    		return edition.get().getTitle();
    	}else return null;
    }
    
    public String getAuthor() {
    	if(edition.isPresent()) {
    		return edition.get().getAuthor();
    	}else return null;
    }
    
    public String getGenre() {
    	if(edition.isPresent()) {
    		return edition.get().getGenre();
    	}else return null;
    }
    
    public Integer getPublishYear() {
    	if(edition.isPresent()) {
    		return edition.get().getPublishYear();
    	}else return null;
    }
    
    public String getPublishYearString() {
    	Integer year = getPublishYear();
    	if(year == null) {
    		return null;
    	}else if(year < 0) {
    		year = -year;
    		return year.toString() + " bc";
    	}else return year.toString();
    }
    
    public Boolean getAvailable() {
    	return available;
    }
    
    public Optional<BookEdition> getEdition() {
    	return edition;
    }
    
    private void setId(String id) {
        propertyChangeSupport.firePropertyChange("id", this.id,
                this.id = id);
    }
    
    private void setEId(String eId) {
        propertyChangeSupport.firePropertyChange("eId", this.eId,
                this.eId = eId);
    }
    
    public Boolean editionSet() {
    	return edition.isPresent();
    }
    
    public void setEdition(Optional<BookEdition> edition) {
    	if(edition.isPresent()) {
    		if(!edition.get().getId().equals(eId)) {
    			// rzuć wyjątkiem
    		}
    	}
    	this.edition = edition;
    }
    
    private void setAvailable(Boolean available) {
        propertyChangeSupport.firePropertyChange("available", this.available,
                this.available = available);
    }
    
    @Override
    public String toString() {
        return "id: " + id + " eId: " + eId + " available: " + available;
    }
    
    public void update(String id, String eId, String title, String author, String genre, 
    		Integer publishYear, Boolean available) throws IllegalArgumentException {
    	validate(id, eId);
    	BookEdition.validate(title, author, genre, publishYear);
    	if(id != null) this.setId(id);
    	if(eId != null) this.setEId(eId);
    	if(available != null) this.setAvailable(available);
    	if(edition.isPresent()) {
    		edition.get().update(title, author, genre, publishYear);
    	}
    }
    
    public static void validate(String id, String eId) throws IllegalArgumentException{ // walidacja
    	if((id != null)&& (id.length() < 3)) throw new IllegalArgumentException("The ID field must be at least 3 characters long!");
		if((eId != null)&&(eId.length() < 3)) throw new IllegalArgumentException("The EID field must be at least 3 characters long!");
	}
}

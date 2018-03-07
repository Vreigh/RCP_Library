package library01.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.Optional;

public class Book {
	private String id;
	private String eId;
	private Optional<BookEdition> edition;
	private Boolean available;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	
	public Book() {
	}
	
	public Book(String id, String eId, Boolean available) {
		this.id = id;
		this.eId = eId;
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
    
    public String getEId() {
    	return eId;
    }
    
    public String getTitle() {
    	if(edition.isPresent()) {
    		return edition.get().getTitle();
    	}else return "BD";
    }
    
    public String getAuthor() {
    	if(edition.isPresent()) {
    		return edition.get().getAuthor();
    	}else return "BD";
    }
    
    public String getGenre() {
    	if(edition.isPresent()) {
    		return edition.get().getGenre();
    	}else return "BD";
    }
    
    public Integer getPublishYear() {
    	if(edition.isPresent()) {
    		return edition.get().getPublishYear();
    	}else return Integer.MIN_VALUE;
    }
    
    public String getPublishYearString() {
    	Integer year = getPublishYear();
    	if(year == Integer.MIN_VALUE) {
    		return "BD";
    	}else if(year < 0) {
    		year = -year;
    		return year.toString() + " bc";
    	}else return year.toString();
    }
    
    public Boolean getAvailable() {
    	return available;
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
    
    public Boolean update(BookUpdateData update){ // TO DO: lepsza kontrola spójności
    	if(update.id != null) this.setId(update.id);
    	if(update.eId != null) this.setEId(update.eId);
    	if(update.available != null) this.setAvailable(update.available);
    	if(edition.isPresent()) return edition.get().update(update);
    	else return true;
    }
}

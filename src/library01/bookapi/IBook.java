package library01.bookapi;

import java.util.Optional;

import library01.model.BookEdition;
import library01.model.BookUpdateData;

public interface IBook {
	public String getId();
    
    public String getEId();
    
    public String getTitle();
    
    public String getAuthor();
    
    public String getGenre();
    
    public Integer getPublishYear();
    
    public String getPublishYearString();
    
    public Boolean getAvailable();
    
    public Optional<String> update(BookUpdateData update);
    
    public Boolean editionSet(); // czy wszystkie dane ksiazki uzupelnione
    
    public void setEdition(Optional<BookEdition> edition);

}

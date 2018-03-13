package library01.bookapi;


public interface IBook {
	public String getId();
    
    public String getEId();
    
    public String getTitle();
    
    public String getAuthor();
    
    public String getGenre();
    
    public Integer getPublishYear();
    
    public String getPublishYearString();
    
    public Boolean getAvailable();
    
    public void update(String id, String eId, String title, String author, String genre, Integer publishYear, Boolean available);
    
    public Boolean editionSet(); // czy wszystkie dane ksiazki uzupelnione
    
}

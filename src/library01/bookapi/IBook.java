package library01.bookapi;


public interface IBook {
	public String getId();
    
    public String getEId();
    
    public Integer getCondition();
    
    public String getConditionString();
    
    public String getTitle();
    
    public String getAuthor();
    
    public String getGenre();
    
    public Integer getPublishYear();
    
    public String getPublishYearString();
    
    public String getDescription();
    
    public Boolean getAvailable();
    
    public void update(String id, String eId, Integer condition, String title, String author, String genre, Integer publishYear, String description, Boolean available);
    
}

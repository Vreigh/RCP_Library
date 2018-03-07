package library01.model;

public class BookUpdateData {
	public String id;
	public String eId;
	public String title;
	public String author;
	public String genre;
	public Integer publishYear;
	public Boolean available;
	
	public BookUpdateData(String id, String eId, String title, String author, 
			String genre, Integer publishYear, Boolean available) {
		this.id = id;
		this.eId = eId;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publishYear = publishYear;
		this.available = available;
	}

}

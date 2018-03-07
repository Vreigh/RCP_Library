package library01.model;

import java.util.Optional;

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
	
	public Optional<String> validateCreateWithEdition(){
		Boolean correct = false;
		if((id != null) && (eId != null) && (title != null) && (author != null) && (genre != null) && (publishYear != null)) {
        	correct = true;
        }
        if((id != null) && (eId != null) && (title == null) && (author == null) && (genre == null) && (publishYear == null)) {
        	correct = true;
        }
        if(!correct) return Optional.of("Please fill all the values or just ID and EID");
        
        return validatePresent();
	}
	
	public Optional<String> validateCreate(){
		if(!((id != null) && (eId != null) && (title == null) && (author == null) && (genre == null) && (publishYear == null))) {
			return Optional.of("Not all the values are set!");
        }
		
		return validatePresent();
	}
	
	public Optional<String> validatePresent(){ // walidacja
		if((id != null) && (id.length() < 3)) return Optional.of("The ID field must be at least 3 characters long!");
		if((eId != null) && (eId.length() < 3)) return Optional.of("The EID field must be at least 3 characters long!"); 
		if((title != null) && (title.length() < 2)) return Optional.of("The Title field must be at least 2 characters long!"); 
		if((author != null) && (author.length() < 3)) return Optional.of("The Author field must be at least 3 characters long!"); 
		if((genre != null) && (genre.length() < 3)) return Optional.of("The ID field must be at least 3 characters long!"); 
		if((id != null) && (id.length() < 3)) return Optional.of("The ID field must be at least 3 characters long!");
		if((publishYear != null) && ((publishYear > 2020) || (publishYear < -10000))) return Optional.of("The Publish Year must be between 10000bc and 2020");
		
		return Optional.empty();
	}

}

package library01.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import library01.bookapi.IBook;
import library01.model.Book;

public class BookFilter extends ViewerFilter {
	private String searchString;

    public void setSearchText(String s) {
        this.searchString = ".*" + s + ".*";
    }
    
    private Boolean search(String a, String b) {
    	if(a == null) a = "";
    	if(b == null) b = "";
    	return a.matches(b);
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }
        
        IBook book = (IBook) element;
        if (search(book.getId(), searchString)) {
            return true;
        }
        if(search(book.getEId(), searchString)) {
        	return true;
        }
        if (search(book.getTitle(), searchString)) {
            return true;
        }
        if (search(book.getAuthor(), searchString)) {
            return true;
        }
        if (search(book.getGenre(), searchString)) {
            return true;
        }
        if (search(book.getPublishYearString(), searchString)) {
            return true;
        }

        return false;
    }
}

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

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }
        
        IBook book = (IBook) element;
        if (book.getId().matches(searchString)) {
            return true;
        }
        if(book.getEId().matches(searchString)) {
        	return true;
        }
        if (book.getTitle().matches(searchString)) {
            return true;
        }
        if (book.getAuthor().matches(searchString)) {
            return true;
        }
        if (book.getGenre().matches(searchString)) {
            return true;
        }
        if (book.getPublishYearString().matches(searchString)) {
            return true;
        }

        return false;
    }
}

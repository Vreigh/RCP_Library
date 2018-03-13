package library01.sorter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import library01.bookapi.IBook;
import library01.model.Book;

public class ByColumnViewerComparator extends ViewerComparator { // dynamiczne sortowanie (po różnych kolumnach)
	private int propertyIndex;
    private static final int DESCENDING = 1;
    private int direction = DESCENDING;

    public ByColumnViewerComparator() {
        this.propertyIndex = 1;
        direction = DESCENDING;
    }

    public int getDirection() {
        return direction == 1 ? SWT.DOWN : SWT.UP;
    }

    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            // jesli kolejne żądanie sortowania po tej samej kolumnie, obracamy kierunek
            direction = 1 - direction;
        } else {
            // nowe sortowanie
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }
    
    private int compare(String a, String b) {
    	if(a == null) a = "";
    	if(b == null) b = "";
    	return a.compareTo(b);
    }
    
    private int compare(Integer a, Integer b) {
    	if(a == null) a = Integer.MIN_VALUE;
    	if(b == null) b= Integer.MIN_VALUE;
    	return a.compareTo(b);
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        IBook b1 = (IBook) e1;
        IBook b2 = (IBook) e2;
        int rc = 0;
        switch (propertyIndex) { // kolejnosc musi byc taka sama jak przy tworzeniu kolumn
        case 0:
            rc = compare(b1.getId(), b2.getId());
            break;
        case 1:
        	rc = compare(b1.getEId(), b2.getEId());
            break;
        case 2:
        	rc = compare(b1.getTitle(), b2.getTitle());
            break;
        case 3:
        	rc = compare(b1.getAuthor(), b2.getAuthor());
            break;
        case 4:
        	rc = compare(b1.getGenre(), b2.getGenre());
            break;
        case 5:
        	rc = compare(b1.getPublishYear(), b2.getPublishYear());
            break;
        case 6:
        	rc = b1.getAvailable().compareTo(b2.getAvailable());
            break;
        default:
            rc = 0;
        }
        // odwrotnie jesli descending
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
}

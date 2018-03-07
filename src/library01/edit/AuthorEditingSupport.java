package library01.edit;

import org.eclipse.jface.viewers.TableViewer;

import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class AuthorEditingSupport extends TitleEditingSupport{
	private final TableViewer viewer;

    public AuthorEditingSupport(TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }
    
    @Override
    protected Object getValue(Object element) {
        return ((Book) element).getAuthor();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	Book book = (Book) element;
    	String input = String.valueOf(userInputValue);
    	BookUpdateData update = new BookUpdateData(null, null, null, input, null, null, null);
    	
    	if(DataProvider.INSTANCE.updateBook(book.getId(), update)) {
    		book.update(update);
    		viewer.refresh(); // odswiez wszystko (po kilka rzedow moglo sie zmienic
    	}else {
    		System.out.println("Edycja niemozliwa");
    	}
    }
}

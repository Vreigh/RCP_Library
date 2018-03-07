package library01.edit;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class IdEditingSupport extends TitleEditingSupport {
	private final TableViewer viewer;

    public IdEditingSupport(TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }
    
    @Override
    protected boolean canEdit(Object element) {
        return true; // to pole mozna zawsze edytowac
    }

    @Override
    protected Object getValue(Object element) {
        return ((Book) element).getId();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	Book book = (Book) element;
    	String newId = String.valueOf(userInputValue);
    	BookUpdateData update = new BookUpdateData(newId, null, null, null, null, null, null);
    	
    	if(DataProvider.INSTANCE.updateBook(book.getId(), update)) {
    		book.update(update);
    		viewer.update(element, null); // wystarczy updatowac ten jeden element
    	}else {
    		System.out.println("Edycja niemozliwa");
    	}
    }
}

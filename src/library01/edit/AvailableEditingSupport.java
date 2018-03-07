package library01.edit;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class AvailableEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private final CheckboxCellEditor editor;

    public AvailableEditingSupport(TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
        
        editor = new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);;
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
    	return editor;
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        return ((Book)element).getAvailable();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	Book book = (Book) element;
    	Boolean input = (Boolean)userInputValue;
    	
    	BookUpdateData update = new BookUpdateData(null, null, null, null, null, null, input);
    	
    	if(DataProvider.INSTANCE.updateBook(book.getId(), update)) {
    		book.update(update);
    		viewer.update(element, null); // wystarczy updatowac ten jeden element
    	}else {
    		System.out.println("Edycja niemozliwa");
    	}
    }
}

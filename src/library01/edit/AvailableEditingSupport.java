package library01.edit;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class AvailableEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private final CheckboxCellEditor editor;
	private Shell parentShell;

    public AvailableEditingSupport(TableViewer viewer, Shell parentShell) {
        super(viewer);
        this.viewer = viewer;
        this.parentShell = parentShell;
        
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
        return ((IBook)element).getAvailable();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	if (MessageDialog.openConfirm(parentShell, "Confirmation","Change the status of that book?")) {
    		IBook book = (Book) element;
        	Boolean input = (Boolean)userInputValue;
        	
        	BookUpdateData update = new BookUpdateData(null, null, null, null, null, null, input);
        	
        	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), update);
        	
        	if(!error.isPresent()) {
        		book.update(update);
        		viewer.update(element, null); // wystarczy updatowac ten jeden element
        	}else {
        		MessageDialog.openError(parentShell, "Invalid data", error.get());
        	}
    	}
    }
}

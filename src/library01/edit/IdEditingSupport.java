package library01.edit;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;

public class IdEditingSupport extends TitleEditingSupport {
	private final TableViewer viewer;

    public IdEditingSupport(TableViewer viewer, Shell parentShell) {
        super(viewer, parentShell);
        this.viewer = viewer;
    }
    
    @Override
    protected boolean canEdit(Object element) {
        return true; // to pole mozna zawsze edytowac
    }

    @Override
    protected Object getValue(Object element) {
        return ((IBook) element).getId();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	IBook book = (IBook) element;
    	String newId = String.valueOf(userInputValue);
    	
    	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), newId, null, null, null, null, null, null);
    	
    	if(!error.isPresent()) {
    		book.update(newId, null, null, null, null, null, null);
    		viewer.update(element, null); // wystarczy updatowac ten jeden element
    	}else {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}
    }
}

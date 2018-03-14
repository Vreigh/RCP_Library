package library01.edit;

import org.eclipse.jface.viewers.EditingSupport;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;

public class ConditionEditingSupport extends EditingSupport{
	private final TableViewer viewer;
	private Shell parentShell;

    public ConditionEditingSupport(TableViewer viewer, Shell parentShell) {
        super(viewer);
        this.viewer = viewer;
        this.parentShell = parentShell;
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        String[] data = ((IBook)element).getConditions();
        return new ComboBoxCellEditor(viewer.getTable(), data);
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        IBook book = (IBook) element;
        return book.getCondition();

    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	IBook book = (IBook) element;
    	Integer input = (Integer)userInputValue;
    	
    	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), null, null, input, null, null, null, null, null, null);
    	
    	if(!error.isPresent()) {
    		book.update(null, null, input, null, null, null, null, null, null);
    		viewer.update(element, null); // wystarczy updatowac ten jeden element
    	}else {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}
    }
}

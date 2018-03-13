package library01.edit;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;

public class TitleEditingSupport extends EditingSupport {
	private final TableViewer viewer;
    private final CellEditor editor;
    protected final Shell parentShell;

    public TitleEditingSupport(TableViewer viewer, Shell parentShell) {
        super(viewer);
        this.viewer = viewer;
        this.editor = new TextCellEditor(viewer.getTable());
        this.parentShell = parentShell;
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return editor;
    }

    @Override
    protected boolean canEdit(Object element) {
        return ((IBook)element).getTitle() != null;
    }

    @Override
    protected Object getValue(Object element) {
        return ((IBook) element).getTitle();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	IBook book = (IBook) element;
    	String input = String.valueOf(userInputValue);
    	
    	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), null, null, input, null, null, null, null);
    	
    	if(!error.isPresent()) {
    		book.update(null, null, input, null, null, null, null);
    		viewer.refresh();
    	}else {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}
    }

}

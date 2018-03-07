package library01.edit;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class TitleEditingSupport extends EditingSupport {
	private final TableViewer viewer;
    private final CellEditor editor;

    public TitleEditingSupport(TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
        this.editor = new TextCellEditor(viewer.getTable());
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return editor;
    }

    @Override
    protected boolean canEdit(Object element) {
        return ((Book)element).editionSet();
    }

    @Override
    protected Object getValue(Object element) {
        return ((Book) element).getTitle();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	Book book = (Book) element;
    	String newTitle = String.valueOf(userInputValue);
    	BookUpdateData update = new BookUpdateData(null, null, String.valueOf(userInputValue), null, null, null, null);
    	if(DataProvider.INSTANCE.updateBook(book.getId(), update)) {
    		book.update(update);
    		viewer.refresh();
    	}else {
    		// Daj pop-up z bledem
    	}
    }

}

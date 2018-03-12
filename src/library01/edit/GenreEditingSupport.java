package library01.edit;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class GenreEditingSupport extends TitleEditingSupport {
	private final TableViewer viewer;

    public GenreEditingSupport(TableViewer viewer, Shell parentShell) {
        super(viewer, parentShell);
        this.viewer = viewer;
    }
    
    @Override
    protected Object getValue(Object element) {
        return ((IBook) element).getGenre();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	IBook book = (IBook) element;
    	String input = String.valueOf(userInputValue);
    	BookUpdateData update = new BookUpdateData(null, null, null, null, input, null, null);
    	
    	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), update);
    	
    	if(!error.isPresent()) {
    		book.update(update);
    		viewer.refresh(); // odswiez wszystko (po kilka rzedow moglo sie zmienic)
    	}else {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}
    }
}

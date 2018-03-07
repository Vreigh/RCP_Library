package library01.edit;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;

import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class PublishYearEditingSupport extends TitleEditingSupport{
	private final TableViewer viewer;

    public PublishYearEditingSupport(TableViewer viewer, Shell parentShell) {
        super(viewer, parentShell);
        this.viewer = viewer;
    }
    
    @Override
    protected Object getValue(Object element) {
        return ((Book) element).getPublishYear().toString(); // w edycji wyswietlam -x. Mam gwarancje, ze to nie bedzie MIN_VALUE, 
        // bo do edycji roku wydania edycja musi byc ustawiona 
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	Book book = (Book) element;
    	String tmp = String.valueOf(userInputValue);
    	Integer input;
    	try {
    		input = Integer.valueOf(tmp);
    	}catch(Exception e) {
    		MessageDialog.openError(parentShell, "Invalid data", "Publish Year must be a number!");
    		return;
    	}
    	
    	BookUpdateData update = new BookUpdateData(null, null, null, null, null, input, null);
    	
    	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), update);
    	
    	if(!error.isPresent()) {
    		book.update(update);
    		viewer.refresh(); // odswiez wszystko (po kilka rzedow moglo sie zmienic
    	}else {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}
    }
}

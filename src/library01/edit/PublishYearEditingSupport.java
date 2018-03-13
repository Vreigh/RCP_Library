package library01.edit;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;

public class PublishYearEditingSupport extends TitleEditingSupport{
	private final TableViewer viewer;

    public PublishYearEditingSupport(TableViewer viewer, Shell parentShell) {
        super(viewer, parentShell);
        this.viewer = viewer;
    }
    
    @Override
    protected Object getValue(Object element) {
        return ((IBook) element).getPublishYear().toString();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	IBook book = (IBook) element;
    	String tmp = String.valueOf(userInputValue);
    	Integer input;
    	try {
    		input = Integer.valueOf(tmp);
    	}catch(Exception e) {
    		MessageDialog.openError(parentShell, "Invalid data", "Publish Year must be a number!");
    		return;
    	}
    	
    	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), null, null, null, null, null, input, null);
    	
    	if(!error.isPresent()) {
    		book.update(null, null, null, null, null, input, null);
    		viewer.refresh(); // odswiez wszystko (po kilka rzedow moglo sie zmienic
    	}else {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}
    }
}

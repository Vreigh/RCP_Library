package library01.handlers;

import java.util.Iterator;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.parts.ViewManager;

public class DeleteHandler{
	
	@Execute
    public void execute(Shell shell) {
		if (MessageDialog.openConfirm(shell, "Confirmation","Are you sure you want to delete these books?")) {
			
	        ISelection selection = ViewManager.INSTANCE.getSelection("books");
	        if (selection != null && selection instanceof IStructuredSelection) {
	            IStructuredSelection sel = (IStructuredSelection) selection;
	            for (Iterator<Book> iterator = sel.iterator(); iterator.hasNext();) {
	                IBook book = iterator.next();
	                DataProvider.INSTANCE.deleteBook(book.getId());
	            }
	            ViewManager.INSTANCE.reload("books");
	        }
			
		}
    }
}

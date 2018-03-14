package library01.handlers;


import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dialogs.UpdateBookDialog;
import library01.parts.ViewManager;

public class UpdateHandler {
	@Execute
	public void execute(Shell shell){
		ISelection selection = ViewManager.INSTANCE.getSelection("books");
        if (selection != null && selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            IBook book = (IBook)sel.getFirstElement();
            if(book != null) {
            	UpdateBookDialog dialog = new UpdateBookDialog(shell, book);
        		dialog.create();
        		if(dialog.open() == Window.OK) {
        			ViewManager.INSTANCE.reload("books");
        		}
            }
        }
	}
}

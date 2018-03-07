package library01.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import library01.dialogs.AddBookDialog;
import library01.parts.ViewManager;

public class CreateHandler {
	
	@Execute
	public void execute(Shell shell){
		AddBookDialog dialog = new AddBookDialog(shell);
		dialog.create();
		if(dialog.open() == Window.OK) {
			ViewManager.INSTANCE.reload("books");
		}
	}
}

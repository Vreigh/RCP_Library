package library01.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class CreateHandler {
	
	@Execute
	public void execute(Shell shell){
		System.out.println("Hello World!");
	}
}

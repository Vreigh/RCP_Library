package library01.parts;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;

public interface IndexView {
	public void refresh();
	public void reload();
	public ISelection getSelection();
	public Object getData();
	public Shell getShell();
}

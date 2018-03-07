package library01.parts;

import org.eclipse.jface.viewers.ISelection;

public interface IndexView {
	public void refresh();
	public void reload();
	public ISelection getSelection();
}

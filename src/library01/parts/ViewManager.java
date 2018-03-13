package library01.parts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;

public enum ViewManager {
	INSTANCE;
	
	private Map<String, IndexView> views = new HashMap<String, IndexView>();
	
	public void addView(String name, IndexView view) {
		views.put(name, view);
	}
	
	public Object getData(String name) {
		IndexView view = views.get(name);
		if(view == null) return null;
		return view.getData();
	}
	
	public void refresh(String name) {
		IndexView view = views.get(name);
		if(view != null) view.refresh();
	}
	
	public void reload(String name) {
		IndexView view = views.get(name);
		if(view != null) view.reload();
	}
	
	public ISelection getSelection(String name) {
		IndexView view = views.get(name);
		if(view == null) return null;
		return view.getSelection();
	}
	
	public Shell getShell(String name) {
		IndexView view = views.get(name);
		if(view == null) return null;
		return view.getShell();
	}

}

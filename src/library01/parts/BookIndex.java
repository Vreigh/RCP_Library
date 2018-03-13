package library01.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;
import library01.edit.AuthorEditingSupport;
import library01.edit.AvailableEditingSupport;
import library01.edit.EIdEditingSupport;
import library01.edit.GenreEditingSupport;
import library01.edit.IdEditingSupport;
import library01.edit.PublishYearEditingSupport;
import library01.edit.TitleEditingSupport;
import library01.filter.BookFilter;
import library01.setup.ConfigFileSetupper;
import library01.sorter.ByColumnViewerComparator;

public class BookIndex implements IndexView{
	private TableViewer viewer;
		
	private BookFilter filter;
	private ByColumnViewerComparator comparator;
	private Shell parentShell;
	
	public void refresh() {
		viewer.refresh();
	}
	
	public void reload() {
		viewer.setInput(DataProvider.INSTANCE.getBooks());
	}
	
	public Object getData() {
		return viewer.getInput();
	}
	
	public ISelection getSelection() {
		return viewer.getSelection();
	}
	
	public Shell getShell() {
		return parentShell;
	}
	
	@PostConstruct
	public void createComposite(Composite parent) {
		parentShell = parent.getShell();
		parentShell.setBounds(200, 200, 1150, 700);
		GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        
        Label searchLabel = new Label(parent, SWT.NONE);
        searchLabel.setText("Search: ");
        final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        
        createViewer(parent);
        
        comparator = new ByColumnViewerComparator();
        viewer.setComparator(comparator);
        
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                filter.setSearchText(searchText.getText());
                viewer.refresh();
            }

        });
        filter = new BookFilter();
        viewer.addFilter(filter);
        
        ViewManager.INSTANCE.addView("books", this);
	}
	
	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        
        DataProvider.newInstance(new ConfigFileSetupper());
        viewer.setInput(DataProvider.INSTANCE.getBooks());
        
        // define layout for the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
        
	}
	
	public TableViewer getViewer() {
        return viewer;
    }
	
	// tworzenie kolumn dla widoku
    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "ID", "EID", "Title", "Author", "Genre", "Publish Year", "Condition", "Available?"};
        int[] bounds = { 110, 110, 180, 180, 180, 120, 160, 100};
        
        Integer i = 0;

        // ID
        TableViewerColumn col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((IBook) cell.getElement()).getId());
            }
        });
        col.setEditingSupport(new IdEditingSupport(viewer, parentShell));
        i++;
        
        // EID
        col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((IBook) cell.getElement()).getEId());
            }
        });
        col.setEditingSupport(new EIdEditingSupport(viewer, parentShell));
        i++;
        
        // Title
        col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((IBook) cell.getElement()).getTitle());
            }
        });
        col.setEditingSupport(new TitleEditingSupport(viewer, parentShell));
        i++;

        // Author
        col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((IBook) cell.getElement()).getAuthor());
            }
        });
        col.setEditingSupport(new AuthorEditingSupport(viewer, parentShell));
        i++;
        
        // Genre
        col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((IBook) cell.getElement()).getGenre());
            }
        });
        col.setEditingSupport(new GenreEditingSupport(viewer, parentShell));
        i++;
        
        // Publish Year
        col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((IBook) cell.getElement()).getPublishYearString());
            }
        });
        col.setEditingSupport(new PublishYearEditingSupport(viewer, parentShell));
        i++;
        
        // CON
        col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((IBook) cell.getElement()).getConditionString());
            }
        });
        //col.setEditingSupport(new EIdEditingSupport(viewer, parentShell));
        i++;
        
        // Available
        col = createTableViewerColumn(titles[i], bounds[i], i);
        col.setLabelProvider(new ColumnLabelProvider() {
        	@Override
            public String getText(Object element) {
        		if (((IBook) element).getAvailable()) {
                    return "YES";
                } else {
                    return "NO";
                }
            }
        });
        col.setEditingSupport(new AvailableEditingSupport(viewer, parentShell));
    }

    private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        column.addSelectionListener(getSelectionAdapter(column, colNumber));
        return viewerColumn;
    }
    
    private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if(index != 7) {
            		comparator.setColumn(index);
                    int dir = comparator.getDirection();
                    viewer.getTable().setSortDirection(dir);
                    viewer.getTable().setSortColumn(column);
                    viewer.refresh();
            	}
            }
        };
        return selectionAdapter;
    }

    public void setFocus() {
        viewer.getControl().setFocus();
    }
	
	
}

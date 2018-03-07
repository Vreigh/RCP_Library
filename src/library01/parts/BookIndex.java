package library01.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import library01.dataprovider.DataProvider;
import library01.edit.AuthorEditingSupport;
import library01.edit.AvailableEditingSupport;
import library01.edit.EIdEditingSupport;
import library01.edit.GenreEditingSupport;
import library01.edit.IdEditingSupport;
import library01.edit.PublishYearEditingSupport;
import library01.edit.TitleEditingSupport;
import library01.filter.BookFilter;
import library01.model.Book;
import library01.sorter.ByColumnViewerComparator;

public class BookIndex{
	private TableViewer viewer;
		
	private BookFilter filter;
	private ByColumnViewerComparator comparator;
	
	@PostConstruct
	public void createComposite(Composite parent) {
		Shell body = parent.getShell();
		body.setSize(950, 700);
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
	}
	
	
	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setInput(DataProvider.INSTANCE.getBooks());
        
        //getSite().setSelectionProvider(viewer);
        // set the sorter for the table

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
        String[] titles = { "ID", "EID", "Title", "Author", "Genre", "Publish Year", "Available?" };
        int[] bounds = { 110, 110, 150, 150, 200, 120, 90 };

        // ID
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((Book) cell.getElement()).getId());
            }
        });
        col.setEditingSupport(new IdEditingSupport(viewer));
        
        // EID
        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((Book) cell.getElement()).getEId());
            }
        });
        col.setEditingSupport(new EIdEditingSupport(viewer));

        // Title
        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((Book) cell.getElement()).getTitle());
            }
        });
        col.setEditingSupport(new TitleEditingSupport(viewer));

        // Author
        col = createTableViewerColumn(titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((Book) cell.getElement()).getAuthor());
            }
        });
        col.setEditingSupport(new AuthorEditingSupport(viewer));
        
        // Genre
        col = createTableViewerColumn(titles[4], bounds[4], 4);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((Book) cell.getElement()).getGenre());
            }
        });
        col.setEditingSupport(new GenreEditingSupport(viewer));
        
        // Publish Year
        col = createTableViewerColumn(titles[5], bounds[5], 5);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
            	cell.setText(((Book) cell.getElement()).getPublishYearString());
            }
        });
        col.setEditingSupport(new PublishYearEditingSupport(viewer));
        
        // Available
        col = createTableViewerColumn(titles[6], bounds[6], 6);
        col.setLabelProvider(new ColumnLabelProvider() {
        	@Override
            public String getText(Object element) {
        		if (((Book) element).getAvailable()) {
                    return "YES";
                } else {
                    return "NO";
                }
            }

            /*@Override
            public Image getImage(Object element) {
                if (((Book) element).getAvailable()) {
                    return CHECKED;
                } else {
                    return UNCHECKED;
                }
            }*/
        });
        col.setEditingSupport(new AvailableEditingSupport(viewer));

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
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                viewer.getTable().setSortDirection(dir);
                viewer.getTable().setSortColumn(column);
                viewer.refresh();
            }
        };
        return selectionAdapter;
    }

    public void setFocus() {
        viewer.getControl().setFocus();
    }
	
	
}

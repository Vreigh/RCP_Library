package Library01.dialogs;


import java.util.Optional;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import library01.dataprovider.DataProvider;
import library01.model.BookUpdateData;

public class AddBookDialog extends TitleAreaDialog {
	private Text idTxt;
    private Text eIdTxt;
    private Text titleTxt;
    private Text authorTxt;
    private Text genreTxt;
    private Text publishYearTxt;
    
    private String id = null;
    private String eId = null;
    private String title = null;
    private String author = null;
    private String genre = null;
    private Integer publishYear = null;
    
    private Shell parentShell;

    public AddBookDialog(Shell parentShell) {
        super(parentShell);
        this.parentShell = parentShell;
    }

    @Override
    public void create() {
        super.create();
        setTitle("Adding a book");
        setMessage("To add a book with an existing Edition, fill only ID and EID fields", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);
        
        
        Label label = new Label(container, SWT.NONE);
    	label.setText("ID: ");
        idTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(container, idTxt, "ID: ");
        
        label = new Label(container, SWT.NONE);
    	label.setText("EID: ");
        eIdTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(container, eIdTxt, "EID: ");
        
        label = new Label(container, SWT.NONE);
    	label.setText("TITLE: ");
        titleTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(container, titleTxt, "TITLE: ");
        
        label = new Label(container, SWT.NONE);
    	label.setText("AUTHOR: ");
        authorTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(container, authorTxt, "AUTHOR: ");
        
        label = new Label(container, SWT.NONE);
    	label.setText("GENRE: ");
        genreTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(container, genreTxt, "GENRE: ");
        
        label = new Label(container, SWT.NONE);
    	label.setText("PUBLISH YEAR: ");
        publishYearTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(container, publishYearTxt, "PUBLISH YEAR: ");
        
        return area;
    }

    
    private void prepareTxtInput(Composite container, Text text, String name) {
        GridData data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;

        text.setLayoutData(data);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    private void saveInput() { // tutaj mozna dolozyc pierwsza walidacje
    	if(!idTxt.getText().equals("")) id = idTxt.getText();
    	if(!eIdTxt.getText().equals("")) eId = eIdTxt.getText();
    	if(!titleTxt.getText().equals("")) title = titleTxt.getText();
    	if(!authorTxt.getText().equals("")) author = authorTxt.getText();
    	if(!genreTxt.getText().equals("")) genre = genreTxt.getText();
    	if(!publishYearTxt.getText().equals("")) {
    		try {
    			publishYear = Integer.valueOf(publishYearTxt.getText());
    		}catch(Exception e) {
    			MessageDialog.openError(parentShell, "Wrong format!", "Publish Year should be a number!");
    			publishYearTxt.setText("");
    			publishYear = null;
    		}
    	}
    }

    @Override
    protected void okPressed() {
        saveInput();
    	Optional<String> error = DataProvider.INSTANCE.addNewBook(get());
    	if(error.isPresent()) {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}else {
    		MessageDialog.openConfirm(parentShell, "Success!", "Book added successfuly");
    		super.okPressed();
    	}
         
    }
    
    public BookUpdateData get() {
    	return new BookUpdateData(id, eId, title, author, genre, publishYear, true);
    }
}

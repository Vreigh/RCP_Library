package library01.dialogs;


import java.util.Optional;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import library01.dataprovider.DataProvider;
import library01.model.Book;

public class AddBookDialog extends TitleAreaDialog {
	protected Text idTxt;
	protected Text eIdTxt;
	protected Combo conditionCombo;
	protected Text titleTxt;
	protected Text authorTxt;
	protected Text genreTxt;
	protected Text publishYearTxt;
	protected Text descriptionTxt;
    
	protected String id = null;
	protected String eId = null;
	protected Integer condition = null;
	protected String title = null;
	protected String author = null;
	protected String genre = null;
	protected Integer publishYear = null;
	protected String description = null;
    
	protected Shell parentShell;

    public AddBookDialog(Shell parentShell) {
        super(parentShell);
        this.parentShell = parentShell;
    }

    @Override
    public void create() {
        super.create();
        setTitle("Adding a book");
        setMessage("To add a book with an existing Edition, fill only ID, EID and Condition fields", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
    	Shell shell = parent.getShell();
    	shell.setBounds(200, 200, 600, 600);
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);
        
        Label label = new Label(container, SWT.NONE);
    	label.setText("ID: ");
        idTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(idTxt);
        
        label = new Label(container, SWT.NONE);
    	label.setText("EID: ");
        eIdTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(eIdTxt);
        
        label = new Label(container, SWT.NONE);
    	label.setText("CONDITION: ");
    	conditionCombo = new Combo(container, SWT.DROP_DOWN);
    	conditionCombo.setItems(Book.getConditionsStatic());
    	conditionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        
        label = new Label(container, SWT.NONE);
    	label.setText("TITLE: ");
        titleTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(titleTxt);
        
        label = new Label(container, SWT.NONE);
    	label.setText("AUTHOR: ");
        authorTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(authorTxt);
        
        label = new Label(container, SWT.NONE);
    	label.setText("GENRE: ");
        genreTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(genreTxt);
        
        label = new Label(container, SWT.NONE);
    	label.setText("PUBLISH YEAR: ");
        publishYearTxt = new Text(container, SWT.BORDER);
        prepareTxtInput(publishYearTxt);
        
        label = new Label(container, SWT.NONE);
    	label.setText("DESCRIPTION: ");
        descriptionTxt = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        descriptionTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
        
        return area;
    }

    private void prepareTxtInput(Text text) {
        GridData data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;

        text.setLayoutData(data);
    }
    
    @Override
    protected boolean isResizable() {
        return true;
    }
    
    private String override(String a) {
    	if(a.equals("")) {
    		return null;
    	}else return a;
    }
    
    private Integer getIntegerFromText(Text text, String error) {
    	if(!text.getText().equals("")) {
    		try {
    			return Integer.valueOf(text.getText());
    		}catch(Exception e) {
    			MessageDialog.openError(parentShell, "Wrong format!", error);
    			text.setText("");
    			return null;
    		}
    	}else return null;
    }
    
    protected void saveInput() {
    	id = override(idTxt.getText());
    	eId = override(eIdTxt.getText());
    	condition = conditionCombo.getSelectionIndex();
    	if(condition == -1) condition = null;
    	title = override(titleTxt.getText());
    	author = override(authorTxt.getText());
    	genre = override(genreTxt.getText());
    	publishYear = getIntegerFromText(publishYearTxt, "Wrong format of Publish Year field");
    	description = override(descriptionTxt.getText());
    }
    
    protected void ok() {
    	super.okPressed();
    }

    @Override
    protected void okPressed() {
        saveInput();
    	Optional<String> error = DataProvider.INSTANCE.createBook(id, eId, condition, title, author, genre, publishYear, description, true);
    	if(error.isPresent()) {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}else {
    		MessageDialog.openConfirm(parentShell, "Success!", "Book added successfuly");
    		ok();
    	}
         
    }
}

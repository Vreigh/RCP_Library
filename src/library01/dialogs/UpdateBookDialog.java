package library01.dialogs;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.IBook;
import library01.dataprovider.DataProvider;

public class UpdateBookDialog extends AddBookDialog{
	private IBook book = null;
	
	public UpdateBookDialog(Shell parentShell, IBook book) {
        super(parentShell);
        this.book = book;
    }

    @Override
    public void create() {
        super.create();
        setTitle("Update a book");
        setMessage("Update a chosen book");
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
    	Control area = super.createDialogArea(parent);
    	setInput();
    	return area;
    }
    
    private String nullToEmpty(String a) {
    	if(a == null) return "";
    	else return a;
    }
    
    private void setInput() {
    	idTxt.setText(book.getId());
    	eIdTxt.setText(book.getEId());
    	conditionCombo.select(book.getCondition());
    	titleTxt.setText(nullToEmpty(book.getTitle()));
    	authorTxt.setText(nullToEmpty(book.getAuthor()));
    	genreTxt.setText(nullToEmpty(book.getGenre()));
    	if(book.getPublishYear() != null) publishYearTxt.setText(String.valueOf(book.getPublishYear()));
    	book.getDescription();
    	descriptionTxt.setText(nullToEmpty(book.getDescription()));
    }
    
    @Override
    protected void okPressed() {
        saveInput();
    	Optional<String> error = DataProvider.INSTANCE.updateBook(book.getId(), id, eId, condition, title, author, genre, publishYear, description, null);
    	if(error.isPresent()) {
    		MessageDialog.openError(parentShell, "Invalid data", error.get());
    	}else {
    		MessageDialog.openConfirm(parentShell, "Success!", "Book updated successfully");
    		ok();
    	}
         
    }

}
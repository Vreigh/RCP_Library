package library01.edit;

import org.eclipse.jface.viewers.TableViewer;

import library01.dataprovider.DataProvider;
import library01.model.Book;
import library01.model.BookUpdateData;

public class PublishYearEditingSupport extends TitleEditingSupport{
	private final TableViewer viewer;

    public PublishYearEditingSupport(TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }
    
    @Override
    protected Object getValue(Object element) {
        return ((Book) element).getPublishYear().toString(); // w edycji wyswietlam -x. Mam gwarancje, ze to nie bedzie MIN_VALUE, 
        // bo do edycji roku wydania edycja musi byc ustawiona 
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    	Book book = (Book) element;
    	String tmp = String.valueOf(userInputValue);
    	Integer input = Integer.valueOf(tmp);
    	if(input > 2050 || input < -10000) {
    		System.out.println("Edycja niemozliwa");
    		return;
    	}
    	BookUpdateData update = new BookUpdateData(null, null, null, null, null, input, null);
    	
    	if(DataProvider.INSTANCE.updateBook(book.getId(), update)) {
    		book.update(update);
    		viewer.refresh(); // odswiez wszystko (po kilka rzedow moglo sie zmienic
    	}else {
    		System.out.println("Edycja niemozliwa");
    	}
    }
}

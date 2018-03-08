package library01.dataprovider;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import library01.dataprovider.book.BookProvider;
import library01.dataprovider.book.BookProviderMock;
import library01.dataprovider.book.BookProviderXML;
import library01.model.Book;
import library01.model.BookUpdateData;
import library01.parts.IndexView;
import library01.tasks.CheckerXMLTask;

import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;

public class DataProvider {
	public static DataProvider INSTANCE;
	
	public static void newInstance(Object arg1, Object arg2) {
		INSTANCE = new DataProvider(arg1, arg2);
	}
	
	private BookProvider bookProvider;
	
	private DataProvider(Object arg1, Object arg2) {
		try {
			// tutaj ladowanie configa i wybor odpowiedniego BookProvider-a
			File file = new File("eclipse-workspace/Library01/src/library01/data/data.xml");
			bookProvider = new BookProviderXML(file);
			CheckerXMLTask checker = new CheckerXMLTask((Shell)arg1, (IndexView)arg2, file);
			checker.start();
			//bookProvider = new BookProviderMock();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public List<Book> getBooks(){
		try {
			return bookProvider.getBooks();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	public Optional<Book> getBookById(String id){
		try {
			return bookProvider.getBookById(id);
		}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	public Optional<String> addNewBook(BookUpdateData data) {
		try {
			return bookProvider.addNewBook(data);
		}catch(Exception e) {
			return Optional.of(e.getMessage());
		}
	}
	public void deleteBook(String id) {
		try {
			bookProvider.deleteBook(id);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public Optional<String> updateBook(String id, BookUpdateData update) {
		try {
			return bookProvider.updateBook(id, update);
		}catch(Exception e) {
			return Optional.of(e.getMessage());
		}
	}
}

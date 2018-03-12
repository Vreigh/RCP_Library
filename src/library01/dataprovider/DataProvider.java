package library01.dataprovider;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import library01.bookapi.BookProvider;
import library01.bookapi.IBook;
import library01.dataprovider.book.BookProviderMock;
import library01.dataprovider.book.BookProviderXML;
import library01.model.Book;
import library01.model.BookUpdateData;
import library01.parts.IndexView;
import library01.setup.Setupper;
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
	
	private BookProvider bookProvider;
	
	public static void newInstance(Setupper setup) {
		INSTANCE = new DataProvider(setup);
	}
	
	private DataProvider(Setupper setup) {
		bookProvider = setup.getBookProvider();
	}
	
	public List<IBook> getBooks(){
		try {
			return bookProvider.getBooks();
		}catch(Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	public Optional<IBook> getBookById(String id){
		try {
			return bookProvider.getBookById(id);
		}catch(Throwable e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	public Optional<String> addNewBook(BookUpdateData data) {
		try {
			return bookProvider.addNewBook(data);
		}catch(Throwable e) {
			return Optional.of(e.getMessage());
		}
	}
	public void deleteBook(String id) {
		try {
			bookProvider.deleteBook(id);
		}catch(Throwable e) {
			e.printStackTrace();
		}
		
	}
	public Optional<String> updateBook(String id, BookUpdateData update) {
		try {
			return bookProvider.updateBook(id, update);
		}catch(Throwable e) {
			return Optional.of(e.getMessage());
		}
	}
}

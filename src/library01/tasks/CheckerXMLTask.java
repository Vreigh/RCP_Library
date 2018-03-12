package library01.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import library01.bookapi.IBook;
import library01.model.Book;
import library01.parts.IndexView;

public class CheckerXMLTask {
	private File file;
	private IndexView view;
	private Shell shell;
	
	public CheckerXMLTask(Shell shell, IndexView view, File file) {
		this.shell = shell;
		this.view = view;
		this.file = file;
	}
	
	public void start() {
		Job job = new Job("First Job") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
            	while(true) {
            		try {
            			Thread.sleep(3000);
            			List<IBook> viewBooks = (List<IBook>)view.getData();
            			if(viewBooks != null) {
            				List<String> changes = new ArrayList<String>();
            				synchronized(file) {
            					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        						Document doc = dBuilder.parse(file);
        						
        						XPathFactory xPathfactory = XPathFactory.newInstance();
        						XPath xpath = xPathfactory.newXPath();
        						for(IBook book : viewBooks) {
        							XPathExpression bookSearch = xpath.compile("/bookData/books/book[@id=\"" + book.getId() + "\"]");
        							NodeList matchingBooks = (NodeList)bookSearch.evaluate(doc, XPathConstants.NODESET);
        							if(matchingBooks.getLength() == 0) {
        								changes.add(book.getId());
        							}else if(matchingBooks.getLength() == 1) {
        								Element bookEl = (Element) matchingBooks.item(0);
        								if(book.getAvailable() != Boolean.valueOf(bookEl.getAttribute("available"))) {
        									changes.add(book.getId());
        								}
        							}else throw new Exception("XML format error - multiple id");
        						}
            				}
    						if(!changes.isEmpty()) {
    							String message = "Source file changed by other program. Following books affected: ";
    							for(String id : changes) {
    								message += id + "\n";
    							}
    							syncWithView(message);
    						}
            			}
                	}catch(Exception e) {
                		e.printStackTrace();
                		return Status.CANCEL_STATUS;
                	}
            	}
            }
        };
        job.setUser(true);
        job.schedule();
	}
	
	private void syncWithView(String warning) {
		Display.getDefault().asyncExec(new Runnable() {
            public void run() {
            	view.reload();
        		MessageDialog.openWarning(shell, "File changed!", warning);
            }
        });
		
	}
}

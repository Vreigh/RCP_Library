package library01.setup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.swt.widgets.Shell;

import library01.dataprovider.book.BookProvider;
import library01.dataprovider.book.BookProviderMock;
import library01.dataprovider.book.BookProviderXML;
import library01.parts.IndexView;
import library01.tasks.CheckerXMLTask;

public class ConfigFileSetupper implements Setupper {
	private BookProvider bookProvider;
	
	public ConfigFileSetupper(Shell shell, IndexView view) {
		String configPath = "eclipse-workspace/Library01/src/library01/data/config";
		
		try (Stream<String> stream = Files.lines(Paths.get(configPath))) {

			Optional<String> provider = stream.findFirst();
			if(!provider.isPresent()) throw new Exception("Wrong config file format");
			switch(provider.get()) {
				case "Mock":
					bookProvider = new BookProviderMock();
					break;
				case "XML":
					File xml = new File("eclipse-workspace/Library01/src/library01/data/data.xml");
					bookProvider = new BookProviderXML(xml);
					CheckerXMLTask checker = new CheckerXMLTask(shell, view, xml);
					checker.start();
					break;
				default:
					throw new Exception("Wrong config provider name");		
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Setup failed!");
			System.exit(2);
		}
	}
	
	public BookProvider getBookProvider() {
		return bookProvider;
	}
}
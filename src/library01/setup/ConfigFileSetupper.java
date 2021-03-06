package library01.setup;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import javax.naming.ConfigurationException;


import library01.bookapi.BookProvider;

public class ConfigFileSetupper implements Setupper {
	private BookProvider bookProvider;
	
	public ConfigFileSetupper() {
		String configPath = "eclipse-workspace/Library01/src/library01/data/config";
		
		try (Stream<String> stream = Files.lines(Paths.get(configPath))) {

			Optional<String> provider = stream.findFirst();
			if(!provider.isPresent()) throw new ConfigurationException("Wrong config file format");
			bookProvider = (BookProvider) Class.forName(provider.get()).newInstance();
			
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Error in configuration");
			System.exit(5);
		} 
	}
	
	public BookProvider getBookProvider() {
		return bookProvider;
	}
}
package library01.dataprovider;

public enum DataProvider {
	INSTANCE;
	
	private BookProvider bookProvider;
	
	private DataProvider() {
		// wybor odpowiedniego BookProvider-a
		bookProvider = BookProviderMock; 
	}

}

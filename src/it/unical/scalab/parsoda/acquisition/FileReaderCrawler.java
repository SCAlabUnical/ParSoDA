package it.unical.scalab.parsoda.acquisition;

public class FileReaderCrawler extends AbstractCrawlerFunction {
	
	public FileReaderCrawler() {
		super();
	}

	public FileReaderCrawler(String params) {
		super(params);
	}

	@Override
	public void collect() {
	}
	
	@Override
	public String getResultPath() {
		return this.options.getOption("i");	
	}

}

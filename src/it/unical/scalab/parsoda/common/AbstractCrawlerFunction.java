package it.unical.scalab.parsoda.common;

import org.apache.hadoop.fs.FileSystem;

import it.unical.scalab.parsoda.common.util.CommandOptions;

public abstract class AbstractCrawlerFunction implements CrawlerFunction, Linkable {
	
	protected CommandOptions options;
	protected String inputPath;
	protected String outputPath;
	protected String basePath;
	protected FileSystem fs;

	public AbstractCrawlerFunction() {
		this.options = new CommandOptions();
	}
	
	public AbstractCrawlerFunction(String options) {
		this.options = new CommandOptions(options);		
	}
	
	public abstract void collect();

	public CommandOptions getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = new CommandOptions(options);	
	}
	
	public String getResultPath(){
		return this.outputPath;
	}

	@Override
	public String getInputPath() {
		return this.inputPath;
	}

	@Override
	public String getOutputPath() {
		return this.outputPath;
	}

	@Override
	public String getBasePath() {
		return this.basePath;
	}

	@Override
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	@Override
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
	public FileSystem getFileSystem() {
		return fs;
	}

	public void setFileSystem(FileSystem fs) {
		this.fs = fs;
	}
	

	
	
}

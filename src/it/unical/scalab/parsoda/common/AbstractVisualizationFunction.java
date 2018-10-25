package it.unical.scalab.parsoda.common;

import org.apache.hadoop.fs.FileSystem;

import it.unical.scalab.parsoda.common.util.CommandOptions;


public abstract class AbstractVisualizationFunction implements VisualizationFunction,Linkable {
	
	protected String inputPath;
	protected String outputPath;
	protected String basePath;
	protected FileSystem fs;
	protected CommandOptions options;
	
	public AbstractVisualizationFunction() {
		this.options = new CommandOptions();		
	}
	
	public AbstractVisualizationFunction(String options) {
		this.options = new CommandOptions(options);		
	}
	
	public CommandOptions getOptions() {
		return options;
	}
	
	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getBasePath() {
		return this.basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public FileSystem getFileSystem() {
		return fs;
	}

	public void setFileSystem(FileSystem fs) {
		this.fs = fs;
	}
	
	

	
	
}

package it.unical.scalab.parsoda.common;

public interface Linkable {
	public String getInputPath();
	public String getOutputPath();
	public String getBasePath();
	public void setBasePath(String basePath);
	public void setInputPath(String input);
	public void setOutputPath(String output);	
}

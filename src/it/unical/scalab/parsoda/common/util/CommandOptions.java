package it.unical.scalab.parsoda.common.util;

import java.util.HashMap;
import java.util.Map;

public class CommandOptions {

	private String cliParams;
	private Map<String, String> options;
	private int counter = 0;
	
	public CommandOptions() {
		this.options = new HashMap<String, String>();
		cliParams = "";
	}

	public CommandOptions(String options) {
		this.cliParams = options;
		this.options = new HashMap<String, String>();
		String elems[] = options.split(" ");

		String tmpKey = null;
		for (String e : elems) {
			if (e.startsWith("-")) {
				// Key of an option
				tmpKey = e.substring(1);
			} else {
				if (tmpKey != null) {
					this.options.put(tmpKey, e);
					tmpKey = null;
				} else {
					// Option without key
					this.options.put(counter + "", e);
					counter++;
				}
			}
		}

	}
	
	public String getCliParams() {
		return cliParams;
	}
	
	public Double getDouble(String name){
		return Double.parseDouble(this.options.get(name));
	}
	public Integer getInteger(String name){
		return Integer.parseInt(this.options.get(name));
	}
	public Long getLong(String name){
		return Long.parseLong(this.options.get(name));
	}	
	public String getString(String name){
		return this.options.get(name);
	}
	public String getOption(String name){
		return this.options.get(name);
	}
	
	public Map<String, String> getOptions() {
		return options;
	}
	public int getCounter() {
		return counter;
	}
	
	public boolean exists(String name) {
		return this.options.containsKey(name);
	}

}

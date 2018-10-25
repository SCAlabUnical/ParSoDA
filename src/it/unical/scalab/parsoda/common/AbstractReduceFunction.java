package it.unical.scalab.parsoda.common;

import it.unical.scalab.parsoda.common.util.CommandOptions;


public abstract class AbstractReduceFunction implements ReduceFunction {

	protected CommandOptions options;

	public AbstractReduceFunction() {
		this.options = new CommandOptions();		
	}
	
	public AbstractReduceFunction(String options) {
		this.options = new CommandOptions(options);		
	}

}

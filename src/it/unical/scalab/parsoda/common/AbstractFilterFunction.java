package it.unical.scalab.parsoda.common;

import java.util.function.Predicate;

import it.unical.scalab.parsoda.common.model.SocialItem;
import it.unical.scalab.parsoda.common.util.CommandOptions;

public abstract class AbstractFilterFunction implements Predicate<SocialItem> {
	
	protected CommandOptions options;
	
	public AbstractFilterFunction() {
		this.options = new CommandOptions();		
	}
	
	public AbstractFilterFunction(String options) {
		this.options = new CommandOptions(options);		
	}

	public void setOptions(String options) {
		this.options = new CommandOptions(options);	
	}

}

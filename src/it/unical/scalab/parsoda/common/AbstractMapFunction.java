package it.unical.scalab.parsoda.common;

import java.util.function.Function;

import org.apache.hadoop.mapreduce.Mapper.Context;

import it.unical.scalab.parsoda.common.model.SocialItem;
import it.unical.scalab.parsoda.common.util.CommandOptions;


public abstract class AbstractMapFunction implements Function<SocialItem, SocialItem> {

	protected CommandOptions options;
	
	public AbstractMapFunction() {
		this.options = new CommandOptions();		
	}
	
	public AbstractMapFunction(String options) {
		this.options = new CommandOptions(options);		
	}
	
	public abstract SocialItem apply(SocialItem t);
	
	@SuppressWarnings("rawtypes")
	public Context context;

	@SuppressWarnings("rawtypes")
	public void setContext(Context context){
		this.context = context;
	}
}

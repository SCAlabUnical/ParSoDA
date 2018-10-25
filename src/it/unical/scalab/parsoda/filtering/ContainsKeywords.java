package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class ContainsKeywords extends AbstractFilterFunction {

	public ContainsKeywords() {
		super();
	}

	
	public ContainsKeywords(String options) {
		super(options);
	}

	public boolean test(AbstractGeotaggedItem g) {
		String item = g.toString();
		String separator = this.options.getOption("separator");
		for (String s : this.options.getOption("keywords").split(separator)) {
			if(item.contains(s)) return true;
		}
		return false;
	}	

}

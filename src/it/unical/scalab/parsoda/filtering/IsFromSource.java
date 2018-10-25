package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class IsFromSource extends AbstractFilterFunction {
	public IsFromSource() {
		super();
	}

	
	public IsFromSource(String params) {
		super(params);
	}

	@Override
	public boolean test(AbstractGeotaggedItem g) {
		return g.getSource().equals(this.options.getString("source"));
	}

}

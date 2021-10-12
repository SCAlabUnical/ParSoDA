package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class IsFromSource extends AbstractFilterFunction {
	public IsFromSource() {
		super();
	}

	
	public IsFromSource(String params) {
		super(params);
	}

	@Override
	public boolean test(SocialItem g) {
		return g.getSource().equals(this.options.getString("source"));
	}

}

package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class IsOfUser extends AbstractFilterFunction {
	public IsOfUser() {
		super();
	}
	
	public IsOfUser(String params) {
		super(params);
	}

	@Override
	public boolean test(AbstractGeotaggedItem g) {
		return this.options.getString("userid").equals(g.getUserId());
	}

}

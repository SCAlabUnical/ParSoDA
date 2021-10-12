package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class IsOfUser extends AbstractFilterFunction {
	public IsOfUser() {
		super();
	}
	
	public IsOfUser(String params) {
		super(params);
	}

	@Override
	public boolean test(SocialItem g) {
		return this.options.getString("userid").equals(g.getUserId());
	}

}

package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class IsGeotagged extends AbstractFilterFunction {
	
	public IsGeotagged() {
		super();
	}

	
	public boolean test(AbstractGeotaggedItem g) {
		if (g == null || g.getLocation() == null || (g.getLocation().getX() == 0 && g.getLocation().getY() == 0))
			return false;
		return true;
	}
}

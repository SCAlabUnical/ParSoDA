package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;
import it.unical.scalab.parsoda.common.model.Tweet;

public class IsNotRetweet extends AbstractFilterFunction {
	
	public IsNotRetweet() {
		super();
	}

	@Override
	public boolean test(AbstractGeotaggedItem g) {
		if(g instanceof Tweet){
			return (!g.getJson().has("retweeted_status"));
		}else{
			return true;
		}
	}

}

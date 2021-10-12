package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.SocialItem;
import it.unical.scalab.parsoda.common.model.TwitterSocialItem;

public class IsNotRetweet extends AbstractFilterFunction {
	
	public IsNotRetweet() {
		super();
	}

	@Override
	public boolean test(SocialItem g) {
		if(g instanceof TwitterSocialItem){
			return (!g.getJson().has("retweeted_status"));
		}else{
			return true;
		}
	}

}

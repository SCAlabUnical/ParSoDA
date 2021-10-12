package it.unical.scalab.parsoda.filtering;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class HasLang extends AbstractFilterFunction {

	public HasLang() {
		super();
	}

	
	public HasLang(String options) {
		super(options);
	}

	public boolean test(SocialItem g) {
		Object obj = g.search("lang");
		if(obj!=null && obj.toString().equals(this.options.getString("lang")))
			return true;
		return false;
	}

}

package it.unical.scalab.parsoda.mapping;

import it.unical.scalab.parsoda.common.AbstractMapFunction;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class RemoveExtraPayload extends AbstractMapFunction {
	
	public RemoveExtraPayload() {
		super();
	}

	public RemoveExtraPayload(String options) {
		super(options);
	}

	@Override
	public SocialItem apply(SocialItem g) {
		g.removeExtraPayload();
		return g;

	}

}

package it.unical.scalab.parsoda.mapping;

import it.unical.scalab.parsoda.common.AbstractMapFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class RemoveExtraPayload extends AbstractMapFunction {
	
	public RemoveExtraPayload() {
		super();
	}

	public RemoveExtraPayload(String options) {
		super(options);
	}

	@Override
	public AbstractGeotaggedItem apply(AbstractGeotaggedItem g) {
		g.removeExtraPayload();
		return g;

	}

}

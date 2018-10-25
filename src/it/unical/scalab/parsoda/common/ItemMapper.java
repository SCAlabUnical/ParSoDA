package it.unical.scalab.parsoda.common;

import java.util.Arrays;
import java.util.List;

import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class ItemMapper {
	private List<AbstractMapFunction> functions = null;

	public ItemMapper(AbstractMapFunction... functions) {
		this.functions = Arrays.asList(functions);
	}

	public ItemMapper(List<AbstractMapFunction> functions) {
		this.functions =  functions;
	}

	public AbstractGeotaggedItem map(AbstractGeotaggedItem i) {
		for (AbstractMapFunction mapFunction : functions) {
			i = mapFunction.apply(i);
		}
		return i;
	}

}

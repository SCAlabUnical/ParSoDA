package it.unical.scalab.parsoda.reduction;

import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.io.Text;

import com.spatial4j.core.shape.Point;

import it.unical.scalab.parsoda.common.AbstractReduceFunction;
import it.unical.scalab.parsoda.common.GeotaggedItemBuilder;
import it.unical.scalab.parsoda.common.TextTuple;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class ReduceByCoordinates extends AbstractReduceFunction {

	public ReduceByCoordinates() {
	}

	public ReduceByCoordinates(String params) {
		super(params);
	}

	public List<String> reduce(TextTuple key, Iterable<Text> items) {

		List<String> ret = new LinkedList<String>();	
		AbstractGeotaggedItem item = null;
		Point loc = null;
		for (Text text : items) {
			item = GeotaggedItemBuilder.create(text.toString());
			loc = item.getLocation();
			if(loc!=null)
				ret.add(loc.getX()+","+loc.getY());
		}

		return ret;
	}

}

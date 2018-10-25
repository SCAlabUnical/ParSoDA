package it.unical.scalab.parsoda.filtering;

import java.io.IOException;
import java.text.ParseException;

import com.spatial4j.core.exception.InvalidShapeException;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;
import it.unical.scalab.parsoda.common.util.GeoUtils;

public class IsInShape extends AbstractFilterFunction {

	private Shape shape = null;

	public IsInShape() {
		super();
	}
	
	public IsInShape(String params) throws InvalidShapeException, IOException, ParseException {
		super(params);
		shape = GeoUtils.getPolygon(params);
	}

	@Override
	public boolean test(AbstractGeotaggedItem g) {
		if (g!=null && g.getLocation() != null) {
			Point p = GeoUtils.getSpatialContext().makePoint(g.getLocation().getX(), g.getLocation().getY());
			if (GeoUtils.isContained(p, shape)) {
				return true;
			}
		}
		return false;
	}

}

package it.unical.scalab.parsoda.filtering;

import com.spatial4j.core.shape.Circle;
import com.spatial4j.core.shape.Point;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.SocialItem;
import it.unical.scalab.parsoda.common.util.GeoUtils;

public class IsInPlace extends AbstractFilterFunction {

	private Circle circle = null;

	public IsInPlace() {
		super();
	}

	
	public IsInPlace(String params) {
		super(params);
		Double lat = Double.parseDouble(this.options.getOption("lat"));
		Double lng = Double.parseDouble(this.options.getOption("lng"));
		Double radius = Double.parseDouble(this.options.getOption("radius"));
		circle = GeoUtils.getCircle((Point)GeoUtils.getPoint(lng, lat), radius);
	}

	@Override
	public boolean test(SocialItem g) {
		if (g!=null && g.getLocation() != null) {
			Point p = GeoUtils.getSpatialContext().makePoint(g.getLocation().getX(), g.getLocation().getY());
			if (GeoUtils.isContained(p, circle)) {
				return true;
			}
		}
		return false;
	}

}

package it.unical.scalab.parsoda.mapping;

import java.io.IOException;

import com.spatial4j.core.context.jts.JtsSpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Point;

import it.unical.scalab.parsoda.common.AbstractMapFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class ClassifyByGeoCell extends AbstractMapFunction {

	public static final double step1mY = 8.992909382672273E-6;
	public static final double step1mX = 1.2080663828690774E-5;
	public static JtsSpatialContext ctx = JtsSpatialContext.GEO;
	public final static double RAD = DistanceUtils.EARTH_MEAN_RADIUS_KM;
	public final static double SURFACE_AREA_ONE_SQUARE_DEGREE = 12365.1613;

	double xAxisStep = 0;
	double yAxisStep = 0;

	public ClassifyByGeoCell() {
		super();
	}

	public ClassifyByGeoCell(String options) throws IOException {
		super(options);
		if (this.options.exists("xStep")) {
			this.xAxisStep = this.options.getDouble("xStep");
		}
		if (this.options.exists("yStep")) {
			this.yAxisStep = this.options.getDouble("yStep");
		}
	}

	@Override
	public AbstractGeotaggedItem apply(AbstractGeotaggedItem g) {
		if (g.getLocation() != null) {
			Point l = g.getLocation();
			double xCell = calculateLongitude(l.getX());
			double yCell = calculateLatitude(l.getY());
			g.put(Metadata.GEO_CELL, xCell + "_" + yCell);
		}
		return g;
	}

	public double calculateLongitude(Double longitute) {
		double stepX = step1mX * this.xAxisStep;
		return ((int) (longitute / stepX) + 1) * stepX;

	}

	public double calculateLatitude(Double latitude) {
		double stepY = step1mY * yAxisStep;
		return ((int) (latitude / stepY) + 1) * stepY;

	}

}

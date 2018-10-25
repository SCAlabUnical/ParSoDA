package it.unical.scalab.parsoda.common.analysis;

import org.apache.commons.math3.ml.clustering.Clusterable;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.impl.PointImpl;


public class ClusterPoint extends PointImpl implements Clusterable {

	public ClusterPoint(double x, double y, SpatialContext ctx) {
		super(x, y, ctx);
	}

	@Override
	public double[] getPoint() {
		double[] p = { this.getX(), this.getY() };
		return p;
	}

	@Override
	public String toString() {
		return "" + this.getX() + "," + this.getY();
	}
}

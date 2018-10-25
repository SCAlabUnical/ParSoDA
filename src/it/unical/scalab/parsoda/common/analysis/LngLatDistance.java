package it.unical.scalab.parsoda.common.analysis;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import it.unical.scalab.parsoda.common.util.GeoUtils;

public class LngLatDistance implements DistanceMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Restituisce la distanza in metri tra due punti geografici
	 * 
	 * @see org.apache.commons.math3.ml.distance.DistanceMeasure#compute(double[], double[])
	 */
	@Override
	public double compute(double[] a, double[] b) throws DimensionMismatchException {
		return GeoUtils.distance(a,b);
	}

}

package it.unical.scalab.parsoda.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public class SortClockwiseCoordinate {

	private static Coordinate center;
	
	public static void sort(List<Coordinate> points) {
		center = getCenterPointOfPoints(points);
		Collections.sort(points, new Comparator<Coordinate>() {

			@Override
			public int compare(Coordinate a, Coordinate b) {
				if (a.x >= 0 && b.x < 0)
					return -1;
				if (a.x == 0 && b.x == 0)
					return a.y > b.y ? -1 : +1;
				double det = (a.x - center.x) * (b.y - center.y) - (b.x - center.x) * (a.y - center.y);
				if (det < 0)
					return -1;
				if (det > 0)
					return +1;
				double d1 = (a.x - center.x) * (a.x - center.x) + (a.y - center.y) * (a.y - center.y);
				double d2 = (b.x - center.x) * (b.x - center.x) + (b.y - center.y) * (b.y - center.y);
				return d1 > d2 ? -1 : +1;
			}

		});
		
	}
	

	public static void sort(Coordinate[] points) {
		center = getCenterPointOfPoints(points);
		ArrayList<Coordinate> pointsList = new ArrayList<Coordinate>();
		for (Coordinate geoPoint : points) {
			pointsList.add(geoPoint);
		}

		for (int i = 0; i < points.length; i++) {
			points[i] = pointsList.get(i);
		}
	}

	private static Coordinate getCenterPointOfPoints(Coordinate[] points) {
		Coordinate pointsSum = new Coordinate(0, 0);
		for (int i = 1; i < points.length; i++) {
			pointsSum = new Coordinate(pointsSum.x + points[i].x, pointsSum.y + points[i].y);
		}
		return new Coordinate(pointsSum.y / points.length, pointsSum.x / points.length);
	}

	private static Coordinate getCenterPointOfPoints(List<Coordinate> points) {
		Coordinate pointsSum = new Coordinate(0, 0);
		for (Coordinate p : points) {
			pointsSum = new Coordinate(pointsSum.x + p.x, pointsSum.y + p.y);
		}
		return new Coordinate(pointsSum.y / points.size(), pointsSum.x / points.size());
	}


}

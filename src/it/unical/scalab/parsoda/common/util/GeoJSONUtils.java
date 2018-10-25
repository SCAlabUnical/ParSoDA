package it.unical.scalab.parsoda.common.util;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Rectangle;
import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.shape.jts.JtsGeometry;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class GeoJSONUtils {

	private GeoJSONUtils() {
	}

	/**
	 * Serializes the given {@link Shape} as GeoJSON format into the given
	 * {@link XContentBuilder}
	 *
	 * @param shape
	 *            Shape that will be serialized
	 * @param builder
	 *            XContentBuilder it will be serialized to
	 * @throws IOException
	 *             Thrown if an error occurs while writing to the
	 *             XContentBuilder
	 */
	public static String serialize(Shape shape) throws IOException {
		if (shape instanceof JtsGeometry) {
			Geometry geometry = ((JtsGeometry) shape).getGeom();
			if (geometry instanceof Point) {
				return serializePoint((Point) geometry);
			} else if (geometry instanceof LineString) {
				return serializeLineString((LineString) geometry);
			} else if (geometry instanceof Polygon) {
				return serializePolygon((Polygon) geometry);
			} else if (geometry instanceof MultiPoint) {
				return serializeMultiPoint((MultiPoint) geometry);
			} else if (geometry instanceof MultiPolygon) {
				return serializeMultiPolygon((MultiPolygon) geometry);
			} else {
				throw new IllegalArgumentException("Geometry type [" + geometry.getGeometryType() + "] not supported");
			}
		} else if (shape instanceof Point) {
			return serializePoint((Point) shape);
		} else if (shape instanceof Rectangle) {
			return serializeRectangle((Rectangle) shape);
		} else {
			throw new IllegalArgumentException("Shape type [" + shape.getClass().getSimpleName() + "] not supported");
		}
	}

	private static String serializePoint(Point geometry) {

		double[] points = new double[2];
		points[0] = geometry.getY();
		points[1] = geometry.getX();

		JSONObject obj_geometry = new JSONObject();
		obj_geometry.put("coordinates", points);
		obj_geometry.put("type", "Point");

		JSONObject obj = new JSONObject().put("type", "Feature").put("geometry", obj_geometry);
		return obj.toString();

	}

	private static String serializeLineString(LineString geometry) {

		double[][] coordinates = getCoordinateMatrix(geometry);
		JSONObject obj_geometry = new JSONObject();
		obj_geometry.put("coordinates", coordinates);
		obj_geometry.put("type", "LineString");

		JSONObject obj = new JSONObject().put("type", "Feature").put("geometry", obj_geometry);

		return obj.toString();
	}


	private static String serializePolygon(Polygon geometry) {

		double[][][] coordinates = getCoordinateMatrixPolygon(geometry);

		JSONObject obj_geometry = new JSONObject();

		obj_geometry.put("coordinates", coordinates);
		obj_geometry.put("type", "Polygon");

		JSONObject obj = new JSONObject().put("type", "Feature").put("geometry", obj_geometry);

		return obj.toString();
	}

	private static String serializeMultiPoint(MultiPoint geometry) {
		/* TODO */
		return null;
	}

	private static String serializeMultiPolygon(MultiPolygon geometry) {
		/* TODO */
		return null;
	}

	private static String serializeRectangle(Rectangle rectangle) {

		double[][] points = new double[4][2];

		points[0][0] = rectangle.getMinX();
		points[0][1] = rectangle.getMinY();
		points[1][0] = rectangle.getMaxX();
		points[1][1] = rectangle.getMaxY();
		points[2][0] = rectangle.getMinX();
		points[2][1] = rectangle.getMaxY();
		points[3][0] = rectangle.getMaxX();
		points[3][1] = rectangle.getMinY();

		JSONObject obj_geometry = new JSONObject();
		obj_geometry.put("coordinates", points);
		obj_geometry.put("type", "Polygon");

		JSONObject obj = new JSONObject().put("type", "Feature").put("geometry", obj_geometry);
		return obj.toString();

	}

	public static double[][][] getCoordinateMatrixPolygon(Geometry geometry) {
		Coordinate[] points = geometry.getCoordinates();

		List<Coordinate> pointsList = new LinkedList<Coordinate>();
		double sumLat = 0;
		double sumLng = 0;
		for (Coordinate coordinate : points) {
			pointsList.add(coordinate);
			sumLat += coordinate.y;
			sumLng += coordinate.x;
		}

		Coordinate reference = new Coordinate(sumLng / points.length, sumLat / points.length);

		Collections.sort(pointsList, new ClockwiseCoordinateComparator(reference));

		Coordinate tmp = pointsList.remove(0);
		pointsList.add(tmp);

		// Create the coordinate's matrix
		double[][][] coordinates = new double[1][points.length][2];
		for (int i = 0; i < points.length; i++) {
			tmp = pointsList.get(i);
			coordinates[0][i][0] = tmp.x;
			coordinates[0][i][1] = tmp.y;
		}

		return coordinates;
	}

	public static double[][] getCoordinateMatrix(Geometry geometry) {
		Coordinate[] points = geometry.getCoordinates();

		List<Coordinate> pointsList = new LinkedList<Coordinate>();
		double sumLat = 0;
		double sumLng = 0;
		for (Coordinate coordinate : points) {
			pointsList.add(coordinate);
			sumLat += coordinate.y;
			sumLng += coordinate.x;
		}

		Coordinate reference = new Coordinate(sumLng / points.length, sumLat / points.length);
		Collections.sort(pointsList, new ClockwiseCoordinateComparator(reference));
		Coordinate tmp = pointsList.remove(points.length - 1);
		pointsList.add(0, tmp);

		// Create the coordinate's matrix
		double[][] coordinates = new double[points.length][2];
		for (int i = 0; i < points.length; i++) {
			tmp = pointsList.get(i);
			coordinates[i][0] = tmp.x;
			coordinates[i][1] = tmp.y;
		}

		return coordinates;
	}

}
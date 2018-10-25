package it.unical.scalab.parsoda.visualization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import com.spatial4j.core.exception.InvalidShapeException;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;

import it.unical.scalab.parsoda.common.AbstractVisualizationFunction;
import it.unical.scalab.parsoda.common.util.GeoUtils;
import it.unical.scalab.parsoda.common.util.KMLUtils;

public class RoIsToKML extends AbstractVisualizationFunction {

	public RoIsToKML() {
		super();
	}

	public RoIsToKML(String options) {
		super(options);
	}

	public void visualize() {

		try {
			OutputStream os = fs.create(new Path(this.getOutputPath() + "/RoIs.kml"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			Path inputPath = new Path(this.getInputPath());
			Map<Integer, Map<String, Integer>> results = new TreeMap<Integer, Map<String, Integer>>();
			Shape shape = null;
			HashSet<Point> points = null;
			String shapeString = null;
			
			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://www.opengis.net/kml/2.2\"><Document>");
			bw.newLine();
			if (fs.isDirectory(inputPath)) {
				RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(inputPath, true);
				while (fileStatusListIterator.hasNext()) {
					LocatedFileStatus fileStatus = fileStatusListIterator.next();
					BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
					String line;
					while ((line = br.readLine()) != null) {
						String[] coords = line.split(" ");
						points = new HashSet<Point>();
						for (String coord : coords) {
							points.add((Point) GeoUtils.getPoint(coord, ","));	
						}

						shape = GeoUtils.convexHull(points);
						if(shape!=null) {
							shapeString = KMLUtils.serialize(shape);
							bw.write((shapeString));
							bw.newLine();
						}
					}

					br.close();
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(inputPath)));
				String line;
				while ((line = br.readLine()) != null) {
					String[] coords = line.split(" ");
					points = new HashSet<Point>();
					for (String coord : coords) {
						points.add((Point) GeoUtils.getPoint(coord, ","));	
					}
					shape = GeoUtils.convexHull(points);
					if(shape!=null) {
						shapeString = KMLUtils.serialize(shape);
						bw.write((shapeString));
						bw.newLine();
					}
				}
				br.close();
			}
			bw.write("</Document></kml>");
			bw.close();
			fs.close();
			
		} catch (IOException | InvalidShapeException e) {
			e.printStackTrace();
		}

	}

}

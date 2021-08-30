package it.unical.scalab.parsoda.analysis;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import it.unical.scalab.parsoda.common.AbstractAnalysisFunction;
import it.unical.scalab.parsoda.common.analysis.ClusterPoint;
import it.unical.scalab.parsoda.common.analysis.DBSCANRoI;
import it.unical.scalab.parsoda.common.util.GeoUtils;

public class ExtractRoIs extends AbstractAnalysisFunction {

	HashSet<ClusterPoint> points = new HashSet<>();

	public ExtractRoIs() {
		super();
	}

	public ExtractRoIs(String params) {
		super(params);
	}

	@Override
	public String getResultPath() {
		return this.getOutputPath();
	}

	@Override
	public void prepareData() {
		String[] coords = null;
		try {
			Path inputPath = new Path(this.getInputPath());
			if (fs.isDirectory(inputPath)) {
				RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(inputPath, true);
				while (fileStatusListIterator.hasNext()) {
					LocatedFileStatus fileStatus = fileStatusListIterator.next();
					BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
					String line;
					line = br.readLine();
					while (line != null) {
						coords = line.split(",");
						points.add(new ClusterPoint(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]),
								GeoUtils.getSpatialContext()));
						line = br.readLine();
					}
					br.close();
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(inputPath)));
				String line;
				line = br.readLine();
				while (line != null) {
					coords = line.split(",");
					if (coords.length == 2) {
						points.add(new ClusterPoint(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]),
								GeoUtils.getSpatialContext()));
						line = br.readLine();
					}
				}
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void analyzeData() {
		OutputStream os;
		try {

			os = fs.create(new Path(this.getOutputPath() + "/RoIs.txt"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			
			List<Cluster<ClusterPoint>> clusters = new LinkedList<Cluster<ClusterPoint>>();
			int DBSCAN_MIN_PTS = this.getOptions().getInteger("minPts");
			double DBSCAN_EPSILON = this.getOptions().getDouble("eps");
			DBSCANRoI<ClusterPoint> dbscan;

			dbscan = new DBSCANRoI<ClusterPoint>(DBSCAN_EPSILON, DBSCAN_MIN_PTS - 1);

			clusters = dbscan.cluster(points);
			List<ClusterPoint> pp = null;
			String[] pps = null;
			for (Cluster<ClusterPoint> cluster : clusters) {
				pp = cluster.getPoints();
				pps = new String[pp.size()];
				int i = 0;
				for (ClusterPoint p : pp) {
					pps[i] = p.toString();
					i++;
				}
				bw.write(String.join(" ", pps));
				bw.newLine();
			}
			bw.close();
			fs.close();

		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}

	}

}

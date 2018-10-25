package it.unical.scalab.parsoda.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import it.unical.scalab.parsoda.common.AbstractAnalysisFunction;

public class NFactionsPolarization extends AbstractAnalysisFunction {

	String outputFileName = "/NFactionsPolarization.txt";
	double threshold = 0.9;
	HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();

	public NFactionsPolarization() {
		super();
	}

	public NFactionsPolarization(String options) {
		super(options);
		if (this.options.exists("th")) {
			this.threshold = this.options.getDouble("th");
		}
	}

	@Override
	public void prepareData() {
		try {
			Path inputPath = new Path(this.getInputPath());
			String[] parts = null;
			double pol = 0.0;
			int labelPosition = 0;
			if (fs.isDirectory(inputPath)) {
				RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(inputPath, true);
				while (fileStatusListIterator.hasNext()) {
					LocatedFileStatus fileStatus = fileStatusListIterator.next();
					BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
					String line;
					line = br.readLine();
					while (line != null) {
						parts = line.split("\t");
						pol = Double.parseDouble(parts[1]);
						if (pol < threshold && pol > (-1.0 * threshold)) {
							labelPosition = 0;
						} else if (pol >= threshold) {
							labelPosition = 1;
						} else {
							labelPosition = 2;
						}

						if (counter.containsKey(labelPosition)) {
							counter.put(labelPosition, counter.get(labelPosition) + 1);
						} else {
							counter.put(labelPosition, 1);
						}

						line = br.readLine();
					}
					br.close();
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(inputPath)));
				String line;
				line = br.readLine();
				while (line != null) {
					parts = line.split("\t");
					pol = Double.parseDouble(parts[1]);
					if (pol < threshold && pol > (-1.0 * threshold)) {
						labelPosition = 0;
					} else if (pol >= threshold) {
						labelPosition = 1;
					} else {
						labelPosition = 2;
					}

					if (counter.containsKey(labelPosition)) {
						counter.put(labelPosition, counter.get(labelPosition) + 1);
					} else {
						counter.put(labelPosition, 1);
					}

					line = br.readLine();
				}
				br.close();
			}
			fs.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void analyzeData() {
		OutputStream os;
		try {
			os = fs.create(new Path(this.getOutputPath() + outputFileName));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			int tot = 0;
			for (Entry<Integer, Integer> e : counter.entrySet()) {
				tot+=e.getValue();
			}
			for (Entry<Integer, Integer> e : counter.entrySet()) {
				bw.write("Items for class "+e.getKey()+":\t"+e.getValue()+"\t"+(1.0*e.getValue())/(1.0*tot));
				bw.newLine();
			}
			bw.close();
			fs.close();
			
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}

	}

}

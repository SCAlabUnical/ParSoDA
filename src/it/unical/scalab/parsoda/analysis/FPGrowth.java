package it.unical.scalab.parsoda.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import it.unical.scalab.parsoda.common.AbstractAnalysisFunction;


public class FPGrowth extends AbstractAnalysisFunction {

	public FPGrowth() {
		super();
	}

	public FPGrowth(String params) {
		super(params);
	}
	
	@Override
	public String getResultPath() {
		return this.getOutputPath();
	}
	
	@Override
	public void prepareData() {

		try {
			// Prepare data for FP-GROWTH
			Path inputPath = new Path(this.getInputPath());
			OutputStream os = fs.create(new Path(this.getOutputPath() + "/itemsetFIS.txt"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			if (fs.isDirectory(inputPath)) {
				RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(inputPath, true);
				while (fileStatusListIterator.hasNext()) {
					LocatedFileStatus fileStatus = fileStatusListIterator.next();
					BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
					String line;
					line = br.readLine();
					while (line != null) {
						bw.write(prepare(line));
						bw.newLine();
						line = br.readLine();
					}
					br.close();
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(inputPath)));
				String line;
				line = br.readLine();
				while (line != null) {
					bw.write(prepare(line));
					bw.newLine();
					line = br.readLine();
				}
				br.close();
			}
			bw.close();
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void analyzeData() {

		String fileOutputResult = this.getResultPath() + "/FIS-results.txt";

		int minSupport = 1;
		int minLevel = this.options.getInteger("minLevel");

		it.unical.scalab.parsoda.common.analysis.FPGrowth alg = new it.unical.scalab.parsoda.common.analysis.FPGrowth();
		alg.setMinLevel(minLevel);
		alg.setMinSupport(minSupport);

		double minSupportPerc = this.options.getDouble("minSupport");

		try {
			alg.runAlgorithm(this.getOutputPath() + "/itemsetFIS.txt", fileOutputResult, minSupportPerc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String prepare(String line) {

		line = line.replace(",", " ").replace("'", "");

		String[] loc = line.split(" ");
		Set<String> dataLine = new HashSet<String>();

		for (String l : loc) {
			dataLine.add(l);
		}

		return StringUtils.join(dataLine, " ");
	}

}

package it.unical.scalab.parsoda.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.util.ToolRunner;

import de.mpii.fsm.driver.FsmDriver;
import it.unical.scalab.parsoda.common.AbstractAnalysisFunction;

public class PrefixSpan extends AbstractAnalysisFunction {

	private static int i = 0;
	

	public PrefixSpan() {
		super();
	}

	public PrefixSpan(String params) {
		super(params);
	}
	
	@Override
	public String getResultPath() {
		return this.getOutputPath() + "/FSM-results";
	}
	

	@Override
	public void prepareData() {

		try {
			Path inputPath = new Path(this.getInputPath());
			OutputStream os = fs.create(new Path(this.getOutputPath() + "/itemsetFSM.txt"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			if (fs.isDirectory(inputPath)) {
				RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(inputPath, true);
				while (fileStatusListIterator.hasNext()) {
					LocatedFileStatus fileStatus = fileStatusListIterator.next();
					BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
					String line;
					line = br.readLine();
					while (line != null) {
						bw.write("s" + i + " " + line.replace(",", " ").replace("'", ""));
						bw.newLine();
						i++;
						line = br.readLine();
					}
					br.close();
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(inputPath)));
				String line;
				line = br.readLine();
				while (line != null) {
					bw.write("s" + i + " " + line.replace(",", " ").replace("'", ""));
					bw.newLine();
					i++;
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
		try {
			String fullParams = "-i " + this.getOutputPath() + "/itemsetFSM.txt";
			fullParams += " -o " + this.getOutputPath() + "/FSM-results -l "
					+ this.options.getInteger("maxPatternLength");
			fullParams += " -s " + Math.max(1, this.options.getDouble("minSupport").intValue());
			ToolRunner.run(new Configuration(), new FsmDriver(), fullParams.split(" "));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

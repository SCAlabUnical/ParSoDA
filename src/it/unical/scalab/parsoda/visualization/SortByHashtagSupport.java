package it.unical.scalab.parsoda.visualization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import it.unical.scalab.parsoda.common.AbstractVisualizationFunction;

public class SortByHashtagSupport extends AbstractVisualizationFunction {

	public SortByHashtagSupport() {
		super();
	}

	public SortByHashtagSupport(String options) {
		super(options);
	}

	public void visualize() {

		try {
			Path inputPath = new Path(this.getInputPath());
			Map<Integer, Map<String, Integer>> results = new TreeMap<Integer, Map<String, Integer>>();
			int len = 0;
			if (fs.isDirectory(inputPath)) {
				RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(inputPath, true);
				while (fileStatusListIterator.hasNext()) {
					LocatedFileStatus fileStatus = fileStatusListIterator.next();
					BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
					String line;
					while ((line = br.readLine()) != null) {
						int indexValue = line.indexOf('\t');
						len = line.substring(0, indexValue).trim().split(" ").length;

						if (!results.containsKey(len)) {
							results.put(len, new HashMap<String, Integer>());
						}
						results.get(len).put(line.substring(0, indexValue).trim(),
								Integer.parseInt(line.substring(indexValue).trim()));
					}

					br.close();
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(inputPath)));
				String line;
				while ((line = br.readLine()) != null) {
					int indexValue = line.indexOf(' ');
					len = line.substring(indexValue).trim().split(" ").length;

					if (!results.containsKey(len)) {
						results.put(len, new HashMap<String, Integer>());
					}
					results.get(len).put(line.substring(indexValue).trim(),
							Integer.parseInt(line.substring(0, indexValue)));
				}
				br.close();
			}

			switch (this.options.getString("k")) {
			case "support":
				sortBySupport(results, this.options.getString("d"));
				break;
			case "length":
				sortByLength(results, this.options.getString("d"));
				break;
			default:
				sortByLength(results, this.options.getString("d"));
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sortByLength(Map<Integer, Map<String, Integer>> results, String order)
			throws IllegalArgumentException, IOException {
		OutputStream os = fs.create(new Path(this.getOutputPath() + "/visualization-result.txt"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		SortedSet<Entry<Integer, Map<String, Integer>>> dataSorted = entriesSortedByLength(results, order);
		for (Entry<Integer, Map<String, Integer>> entry : dataSorted) {
			bw.write("\n*** SEQUENTIAL PATTERNS OF LENGTH " + entry.getKey());
			bw.newLine();
			for (Entry<String, Integer> list : entry.getValue().entrySet()) {
				bw.write(list.getKey() + " --> " + list.getValue());
				bw.newLine();
			}
			bw.newLine();
		}
		bw.close();
		fs.close();
	}

	private void sortBySupport(Map<Integer, Map<String, Integer>> results, String order)
			throws IllegalArgumentException, IOException {
		OutputStream os = fs.create(new Path(this.getOutputPath() + "/visualization-result.txt"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		int lastLen = 0;
		for (Entry<Integer, Map<String, Integer>> data : results.entrySet()) {
			SortedSet<Map.Entry<String, Integer>> dataSorted = entriesSortedBySupport(data.getValue(), order);
			for (Entry<String, Integer> entry : dataSorted) {
				if (lastLen < entry.getKey().trim().split(" ").length) {
					lastLen = entry.getKey().trim().split(" ").length;
					bw.write("\n*** SEQUENTIAL PATTERNS OF LENGTH " + lastLen);
					bw.newLine();
				}
				bw.write(entry.getKey() + " --> " + entry.getValue());
				bw.newLine();
			}

		}
		bw.close();
		fs.close();
	}

	private <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedBySupport(Map<K, V> map,
			String order) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = 0;
				if (order.equalsIgnoreCase("DESC")) {
					res = -e1.getValue().compareTo(e2.getValue());
				} else {
					res = e1.getValue().compareTo(e2.getValue());
				}
				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	private SortedSet<Map.Entry<Integer, Map<String, Integer>>> entriesSortedByLength(
			Map<Integer, Map<String, Integer>> results, String order) {
		SortedSet<Map.Entry<Integer, Map<String, Integer>>> sortedEntries = new TreeSet<Map.Entry<Integer, Map<String, Integer>>>(
				new Comparator<Map.Entry<Integer, Map<String, Integer>>>() {
					@Override
					public int compare(Map.Entry<Integer, Map<String, Integer>> e1,
							Map.Entry<Integer, Map<String, Integer>> e2) {
						int res = 0;
						if (order.equalsIgnoreCase("DESC")) {
							res = -e1.getKey().compareTo(e2.getKey());
						} else {
							res = e1.getKey().compareTo(e2.getKey());
						}
						return res != 0 ? res : 1;
					}
				});
		sortedEntries.addAll(results.entrySet());
		return sortedEntries;
	}

}

package it.unical.scalab.parsoda.common.analysis;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth_with_strings.AlgoFPGrowth_Strings;
import it.unical.scalab.parsoda.common.IntegerComparator;

public class FPGrowth extends AlgoFPGrowth_Strings {

	static Random randomGenerator = new Random();
	private int maxSupport = Integer.MAX_VALUE;
	private int minSupport = 1;
	private int minLevel = 1;
	private int maxLevel = Integer.MAX_VALUE;

	public FPGrowth(int maxSupport, int minSupport, int minLevel, int maxLevel) {
		super();
		this.maxSupport = maxSupport;
		this.minSupport = minSupport;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}

	public FPGrowth() {
	}


	public void setMaxSupport(int maxSupport) {
		this.maxSupport = maxSupport;
	}

	public void setMinSupport(int minSupport) {
		this.minSupport = minSupport;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	
	
	
	@Override
	public void runAlgorithm(String input, String output, double minsupp) throws FileNotFoundException, IOException {
		super.runAlgorithm(input, output, minsupp);
		sortAndFilterResults(output);
	}

	private void sortAndFilterResults(String fileOutputResult) throws IOException {

		TreeMap<Integer, TreeMap<Integer, List<String>>> allResults = new TreeMap<Integer, TreeMap<Integer, List<String>>>(new IntegerComparator());

		try (BufferedReader br = new BufferedReader(new FileReader(fileOutputResult))) {
			String line;
			while ((line = br.readLine()) != null) {

				int pos = line.lastIndexOf(":");
				Integer support = Integer.parseInt(line.substring(pos + 1));

				if (support.intValue() >= minSupport && support.intValue() <= maxSupport) {
					String value = line.substring(0, pos);

					String[] items = value.split(" ");
					int level = items.length;

					if (level >= minLevel && level <= maxLevel) {
						value = "";
						for (int i = 0; i < items.length; i++) {
							value += "[" + items[i] + "]";
						}

						if (!allResults.containsKey(level)) {
							// Create item list
							List<String> list = new LinkedList<String>();
							list.add(value);
							// Create support map
							TreeMap<Integer, List<String>> resultsBySupport = new TreeMap<Integer, List<String>>(new IntegerComparator());
							resultsBySupport.put(support, list);
							// Create level map
							allResults.put(level, resultsBySupport);

						} else {
							// Level already exists.. looks for support
							if (allResults.get(level).containsKey(support)) {
								allResults.get(level).get(support).add(value);
							} else {
								// Create item list
								List<String> list = new LinkedList<String>();
								list.add(value);
								// Create level treemap
								allResults.get(level).put(support, list);
							}
						}
					}

				}
			}
		}

		PrintWriter writer = new PrintWriter(new File(fileOutputResult));
		for (Entry<Integer, TreeMap<Integer, List<String>>> resultsByLevel : allResults.entrySet()) {

			writer.println("*** ITEMSET FOR LEVEL " + resultsByLevel.getKey() + " ***");

			for (Entry<Integer, List<String>> resultsForSupport : resultsByLevel.getValue().entrySet()) {
				Integer support = resultsForSupport.getKey();

				List<String> values = resultsForSupport.getValue();
				for (String value : values) {
					writer.println(value + " : (Abs: " + support + ")");
				}

			}
			writer.println();
		}
		writer.close();

	}


}

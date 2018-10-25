package it.unical.scalab.parsoda.reduction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.json.JSONObject;

import it.unical.scalab.parsoda.common.AbstractReduceFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.TextTuple;

public class ReduceByNFactionsPolarization extends AbstractReduceFunction {

	int threshold = 5;

	public ReduceByNFactionsPolarization() {
	}

	public ReduceByNFactionsPolarization(String params) {
		super(params);
		if (this.options.exists("t")) {
			this.threshold = this.options.getInteger("t");
		}
	}

	public List<String> reduce(TextTuple key, Iterable<Text> items) {
		HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();

		int labelPosition = 0;
		JSONObject item = null;
		int global_counter = 0;
		for (Text text : items) {
			item = new JSONObject(text.toString());
			if (item.has(Metadata.LABEL_CLASS)) {
				labelPosition = item.getInt(Metadata.LABEL_CLASS);
				if (counter.containsKey(labelPosition)) {
					counter.put(labelPosition, counter.get(labelPosition) + 1);
				} else {
					counter.put(labelPosition, 1);
				}
				global_counter++;
			}
		}

		List<String> ret = new ArrayList<String>(1);

		if (global_counter < threshold)
			return ret;

		int maxLabel = 0;
		int tmpMax = -1;
		for (Entry<Integer, Integer> e : counter.entrySet()) {
			if (e.getValue() > tmpMax) {
				tmpMax = e.getValue();
				maxLabel = e.getKey();
			}
		}

		ret.add("POL\t" + maxLabel);
		return ret;
	}

}

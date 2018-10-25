package it.unical.scalab.parsoda.reduction;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.json.JSONObject;

import it.unical.scalab.parsoda.common.AbstractReduceFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.TextTuple;

public class ReduceByAveragePolarization extends AbstractReduceFunction {

	private Integer threshold = 100;

	public ReduceByAveragePolarization() {
	}

	public ReduceByAveragePolarization(String params) {
		super(params);
		if (this.options.exists("t")) {
			this.threshold = this.options.getInteger("t");
		}
	}

	public List<String> reduce(TextTuple key, Iterable<Text> items) {

		JSONObject item = null;
		int global_counter = 0;
		int sum = 0;
		for (Text text : items) {
			item = new JSONObject(text.toString());
			if (item.has(Metadata.LABEL_CLASS)) {
				sum += item.getInt(Metadata.LABEL_CLASS);
				global_counter++;
			}
		}

		List<String> ret = new ArrayList<String>(1);

		if (global_counter < threshold)
			return ret;

		double mean = 0.0;
		if (global_counter > 0)
			mean = (1.0 * sum) / (1.0 * global_counter);
		ret.add("AVERAGE_POLARITY for " + key.left + " \t" + mean + "\t" + global_counter);
		return ret;
	}

}

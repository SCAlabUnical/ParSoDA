package it.unical.scalab.parsoda.reduction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.json.JSONObject;

import it.unical.scalab.parsoda.common.AbstractReduceFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.TextTuple;

public class ReduceByTwoFactionsPolarization extends AbstractReduceFunction {

	int REF_LABEL_INDEX = 1;
	int NEUTRAL_LABEL_INDEX = 0;
	int threshold = 5;

	public ReduceByTwoFactionsPolarization() {
	}

	public ReduceByTwoFactionsPolarization(String params) {
		super(params);
		if(this.options.exists("nIndex")) {
			this.NEUTRAL_LABEL_INDEX = this.options.getInteger("nIndex");
		}
		if(this.options.exists("refIndex")) {
			this.REF_LABEL_INDEX = this.options.getInteger("refIndex");
		}
		if(this.options.exists("t")) {
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
				if (labelPosition > NEUTRAL_LABEL_INDEX) {
					if (counter.containsKey(labelPosition)) {
						counter.put(labelPosition, counter.get(labelPosition) + 1);
					} else {
						counter.put(labelPosition, 1);
					}
					global_counter++;
				}
			}
		}
		


		List<String> ret = new ArrayList<String>(1);
		
		if(global_counter<threshold)
			return ret;

		int ref_class_counter = 0;
		if (counter.containsKey(REF_LABEL_INDEX)) {
			ref_class_counter = counter.get(REF_LABEL_INDEX);
		}
		double pol = 0.0;
		if (global_counter > 0)
			pol = (2.0 * ref_class_counter) / (1.0 * global_counter) - 1;
		ret.add("POL\t" + pol);
		return ret;
	}

}

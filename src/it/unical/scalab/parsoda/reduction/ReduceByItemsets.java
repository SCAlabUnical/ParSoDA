package it.unical.scalab.parsoda.reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.io.Text;

import it.unical.scalab.parsoda.common.AbstractReduceFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.TextTuple;
import it.unical.scalab.parsoda.common.util.DataUtils;

public class ReduceByItemsets extends AbstractReduceFunction {
	
	public ReduceByItemsets() {}
	
	public ReduceByItemsets(String params) {
		super(params);
	}

	public List<String> reduce(TextTuple key, Iterable<Text> items) {
		Set<String> set =  DataUtils.splitDataByTimeSet(items, Metadata.LOCATION_NAME, "day", options.getInteger("t"));
		List<String> ret = new ArrayList<String>();
		ret.addAll(set);
		return ret;
	}

}

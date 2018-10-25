package it.unical.scalab.parsoda.reduction;

import java.util.List;

import org.apache.hadoop.io.Text;

import it.unical.scalab.parsoda.common.AbstractReduceFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.TextTuple;
import it.unical.scalab.parsoda.common.util.DataUtils;

public class ReduceByTrajectories extends AbstractReduceFunction {
	public ReduceByTrajectories() {}
	public ReduceByTrajectories(String params) { super(params); }
	public List<String> reduce(TextTuple key, Iterable<Text> items) {
		return DataUtils.splitDataByTime(items, Metadata.LOCATION_NAME, "day", options.getInteger("t"));
	}
}
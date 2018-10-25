package it.unical.scalab.parsoda.common;
import java.util.List;

import org.apache.hadoop.io.Text;

public interface ReduceFunction {
	public List<String> reduce(TextTuple key, Iterable<Text> items);
}

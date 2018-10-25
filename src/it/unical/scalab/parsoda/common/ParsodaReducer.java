package it.unical.scalab.parsoda.common;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ParsodaReducer extends Reducer<TextTuple, Text, NullWritable, Text> {

	private ReduceFunction reducer = null;
	private Text outputValue = new Text();

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		String reduceFunction = context.getConfiguration().get("reduceFunction");
		String reduceParams = context.getConfiguration().get("reduceParams");
		initReducer(reduceFunction, reduceParams);
		super.setup(context);
	}

	@SuppressWarnings("unchecked")
	private void initReducer(String reduceFunction, String reduceParams) {
		if (reduceFunction != null && !reduceFunction.equals("")) {
			try {

				Class<ReduceFunction> _tempClass = (Class<ReduceFunction>) Class.forName(reduceFunction);
				if (reduceParams.length() > 0) {
					Constructor<ReduceFunction> ctor = _tempClass.getConstructor(String.class);
					reducer = (ReduceFunction) ctor.newInstance(reduceParams);
				} else {
					Constructor<ReduceFunction> ctor = _tempClass.getConstructor();
					reducer = ctor.newInstance();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reduce(TextTuple key, Iterable<Text> values, Context context)
			throws java.io.IOException, InterruptedException {

		if (reducer == null) {
			for (Text value : values) {
				context.write(NullWritable.get(), value);
			}
		} else {
			Iterable<String> aggregatedData = reducer.reduce(key, values);

			for (String data : aggregatedData) {
				outputValue.set(data);
				context.write(NullWritable.get(), outputValue);
			}

		}
	}

}
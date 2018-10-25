package it.unical.scalab.parsoda.common;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import it.unical.scalab.parsoda.common.util.CommandOptions;

public class MultiKeysMapperWriter implements MapperWriter {

	private CommandOptions options;

	public MultiKeysMapperWriter() {
		this.options = new CommandOptions();
	}

	public MultiKeysMapperWriter(String options) {
		this.options = new CommandOptions(options);
	}

	@SuppressWarnings("unchecked")
	public void write(Context context, TextTuple outputKey, Text outputValue) throws IOException, InterruptedException {
		TextTuple tmp = new TextTuple(outputKey.left, outputKey.right);
		Object json = new JSONTokener(outputKey.left.toString()).nextValue();
		if (json instanceof JSONObject) {
			tmp.left.set(json.toString());
			context.write(tmp, outputValue);
		} else if (json instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) json;
			for (int i = 0; i < jsonArray.length(); i++) {
				tmp.left.set(jsonArray.getString(i));
				context.write(tmp, outputValue);
			}
		}

	}
}

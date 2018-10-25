package it.unical.scalab.parsoda.common;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;

public interface MapperWriter {
	public void write(Context  context, TextTuple outputKey, Text outputValue) throws InterruptedException, IOException ;
}

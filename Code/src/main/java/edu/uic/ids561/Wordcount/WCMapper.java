package edu.uic.ids561.Wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.bson.*;

import com.mongodb.hadoop.io.BSONWritable;

public class WCMapper extends MapReduceBase implements
		Mapper<BSONWritable, BSONWritable, Text, IntWritable> {
	private final static IntWritable ONE = new IntWritable(1);
	private Text word = new Text();

	public void map(BSONWritable key, BSONWritable val,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
//		System.out.println(val);
		BSONObject value = (BSONObject) val.getDoc();
		String location = (String) value.get("location");
//		System.out.println(location);
		word.set(location);
		output.collect(word, ONE);

	}
}

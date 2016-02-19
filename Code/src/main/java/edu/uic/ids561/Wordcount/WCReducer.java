package edu.uic.ids561.Wordcount;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class WCReducer extends MapReduceBase implements
		Reducer<Text, IntWritable, Text, IntWritable> {
	
	public void reduce(Text key, Iterator<IntWritable> value,
			OutputCollector<Text, IntWritable> output, Reporter arg3)
			throws IOException {
		int sum = 0;
		while (value.hasNext()) {
			sum = sum + value.next().get();
		}

//		BSONObject b = new BasicBSONObject();
//		b.put("state", key);
//		b.put("count", sum);
		
		output.collect(key,new IntWritable(sum));

	}

}
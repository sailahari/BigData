package edu.uic.ids561.SentimentAnalysis;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.bson.*;

import com.mongodb.hadoop.io.BSONWritable;

public class SAMapper extends MapReduceBase implements
Mapper<BSONWritable, BSONWritable, Text, IntWritable> {
	
	private final static IntWritable one = new IntWritable(1);
	String sentimentRating = null;

	public void map(BSONWritable key, BSONWritable val,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		
		BSONObject value = (BSONObject) val.getDoc();
		String text = (String) value.get("text");
		Classifier sc = new Classifier();
		try {
			if (text.toString().length() > 0 && !(text == null)) {
				sentimentRating = sc.classify(text.toString());
				output.collect(new Text(sentimentRating), one);
				System.out.println(sentimentRating);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

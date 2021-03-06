package edu.uic.ids561.SentimentAnalysis;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.io.Text;

import com.mongodb.hadoop.mapred.MongoInputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;

@SuppressWarnings("deprecation")
@ManagedBean(name = "sADriver")
@SessionScoped
public class SADriver implements Tool {

	public int run(String[] arg0) throws Exception {

		Configuration conf = new Configuration();
		MongoConfigUtil.setInputURI(conf,
				"mongodb://localhost:27017/Sentiment.tweetText");
		DistributedCache.addFileToClassPath(new Path("/home/siva/Downloads/lingpipe-4.1.0.jar"), conf);

		JobConf job = new JobConf(conf);
		job.setJobName("sentiment");

		FileOutputFormat.setOutputPath(job, new Path(arg0[0]));

		job.setJarByClass(SADriver.class);
		job.setMapperClass(SAMapper.class);
		job.setReducerClass(SAReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setInputFormat(MongoInputFormat.class);

		JobClient.runJob(job);
		return 0;
	}

	public static void main(String args[]) throws Exception {
		int res = ToolRunner.run(new Configuration(), new SADriver(), args);
		System.exit(res);
	}

	public Configuration getConf() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setConf(Configuration arg0) {
		// TODO Auto-generated method stub

	}

}

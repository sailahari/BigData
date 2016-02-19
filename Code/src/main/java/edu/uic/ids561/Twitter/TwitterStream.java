package edu.uic.ids561.Twitter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import edu.uic.ids561.Hadoop.Hadoop;
import edu.uic.ids561.SentimentAnalysis.SADriver;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.User;

@ManagedBean(name = "twitterStream")
@SessionScoped
public class TwitterStream {

	// getting authorization variables from OAuth class
	private static String consumerKey = OAuth.getConsumerkey();
	private static String consumerSecret = OAuth.getConsumersecret();
	private static String accessToken = OAuth.getAccesstoken();
	private static String accessTokenSecret = OAuth.getAccesstokensecret();

	private  String keyword;
	
	private boolean pieChart=false;
	
	
	private static String filePath="/home/siva/workspace/TMiners/src/main/";
//	public static void main(String args[]) throws Exception {
//		new TwitterStream().getTweetsWordCount();
//	}
	public String getTweetsWordCount() throws Exception {
		int j = 0; // tweets iteration initialized to zero
		String mapState = "US-";
		// Read states and cities
		MongoDB mongo = new MongoDB();
		mongo.readStatesAbr();
		mongo.readStatesCities();
		// drop collection in mongodb
		mongo.dropCollection("Twitter", "tweets");
		// configure twitter
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(consumerKey);
		cb.setOAuthConsumerSecret(consumerSecret);
		cb.setOAuthAccessToken(accessToken);
		cb.setOAuthAccessTokenSecret(accessTokenSecret);

		// read tweets
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		Query query = new Query(keyword);
		QueryResult result;

		// loop through tweets and find location
		loop: do {
			result = twitter.search(query);
			List<Status> tweets = result.getTweets();
			for (Status tweet : tweets) {
				User user = tweet.getUser();
				String location = user.getLocation();
				// System.out.print(location);
				String[] locSplit = location.split(",");
				for (int i = 0; i < locSplit.length; i++) {
					String abr = mongo.isUSState(locSplit[i].trim());
					if (!abr.isEmpty()) {
						mongo.insertTweets(mapState + abr);
						j++;
						break;
					}
				}
				if (j == 100) {
					break loop;
				}
			}
		} while ((query = result.nextQuery()) != null);
	
		if(Hadoop.startHadoop()){
			if(Hadoop.wordcount()){
				if(Hadoop.stopHadoop()){
					LoadMapData.loaddata();
					System.out.println("done");
				}
			}
		}

		return "WORDCOUNT";
	}

	public String getTweetsSentiment() throws Exception {
		int j = 0; // tweets iteration initialized to zero
		MongoDB mongo = new MongoDB();
		// drop collection in mongodb
		mongo.dropCollection("Sentiment", "tweetText");
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(consumerKey);
		cb.setOAuthConsumerSecret(consumerSecret);
		cb.setOAuthAccessToken(accessToken);
		cb.setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		try {
			Query query = new Query(keyword);
			query.setLang("en");
			QueryResult result;
			Sloop: do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					j++;
					mongo.insertSentiment(tweet.getText().replaceAll("\n", " "));
					if (j == 100) {
						break Sloop;
					}
				}
			} while ((query = result.nextQuery()) != null);
		} catch (TwitterException te) {
			System.out.println("Search failed:" + te.getMessage());
			return "FAILED";
		}
		if(Hadoop.startHadoop()){
			if(Hadoop.sentiment()){
				if(Hadoop.stopHadoop()){
					LoadPieChart.generateChart(filePath);
					pieChart=true;
					System.out.println("sentiment completed");
				}
			}
		}
		return "SENTIMENT";
	}
	
	//getters and setters
	public static String getConsumerKey() {
		return consumerKey;
	}

	public static String getConsumerSecret() {
		return consumerSecret;
	}

	public static String getAccessToken() {
		return accessToken;
	}

	public static String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public static String getFilePath() {
		return filePath;
	}

	public boolean isPieChart() {
		return pieChart;
	}

	public void setPieChart(boolean pieChart) {
		this.pieChart = pieChart;
	}

}

package edu.uic.ids561.Twitter;

import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class MongoDB {

	// initialize mongodb variables
	private MongoClient mongoClient;
	private DB db;
	private DBCollection collection;

	// initialize hashmap for states and cities
	public static HashMap<String, String> stateName = new HashMap<String, String>();
	public static HashMap<String, String> abrState = new HashMap<String, String>();
	public static HashMap<String, String> cityState = new HashMap<String, String>();

	// Method to initiate connection
	@SuppressWarnings("deprecation")
	public void connection(String mongo_db, String mongo_colc) throws Exception {
		mongoClient = new MongoClient("localhost", 27017);
		db = mongoClient.getDB(mongo_db);
		collection = db.getCollection(mongo_colc);
	}

	// Method to read states and their abbrevations
	public void readStatesAbr() throws Exception {
		connection("states", "state_abr");
		DBCursor cursor = collection.find();
		String state;
		String abr;
		try {
			while (cursor.hasNext()) {
				DBObject o = cursor.next();
				state = (String) o.get("State");
				abr = (String) o.get("Abbrevation");
				stateName.put(state, abr);
				abrState.put(abr, abr);
			}
		} finally {
			cursor.close();
		}
		closeConnection();
	}

	// Method to read cities in a state
	public void readStatesCities() throws Exception {
		connection("state_citi", "stateCity");
		DBCursor cursor = collection.find();
		String state;
		String city;
		try {
			while (cursor.hasNext()) {
				DBObject o = cursor.next();
				state = (String) o.get("State");
				city = (String) o.get("City");
				cityState.put(city, state);
			}
		} finally {
			cursor.close();
		}
		closeConnection();

	}

	// Method to insert tweets
	public void insertTweets(String state) throws Exception {
		connection("Twitter", "tweets");
		BasicDBObject doc = new BasicDBObject("location", state);
		mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		collection.insert(doc);
		closeConnection();
	}

	// Method to insert sentiments text
	public void insertSentiment(String text) throws Exception {
		connection("Sentiment", "tweetText");
		BasicDBObject doc = new BasicDBObject("text", text);
		mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		collection.insert(doc);
		closeConnection();
	}

	// Method to drop collection
	public void dropCollection(String db, String collectionName)
			throws Exception {
		connection(db, collectionName);
		collection.drop();
		closeConnection();
	}

	// read tweets
	// public DBCursor readTweets() throws Exception {
	// connection("Twitter", "tweets");
	// DBCursor cursor = collection.find();
	// closeConnection();
	// return cursor;
	// }

	public void closeConnection() throws Exception {
		mongoClient.close();
	}

	// validation of us state
	public String isUSState(String location) {
		String sN = stateName.get(location);
		String aS = abrState.get(location);
		String cS = cityState.get(location);
		if (sN != null) {
			return sN;
		} else if (aS != null) {
			return aS;
		} else if (cS != null) {
			String cSabr = stateName.get(cS);
			return cSabr;
		}
		return "";
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}

	public static HashMap<String, String> getStateName() {
		return stateName;
	}

	public static void setStateName(HashMap<String, String> stateName) {
		MongoDB.stateName = stateName;
	}

	public static HashMap<String, String> getAbrState() {
		return abrState;
	}

	public static void setAbrState(HashMap<String, String> abrState) {
		MongoDB.abrState = abrState;
	}

	public static HashMap<String, String> getCityState() {
		return cityState;
	}

	public static void setCityState(HashMap<String, String> cityState) {
		MongoDB.cityState = cityState;
	}

}

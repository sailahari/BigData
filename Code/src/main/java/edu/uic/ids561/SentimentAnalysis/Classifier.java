package edu.uic.ids561.SentimentAnalysis;

import java.io.File;
import java.io.IOException;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

public class Classifier {
	String[] categories;
	public static LMClassifier cls;
	
	private static String filePath="/home/siva/workspace/TMiners/src/main/";

	public Classifier() {
		try {
			cls = (LMClassifier) AbstractExternalizable.readObject(new File(filePath+
					"/resources/classifier/classifier.txt"));
			categories = cls.categories();
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public String classify(String text) {
		ConditionalClassification classification = cls.classify(text);
		return classification.bestCategory();
	}
	
	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public static LMClassifier getCls() {
		return cls;
	}

	public static void setCls(LMClassifier cls) {
		Classifier.cls = cls;
	}
	

	public static String getFilePath() {
		return filePath;
	}

}
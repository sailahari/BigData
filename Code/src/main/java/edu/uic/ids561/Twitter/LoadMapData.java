package edu.uic.ids561.Twitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.json.simple.JSONValue;

@ManagedBean(name = "loadMapData")
@SessionScoped
public class LoadMapData {
	
	private static List<Map> l1;
	private static String initial = "{ \"map\": \"usaLow\", \"areas\": ";
	private static String end = "}";

	public static void loaddata() throws NumberFormatException, IOException {

		// Read the wordcount output and convert into ammap json format for
		// loading the data into maps
		int sum=0;
		String jsonText="";
		l1 = new LinkedList<Map>();
		String filePath="/home/siva/workspace/TMiners/src/main/";
		BufferedReader br = new BufferedReader(new FileReader(filePath
				+ "/resources/wordcount/part-00000"));
		String readLine;
		while ((readLine = br.readLine()) != null) {
			String temp[] = readLine.split("\\t");
			Map reducerOutput = new LinkedHashMap();
			reducerOutput.put("id", temp[0].trim());
			reducerOutput.put("value", Integer.parseInt(temp[1].trim()));
			sum+=Integer.parseInt(temp[1].trim());
			l1.add(reducerOutput);
		}
		br.close();
		jsonText = JSONValue.toJSONString(l1);
		System.out.println(initial + jsonText + end);
		System.out.println(sum);

		//Removing the previous data in file 
		File f = new File(filePath + "/webapp/map.json");
		if (f.exists()) {
			f.delete();
		}
//		PrintWriter p = new PrintWriter(filePath + "/webapp/map.json");
//		p.close();

		//Adding the new json output
		FileWriter file = new FileWriter(filePath + "/webapp/map.json");
		file.write(initial + jsonText + end);
		file.flush();
		file.close();

	}

	public static List<Map> getL1() {
		return l1;
	}

	public static void setL1(List<Map> l1) {
		LoadMapData.l1 = l1;
	}

	public static String getInitial() {
		return initial;
	}

	public static String getEnd() {
		return end;
	}
	
	


}

package edu.uic.ids561.Hadoop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import edu.uic.ids561.Twitter.TwitterStream;

public class Hadoop {

	private static String hadoopPath = "/home/siva/hadoop/bin";

	public static String getHadoopPath() {
		return hadoopPath;
	}

	public static boolean startHadoop() throws IOException,
			InterruptedException {

		// start hadoop
		Process p = Runtime.getRuntime().exec(hadoopPath + "/start-all.sh");
		InputStream pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);

		// To run the commands immediately, we exit the safe mode
		String cmd = hadoopPath + "/hadoop dfsadmin -safemode leave";
		Process p1 = Runtime.getRuntime().exec(cmd);
		InputStream p1In = p1.getInputStream();
		p1.waitFor();
		System.out.println(p1In);

		return true;
	}

	public static boolean stopHadoop() throws IOException, InterruptedException {

		// stop hadoop
		Process p = Runtime.getRuntime().exec(hadoopPath + "/stop-all.sh");
		InputStream pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);

		return true;
	}

	public static boolean wordcount() throws Exception {
		Process p;
		InputStream pIn;

		// delete output folder in hadoop
		String delcmd = hadoopPath + "/hadoop fs -rmr wcoutput";
		p = Runtime.getRuntime().exec(delcmd);
		pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);
//
//		ServletContext sc = (ServletContext) FacesContext.getCurrentInstance()
//				.getExternalContext().getContext();
//		String path = sc.getRealPath("/");
//		System.out.println(path);

		String filePath = TwitterStream.getFilePath()+"/resources";
		String cmd = hadoopPath
				+ "/hadoop jar "
				+ filePath
				+ "/Jars/wordcount.jar edu.uic.ids561.Wordcount.WCDriver wcoutput";
		p = Runtime.getRuntime().exec(cmd);
		pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);

		// move wordcount ouput to local system project path
		// delete output if exists in the local path
		File f = new File(filePath + "/wordcount/part-00000");
		if (f.exists()) {
			f.delete();
		}
		// move output
		String moveCmd = hadoopPath
				+ "/hadoop fs -copyToLocal wcoutput/part-00000 " + filePath
				+ "/wordcount";
		p = Runtime.getRuntime().exec(moveCmd);
		pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);
		return true;
	}

	public static boolean sentiment() throws Exception {

		Process p;
		InputStream pIn;

		// delete output folder in hadoop
		String delcmd = hadoopPath + "/hadoop fs -rmr saOutput";
		p = Runtime.getRuntime().exec(delcmd);
		pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);

		// run hadoop wordcount
		// String path = new File(".").getCanonicalPath();
		// String filePath=path+"/src/main/resources";
		String filePath = TwitterStream.getFilePath()+"/resources";
		String cmd = hadoopPath
				+ "/hadoop jar "
				+ filePath
				+ "/Jars/sentiment.jar edu.uic.ids561.SentimentAnalysis.SADriver saOutput";
		p = Runtime.getRuntime().exec(cmd);
		pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);

		// move wordcount ouput to local system project path
		// delete output if exists in the local path
		File f = new File(filePath + "/sentiment/part-00000");
		if (f.exists()) {
			f.delete();
		}
		// move output
		String moveCmd = hadoopPath
				+ "/hadoop fs -copyToLocal saOutput/part-00000 " + filePath
				+ "/sentiment";
		p = Runtime.getRuntime().exec(moveCmd);
		pIn = p.getInputStream();
		p.waitFor();
		System.out.println(pIn);
		return true;
	}

}

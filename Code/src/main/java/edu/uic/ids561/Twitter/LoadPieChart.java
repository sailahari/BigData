package edu.uic.ids561.Twitter;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class LoadPieChart {
	private static int positive,negative,neutral,total;
	private static double positivePercentage,negativePercentage,neutralPercentage;

	public static void generateChart(String path) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(path
				+ "/resources/sentiment/part-00000"));
		String readLine;
		positive=0;
		negative=0;
		neutral=0;
		total=0;
		positivePercentage=0;
		negativePercentage=0;
		neutralPercentage=0;

		while ((readLine = br.readLine()) != null) {
			String[] temp = readLine.split("\\t");
			if (temp[0].trim().equalsIgnoreCase("pos")) {
				positive = Integer.parseInt(temp[1]);
			} else if (temp[0].trim().equalsIgnoreCase("neg")) {
				negative = Integer.parseInt(temp[1]);
			} else if (temp[0].trim().equalsIgnoreCase("neu")) {
				neutral = Integer.parseInt(temp[1]);
			}
		}
		br.close();
		total = positive + negative + neutral;
		
		positivePercentage = ((double) positive) / total * 100;
		negativePercentage = ((double) negative) / total * 100;
		neutralPercentage = ((double) neutral) / total * 100;
		setPrecision();
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Positive", positivePercentage);
		dataset.setValue("Negative", negativePercentage);
		dataset.setValue("Neutral", neutralPercentage);
		JFreeChart chart = ChartFactory.createPieChart("Sentiment Analysis", //chart title
				dataset,//data
				true, //include legend
				true, false);
		// Setting colors to plot
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint("Positive", Color.GREEN);
		plot.setSectionPaint("Negative", Color.RED);
		plot.setSectionPaint("Neutral", Color.blue);
		plot.setSimpleLabels(true);
		// Display percentages in the pie chart
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
				"{1}%");

		plot.setLabelGenerator(gen);
		
		saveChart(chart);
	}
	
	private static void setPrecision() {
		BigDecimal value = new BigDecimal(positivePercentage);
		positivePercentage = value.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		value = new BigDecimal(negativePercentage);
		negativePercentage =value.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		value = new BigDecimal(neutralPercentage);
		neutralPercentage = value.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private static void saveChart(JFreeChart chart) {
		try {
			ServletContext ctx = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();

			String deploymentDirectoryPath = ctx.getRealPath("/");
			String path = deploymentDirectoryPath + "/JFreechart.png";
			File file = new File(path);
			ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static int getPositive() {
		return positive;
	}
	public static void setPositive(int positive) {
		LoadPieChart.positive = positive;
	}
	public static int getNegative() {
		return negative;
	}
	public static void setNegative(int negative) {
		LoadPieChart.negative = negative;
	}
	public static int getNeutral() {
		return neutral;
	}
	public static void setNeutral(int neutral) {
		LoadPieChart.neutral = neutral;
	}
	public static int getTotal() {
		return total;
	}
	public static void setTotal(int total) {
		LoadPieChart.total = total;
	}
	public static double getPositivePercentage() {
		return positivePercentage;
	}
	public static void setPositivePercentage(double positivePercentage) {
		LoadPieChart.positivePercentage = positivePercentage;
	}
	public static double getNegativePercentage() {
		return negativePercentage;
	}
	public static void setNegativePercentage(double negativePercentage) {
		LoadPieChart.negativePercentage = negativePercentage;
	}
	public static double getNeutralPercentage() {
		return neutralPercentage;
	}
	public static void setNeutralPercentage(double neutralPercentage) {
		LoadPieChart.neutralPercentage = neutralPercentage;
	}
}
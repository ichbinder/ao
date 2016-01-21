package Ue4_sortieren;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;

public class Main {

	public static long insertionCnt = 0;
	public static long mergeCnt = 0;
	public static long quickCnt = 0;
	
	private static final int END_NUMBERS = 1000000;
	private static final int STEPS = 50000;
	
	private static final int COUNT_TESTRUNS = 100;
	private static final Random generator = new Random();
	private static int[] randomArr;
	private static long[] durations = new long [COUNT_TESTRUNS];
	
	private static Graph graph;
	
	public static void main(String[] args) {
		buildDiagram();
	}
	
	private static void buildDiagram() {
		graph = new Graph();
		float[][] graphMap = new float[4] [END_NUMBERS/STEPS];
		long[][] tempMap = new long [END_NUMBERS/STEPS][4];
		int count = 0;
		
		for (int i = 0; i < END_NUMBERS; i+=STEPS) {
			System.out.println("Anzahl Elemente: " + i);
			long[] ret = testRun(i);
			ret[2] = i;
			tempMap[count] = ret;
			count++;
		}
		
		long peakVal = 0;
		for (int i = 0; i < tempMap.length; i++) {
			if(tempMap[i][0] > peakVal) peakVal = tempMap[i][0];
			if(tempMap[i][1] > peakVal) peakVal = tempMap[i][1];
			if(tempMap[i][2] > peakVal) peakVal = tempMap[i][2];
		}
		
		for (int i = 0; i < tempMap.length; i++){
			graphMap[2][i] = (tempMap[i][0] / (float) peakVal);
			graphMap[3][i] = (i+1)*STEPS / (float) END_NUMBERS;
		}
		
		graph.addGraph(graphMap, "Heap");
		
		graphMap = new float[4][END_NUMBERS/STEPS];
		for (int i = 0; i < tempMap.length; i++) {
			graphMap[2][i] = (tempMap[i][1] / (float) peakVal);
			graphMap[3][i] = (i+1)*STEPS/(float) END_NUMBERS;
		}
		
		graph.addGraph(graphMap, "Quick");
		
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("table.csv"), "utf-8"));
				for (long[] arr : tempMap) {
					writer.write(arr[2] + ";" + arr[0] + ";" + arr[1]);
					writer.write("\n");
				}
		}	catch(Exception e) {
			System.out.println(e.getMessage());
		}	finally{
				try {
					writer.close();
				} catch (IOException e) {
					
				}
		}
	}
	
	public static long[] testRun(int COUNT_NUMBERS) {
		long[] ret = new long[3];
		
		//init
		int[] sortArr = new int [COUNT_NUMBERS];
		for (int i=0; i<COUNT_NUMBERS; i++) {
			sortArr[i] = generator.nextInt(COUNT_NUMBERS);
		}
		randomArr = Arrays.copyOf(sortArr, COUNT_NUMBERS);
		System.out.println("READY SETUP");
		
		//heapSort
		for (int i=0; i<COUNT_TESTRUNS; i++) {
			sortArr = Arrays.copyOf(randomArr, COUNT_NUMBERS);
			
			long currentTime = System.nanoTime();
			HeapSort.heapsort(sortArr);
			long finishTime = System.nanoTime();
			
			durations[i] = finishTime - currentTime;
		}
		
		long sumDuration = 0;
		for(int i=0; i<durations.length; i++) {
			sumDuration+=durations[i];
		}
		long meanTime = sumDuration/durations.length;
		System.out.println("Mean Time Heap Sort: " + meanTime);
		
		ret[0] = meanTime;
		
		//quick sort
		for (int i=0; i<COUNT_TESTRUNS; i++) {
			sortArr = Arrays.copyOf(randomArr, COUNT_NUMBERS);
			
			long currentTime = System.nanoTime();
			GivenQuickSort.quickSort(sortArr, 0, sortArr.length -1);
			long finishTime = System.nanoTime();
			
			durations[i] = finishTime-currentTime;
		}
		sumDuration = 0;
		for (int i=0; i<durations.length; i++) {
			sumDuration+=durations[i];
		}
		meanTime = sumDuration/durations.length;
		System.out.println("Mean Time Quick Sort: " + meanTime);
		
		ret[1] = meanTime;
		return ret;
	}
}

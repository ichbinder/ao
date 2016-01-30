package ue04_neu;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import java.util.TreeMap;

//import Ue4_sortieren.*;
//import ue4.Compare;


public class Main {

	public static long insertionCnt = 0;
	public static long mergeCnt = 0;
	public static long quickCnt = 0;


	static TreeMap<Integer, Double> insertionTimes; 
	static TreeMap<Integer, Double> mergeTimes; 
	static TreeMap<Integer, Double> quickTimes; 
	
	public static void main(String[] args) throws IOException {

		CompareAlgorithms(1000, 20000, 10, true);
		Compare.optimizedValues();
	}
	
	public static void TestQuickSort(int min, int max, int times, boolean unsorted){
		
		quickTimes = new TreeMap<Integer, Double>();	
		Random randomizer = new Random();		
		int [] list = createSortedList(min);
		
		for(int i = min; i < max; i++){

			long middle = 0;			
			if(!unsorted){
				
				list = sortedList(i, list);
			}
			
			for(int j = 0; j < times; j++){
			
				if(unsorted) list = randomList(i);
				
				long currentTime = System.nanoTime();
				QuickSort.sortiere(list);
				long finishTime = System.nanoTime();				
				middle += finishTime - currentTime;
			}
			quickTimes.put(i, (middle / times) / 1000000.0 );
			System.out.println(i);
		}
	}
	
	public static int [] sortedList(int length, int [] src){		
		int [] dest = new int[length];		
		
		System.arraycopy( src, 0, dest, 0, src.length);		
		dest[dest.length -1] = length;

		return dest;
	}


	
	public static void TestMergeSort(int min, int max, int times, boolean unsorted){
		
		mergeTimes = new TreeMap<Integer, Double>();	
		Random randomizer = new Random();		
		int [] list = createSortedList(min);

		MergeSort merge = new MergeSort();

		for(int i = min; i < max; i++){			

			if(!unsorted){				
				list = sortedList(i, list);
			}

			long middle = 0;
			for(int j = 0; j < times; j++){
			
				if(unsorted) list = randomList(i);				
				long currentTime = System.nanoTime();
				merge.sort(list);
				long finishTime = System.nanoTime();				
				middle += finishTime - currentTime;
			}
			mergeTimes.put(i, (middle / times) / 1000000.0 );
			System.out.println(i);
		}
	}

	public static int [] createSortedList(int count){
		
		int [] sortedList = new int[count];
		
		for(int i = 0; i < count; i++){
			
			sortedList[i] = i;
		}
		
		return sortedList;
	}
	
	
	public static void TestInsertionSort(int min, int max, int times, boolean unsorted){
		
		insertionTimes = new TreeMap<Integer, Double>();	
		Random randomizer = new Random();		
		int [] list = createSortedList(min);
	
		
		for(int i = min; i < max; i++){			

			if(!unsorted){
				list = sortedList(i, list);
			}
						
			long middle = 0;
			for(int j = 0; j < times; j++){
			
				if(unsorted)list = randomList(i);
				long currentTime = System.nanoTime();
				InsertionSort.insertionSort(list);
				long finishTime = System.nanoTime();				
				middle += finishTime - currentTime;
			}
			insertionTimes.put(i, (middle / times) / 1000000.0);
			System.out.println(i);
		}
	}
	
	
	public static void TestInsertionSortThiel(int min, int max, int times){
		
		insertionTimes = new TreeMap<Integer, Double>();	
		Random randomizer = new Random();		
		int [] list;
		
		for(int i = min; i < max; i++){			

			long middle = 0;
			for(int j = 0; j < times; j++){
			
				list = randomList(i);
				long currentTime = System.nanoTime();
				SortAlgorithms.insertionSort(list);
				long finishTime = System.nanoTime();				
				middle += finishTime - currentTime;
			}
			insertionTimes.put(i, (middle / times) / 1000000.0);
			System.out.println(i);
		}
	}
	
	
	
	public static void CompareAlgorithmsThiel(int min, int max, int times){

		System.out.println("Teste Insertionsort");
		TestInsertionSortThiel(min, max, times);
		
		System.out.println("Teste Mergesort");
//		TestMergeSortThiel(min, max, times);

		System.out.println("Teste Quicksort");
	//	TestQuickSortThiel(min, max, times);
		
		System.out.println("Speichere Listen");
		saveLists(min, max);
	}

	
	public static void CompareAlgorithms(int min, int max, int times, boolean unsorted){

		System.out.println("Teste Insertionsort");
		TestInsertionSort(min, max, times, unsorted);
		
		System.out.println("Teste Mergesort");
		TestMergeSort(min, max, times, unsorted);

		System.out.println("Teste Quicksort");
		TestQuickSort(min, max, times, unsorted);		
		
		System.out.println("Speichere Listen");
		saveLists(min, max);
	}

	public static void saveLists(int min, int max){
		
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./table.csv"), "utf-8"));
		
			writer.write(";InsertionSort;MergeSort;QuickSort\n");
			printLists(min, max, writer);
			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally{
			try {
				writer.close();
			} 
			catch (IOException e) {
				
			}
		}
	}
	
	public static void printLists(int min, int max, Writer writer) throws IOException{
		
		String message = "";
		for(int i = min; i < max; i++){
			
			
			message = i + ";" + insertionTimes.get(i) + ";" + mergeTimes.get(i) + ";" + quickTimes.get(i) + "\n";
			writer.write(message);
		}		
	}
	
	public static void printLists(int min, int max){
		
		for(int i = min; i < max; i++){
			
			System.out.println(i + ":" + insertionTimes.get(i));
		}		
	}
	
	
	public static void CompareAlgorithmsFromThiel(){
		
		System.out.println("----------------------");		
		System.out.println("Vergleich InsertionSort:");		
		CompareInsertionSort();

		System.out.println("----------------------");		
		System.out.println("Vergleich MergeSort:");		
		CompareMergeSort();

		System.out.println("----------------------");		
		System.out.println("Vergleich QuickSort:");		
		CompareQuickSort();		
	}
	
	public static void CompareQuickSort(){
		
		int [] list = randomList(10);					
		int[] copy = list.clone();
			
		System.out.println("Original Liste:");		
		printList(list);
		SortAlgorithms.quickSort(list, 0, list.length -1);
		
		System.out.println("Thiels Liste:");
		printList(list);
	
		QuickSort.sortiere(copy);
		System.out.println("Unsere Liste:");
		printList(copy);
		
	}
	
	public static void CompareMergeSort(){
		
		int [] list = randomList(10);					
		int[] copy = list.clone();
			
		System.out.println("Original Liste:");		
		printList(list);
		SortAlgorithms.mergeSort(list, 0, list.length-1);
		
		System.out.println("Thiels Liste:");
		printList(list);
	
		MergeSort mergeSort = new MergeSort();
		mergeSort.sort(copy);
		System.out.println("Unsere Liste:");
		printList(copy);						

	}
	
	public static void CompareInsertionSort(){
		
		int [] list = randomList(10);					
		int[] copy = list.clone();
			
		System.out.println("Original Liste:");		
		printList(list);
		SortAlgorithms.insertionSort(list);		
		
		System.out.println("Thiels Liste:");
		printList(list);
	
		InsertionSort.insertionSort(copy);		
		System.out.println("Unsere Liste:");
		printList(copy);						

	}
	
	public static void printList(int [] list){

		String message = "";
		for(int i = 0; i < list.length; i++){

			message += list[i] + ";";
		}
		System.out.println(message);
	}
	
	private static int [] randomList (int length){
		
		Random randomizer = new Random();
		int [] randomList = new int[length];
		
		for(int i = 0; i< length; i++){
			
			randomList[i] = randomizer.nextInt(length);			
		}		
		
		return randomList;
	}
}

package ue04_neu;

import java.util.Random;
import Ue4_sortieren.*;


public class Main {

	
	public static void main(String[] args) {

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

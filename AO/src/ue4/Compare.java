package ue4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Compare {

	public static int[] convertIntegers(List<Integer> integers, boolean random)
	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    
	    for (int i = 0; i < ret.length ; i++)
	    {
	        //ret[i] = iterator.next().intValue()	    	
	        if(random){
	        	ret[i] = (int) Math.random() * iterator.next();
	        }
	        else{
	        	ret[i] = i ;
	        }	        
	    }
	    return ret;
	}
	
	
	public static void optimizedValues() throws IOException{
		
		OutputStream outputStream       = new FileOutputStream("sortComparisionOptim.csv");
		Writer       outputStreamWriter = new OutputStreamWriter(outputStream);
		outputStreamWriter.write("n; MergeSort;InsertionSort;QuickSort\n");
		
		for(int n = 1000; n < 20000; n++){
			
			double insert = Math.pow(n, 2) / 1000000.0;// * 1000;
			double merge_quick = n * Math.log10(n) / 1000000.0;// * 1000;
			String sLine = n + ";" + insert + ";" + merge_quick + ";" + merge_quick + "\n";
			while(sLine.contains(".")){
				sLine = sLine.replace('.', ',');
			}
			outputStreamWriter.write(sLine);
		}
		outputStreamWriter.close();
		outputStream.close();
		System.out.println("FERTIG");
	}
	
	// 1-300 mal den Vorgang wiederholen
	// 100 * Messwerte mit der selben Reihe -> Mittelwert bilden	
	private static void tests() throws IOException{
		
		int counter = 10000;
		ArrayList<Integer> n = new ArrayList<>();
		int max = 100000;
		int min = 1;

		OutputStream outputStream       = new FileOutputStream("sortComparisionSort2.csv");
		Writer       outputStreamWriter = new OutputStreamWriter(outputStream);

		outputStreamWriter.write("n;MergeSort;InsertionSort;QuickSort\n");
		for (int i = 0; i < 1000; i++) {
			
			n.add(i*10000000);
//			n.add(i*10000000);

//			n.add(i);
			int[] conv = convertIntegers(n, false);
			
			int[] input = new int [conv.length];
			System.arraycopy(conv, 0, input, 0, input.length);
			
			double timeSum = 0;
			double timeSumMerge = 0, timeSumInsertion = 0, timeSumQuick = 0;
			
			double middleTimeMerge = 0, middleTimeInsertion = 0, middleTimeQuick = 0;

			long middle = 0, lg = 0; 
			for(int j = 0; j < counter; j++){
				
				long timeStart = System.nanoTime();	
				MergeSort.mergeSort(conv, 0, n.size() -1);
				long timeStop = System.nanoTime();
				timeSumMerge += timeStop - timeStart;

//				middle += (timeStop - timeStart);
//				lg += conv.length;
			}			
			
//			System.out.println(lg/counter + ";" + middle / counter);
			
//			middleTimeMerge = middle / counter;
			middleTimeMerge = timeSumMerge / counter;					
			
			for(int j = 0; j < counter; j++){
				
				long timeStart = System.nanoTime();	
				Insertionsort.insertionSort(input);
				long timeStop = System.nanoTime();
				timeSumInsertion += timeStop - timeStart;

//				middle += (timeStop - timeStart);
//				lg += conv.length;
			}
			middleTimeInsertion = timeSumInsertion / counter;
//			middleTimeInsertion = middle / counter;

			
			for(int j = 0; j < counter; j++){
				
				long timeStart = System.nanoTime();	
				Insertionsort.insertionSort(input);
				long timeStop = System.nanoTime();
				timeSumQuick += timeStop - timeStart;
			}		
			
			middleTimeQuick = timeSumQuick / counter;

			String sLine = conv.length + ";" + middleTimeMerge + ";" + middleTimeInsertion + ";" + middleTimeQuick + "\n";
			
			while(sLine.contains(".")){
				sLine = sLine.replace('.', ',');					
			}
			
			outputStreamWriter.write(sLine);
//			outputStreamWriter.write(conv.length + ";" + middleTime + "\n");
			System.out.print(sLine);
		}				
		System.out.println("Size:" + n.size());
		outputStreamWriter.close();		
		
	}
	
	public static void main(String[] args) throws IOException {
			
//			tests();
			optimizedValues();
		}
}

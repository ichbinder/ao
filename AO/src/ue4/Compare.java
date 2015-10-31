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
	
	
	private static void optimizedValues() throws IOException{
		
		OutputStream outputStream       = new FileOutputStream("sortComparisionOptim.csv");
		Writer       outputStreamWriter = new OutputStreamWriter(outputStream);
		outputStreamWriter.write("n; MergeSort;InsertionSort;QuickSort\n");
				
		for(int i = 1; i < 301; i++){
			
			double insert = Math.pow(i, 2) * 1000;
			double merge_quick = i * Math.log10(i) * 1000;
			String sLine = i + ";" + insert + ";" + merge_quick + ";" + merge_quick + "\n";
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
		
		
		ArrayList<Integer> n = new ArrayList<>();
		int max = 100000;
		int min = 1;

		OutputStream outputStream       = new FileOutputStream("sortComparisionSortiert.csv");
		Writer       outputStreamWriter = new OutputStreamWriter(outputStream);

		outputStreamWriter.write("n;MergeSort;InsertionSort;QuickSort\n");
		for (int i = 0; i < 300; i++) {
			
//			n.add(i*100000000);
			n.add(i);
			int[] conv = convertIntegers(n, false);
			
			int[] input = new int [conv.length];
			System.arraycopy(conv, 0, input, 0, input.length);
			
			double timeSum = 0;
			double middleTimeMerge = 0, middleTimeInsertion = 0, middleTimeQuick = 0;

			for(int j = 0; j < 100; j++){
				
				long timeStart = System.nanoTime();	
				MergeSort.mergeSort(conv, 0, n.size() -1);
				long timeStop = System.nanoTime();
				timeSum += timeStop - timeStart;
			}
			
			middleTimeMerge = timeSum / 100;					

			
			for(int j = 0; j < 100; j++){
				
				long timeStart = System.nanoTime();	
				Insertionsort.insertionSort(input);
				long timeStop = System.nanoTime();
				timeSum += timeStop - timeStart;
			}
			middleTimeInsertion = timeSum / 100.0;
			
			for(int j = 0; j < 100; j++){
				
				long timeStart = System.nanoTime();	
				Insertionsort.insertionSort(input);
				long timeStop = System.nanoTime();
				timeSum += timeStop - timeStart;
			}				
			
			middleTimeQuick = timeSum / 100.0;

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

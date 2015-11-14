package ue5;

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
		
		OutputStream outputStream       = new FileOutputStream("sortComparisionOptim2.csv");
		Writer       outputStreamWriter = new OutputStreamWriter(outputStream);
		outputStreamWriter.write("n; MergeSort;InsertionSort;QuickSort\n");

		
		for(int i = 0; i < 100; i++){
			
			double insert = Math.pow(i, 2) * 100000;// * 1000;
			double merge_quick = i * Math.log10(i) * 100000;// * 1000;
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
		
		int counter = 10000;
		ArrayList<Integer> n = new ArrayList<>();
		OutputStream outputStream       = new FileOutputStream("sortComparisionSort2.csv");
		Writer       outputStreamWriter = new OutputStreamWriter(outputStream);

		outputStreamWriter.write("n;MergeSort;InsertionSort;QuickSort\n");
		for (int i = 0; i < 1000; i++) {
			
			n.add(i*10000000);
			int[] conv = convertIntegers(n, false);
			
			int[] input = new int [conv.length];
			System.arraycopy(conv, 0, input, 0, input.length);
			
			double timeSumHeap = 0, timeSumQuick = 0;
			
			double middleTimeHeap = 0, middleTimeQuick = 0;

			for(int j = 0; j < counter; j++){
				
				long timeStart = System.nanoTime();	
				HeapSort.heapsort(input);
				long timeStop = System.nanoTime();
				timeSumHeap += timeStop - timeStart;
			}			
			
			middleTimeHeap = timeSumHeap / counter;					
			
			for(int j = 0; j < counter; j++){
				
				long timeStart = System.nanoTime();	
				QuickSort.sortiere(input);
				long timeStop = System.nanoTime();
				timeSumQuick += timeStop - timeStart;
			}
			middleTimeQuick = timeSumQuick / counter;

			
			middleTimeQuick = timeSumQuick / counter;

			String sLine = conv.length + ";" + middleTimeHeap + ";" + middleTimeQuick + "\n";
			
			while(sLine.contains(".")){
				sLine = sLine.replace('.', ',');					
			}
			
			outputStreamWriter.write(sLine);
			System.out.print(sLine);
		}				
		System.out.println("Size:" + n.size());
		outputStreamWriter.close();		
		
	}
	
	public static void main(String[] args) throws IOException {
			
			tests();
			optimizedValues();
		}
}

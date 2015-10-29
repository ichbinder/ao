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

	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	//1-300 mal den Vorgang wiederholen
		//100 * Messwerte mit der selben Reihe -> Mittelwert bilden
		
	public static void main(String[] args) throws IOException {
			
			ArrayList<Integer> n = new ArrayList<>();
			int max = 100000;
			int min = 1;

			OutputStream outputStream       = new FileOutputStream("resultMerge.csv");
			Writer       outputStreamWriter = new OutputStreamWriter(outputStream);

			outputStreamWriter.write("n;Merge Sort Time;Insertion Sort Time\n");
			for (int i = 0; i < 300; i++) {
				
				n.add(i*100000000);			
				int[] conv = convertIntegers(n);
				
				int[] input = new int [conv.length];
				System.arraycopy(conv, 0, input, 0, input.length);
				
				double timeSum = 0;
				double middleTimeMerge = 0, middleTimeInsertion = 0;

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

				String sLine = conv.length + ";" + middleTimeMerge + ";" + middleTimeInsertion +"\n";
				sLine.replace('.', ',');
				outputStreamWriter.write(sLine);
//				outputStreamWriter.write(conv.length + ";" + middleTime + "\n");
				System.out.println(conv.length+";"+ middleTimeMerge + ";" + middleTimeInsertion);
			}				
			outputStreamWriter.close();		
		}
}

package ue04_neu;

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
		outputStreamWriter.write("n;InsertionSort;MergeSort;QuickSort\n");
		
		for(int n = 1000; n < 20000; n++){
			
			double insert = Math.pow(n, 2);// * 1000;
			double merge_quick = n * Math.log10(n);// * 1000;
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
	
	
	public static void main(String[] args) throws IOException {
			
//			tests();
			optimizedValues();
		}
}

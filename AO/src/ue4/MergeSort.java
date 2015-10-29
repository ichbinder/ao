package ue4;

import java.util.Iterator;
import java.util.List;


public class MergeSort {

	public static void merge(int[] A, int al, int ar,
            int[] B, int bl, int br, int[] C) {
		int i=al, j=bl;
		int size = ar-al +br-bl +1;
		for (int k=0; k<= size; k++){
			if (i>ar){C[k]=B[j++]; continue;}
			if (j>br){C[k]=A[i++]; continue;}
			C[k]=(A[i]<B[j]) ? A[i++] : B[j++]; 
		}
	}
	
	public static void mergeSort(int[] A, int al, int ar){
		if (ar>al) {
			int m=(ar+al)/2;
			mergeSort(A, al, m);
			mergeSort(A, m+1, ar);
			int size = ar-al +1;
			int[] B= new int[size];
			merge(A, al, m, A, m+1, ar, B);
			
			for (int i=0; i< size; i++){
				A[al+i]=B[i];
			}
		}
	}
	
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
	

	
}

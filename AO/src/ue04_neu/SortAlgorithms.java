package ue04_neu;

public class SortAlgorithms {

	public static void insertionSort(int[] A) {
	    int i, key;
	    for (int j=1; j<A.length; j++){
	    	key=A[j];
	    	i=j-1;
	    	while (i>=0 && A[i]>key){
	    		A[i+1]=A[i];
	    		i--; 
	    	}
	    	A[i+1]=key;
	   	}
	}

	
	public static void merge(int[] A, int al, int ar, int[] B, int bl, int br, int[] C) {
		int i=al, j=bl;
		
		int calculation = ar -al + br - bl +1;
				
		for (int k=0; k<= calculation; k++){
			if (i>ar){
				C[k]=B[j++]; continue;
			}
			if (j>br){
				C[k]=A[i++]; continue;
			}
			C[k]=(A[i]<B[j]) ? A[i++] : B[j++];
		}
	}
		
	
	public static void mergeSort(int[] A, int al, int ar){ 
		if (ar>al) {
			int m=(ar+al)/2;
			mergeSort(A, al, m);
			mergeSort(A, m+1, ar);
			
			int arl = ar -al +1;
			int[] B=new int[arl];
			merge(A, al, m, A, m+1, ar, B);
						
			for (int i=0; i < arl; i++){
				A[al+i]=B[i];
			}
		}	
	}
	
	
	public static void quickSort(int[] A, int al, int ar){
		if (al<ar){
			int pivot = A[al], i=al, j=ar+1;
			while (true){
				while (A[++i]<pivot && i<ar){}
				while (A[--j]>pivot && j>al){} 
				if (i<j) swap(A, i, j);
				else break;
			}
			swap(A, j, al);
			quickSort(A, al, j-1);
			quickSort(A, j+1, ar);
		}
	}
	
	public static void swap(int[] A, int i, int j){
		int t=A[i]; A[i]=A[j]; A[j]=t; 
	}
	
}

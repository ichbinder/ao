package Ue4_sortieren;

public class GivenQuickSort {

	public static void quickSort (int[] A, int al, int ar) {
		if (al < ar) {
			int pivot = A[al], i=al, j=ar+1;
			while (true) {
				while (A[++i] < pivot && i < ar) {}
				while (A[--j] > pivot && j > al) {}
				if (i<j) swap (A, i, j);
				else break;
			}
			swap(A, j, al);
			
			quickSort(A, al, j-1);
			quickSort(A, j+1, ar);
		}
	}
	
	public static void swap(int[] A, int i, int j) {
		int t = A[i]; A[i] = A[j]; A[j] = t;
	}
}

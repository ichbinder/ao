package Ue4_sortieren;

public class HeapSort {
	
	public static void heapsort(int[] A) {
		buildMaxHeap(A);
		
		for (int i = A.length -1; i > 0; --i) {
			swap(A, 0, i);
			maxHeapify(A, i, A.length -1);
		}
	}
	
	private static void maxHeapify(int[] A, int i, int heapSize) {
		int l = left(i);
		int r = right(i);
		int maximum;
		
		if (l < heapSize && A[l] > A[i])
			maximum = l;
		else 
			maximum = i;
		
		if (r < heapSize && A[r] > A[maximum])
			maximum = r;
		
		if (maximum != i) {
			swap (A, i, maximum);
			maxHeapify(A, maximum, heapSize);
		}
	}
	
	private static void buildMaxHeap(int[] A) {
		for (int i = (A.length / 2); i >= 0; --i) {
			maxHeapify(A, i, A.length);
		}
	}
	
	private static void swap(int[] A, int i, int childIndex) {
		int temp = A[i];
		A[i] = A[childIndex];
		A[childIndex] = temp;
	}
	
	private static int left (int i) {
		return 2 * i;
	}
	
	private static int right (int i) {
		return 2 * i + 1;
	}
}


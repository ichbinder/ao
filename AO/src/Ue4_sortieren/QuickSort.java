package Ue4_sortieren;

public class QuickSort {
private static long comparisonCnt = 0;
	
	public static void sortiere(int x[]) {
		qSort(x, 0, x.length-1);
		Main.quickCnt = comparisonCnt;
		//System.out.println("Quick Sort: ArrayLength: " + x.length + " Comparison Cnt: " + comparisonCnt);
	}

	public static void qSort(int x[], int links, int rechts) {
		comparisonCnt++;
		if (links < rechts) {
			int i = partition(x,links,rechts);
			qSort(x,links,i-1);
			qSort(x,i+1,rechts);
		}
	}

	public static int partition(int x[], int links, int rechts) {
		int pivot, i, j, help;
		pivot = x[rechts];               
		i     = links;
		j     = rechts-1;
		while(i<=j) {
			
			if (x[i] > pivot) {     
				help = x[i]; 
				x[i] = x[j]; 
				x[j] = help;                             
				j--;
			} 
			else i++;
			comparisonCnt += 2;
			
		}
		help      = x[i];
		x[i]      = x[rechts];
		x[rechts] = help;

		return i;
	}
}

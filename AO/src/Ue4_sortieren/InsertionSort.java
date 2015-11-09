package Ue4_sortieren;

public class InsertionSort {

		public static int[] insertionSort(int[] sortieren) {
			int temp;
			long comparisonCnt = 0;
			for (int i = 1; i < sortieren.length; i++) {
				temp = sortieren[i];
				int j = i;
				while (j > 0 && sortieren[j - 1] > temp) {
					sortieren[j] = sortieren[j - 1];
					j--;
					comparisonCnt++;
				}
				comparisonCnt++;
				sortieren[j] = temp;
			}
			Main.insertionCnt = comparisonCnt;
			//System.out.println("Insertion Sort: ArrayLength: " + sortieren.length + " Comparison Cnt: " + comparisonCnt);
			return sortieren;
		}
}

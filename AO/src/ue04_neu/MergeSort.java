package ue04_neu;

public class MergeSort {
	private int[] array;
    private int[] tempMergArr;
    private int length;
	private long comparisonCnt = 0;
    
	public void sort(int inputArr[]) {
        this.array = inputArr;
        this.length = inputArr.length;
        this.tempMergArr = new int[length];
        doMergeSort(0, length - 1);
        Main.mergeCnt = comparisonCnt;
        //System.out.println("Merge Sort: ArrayLength: " + inputArr.length + " Comparison Cnt: " + comparisonCnt);
    }
 
    private void doMergeSort(int lowerIndex, int higherIndex) {
        comparisonCnt++; 
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            doMergeSort(lowerIndex, middle);
            doMergeSort(middle + 1, higherIndex);
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }
 
    private void mergeParts(int lowerIndex, int middle, int higherIndex) {
    	
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = array[i];
            comparisonCnt++;
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
        	if(i <= middle)
        		comparisonCnt++;
        	else
        		comparisonCnt += 2;
            if (tempMergArr[i] <= tempMergArr[j]) {
                array[k] = tempMergArr[i];
                i++;
            } else {
                array[k] = tempMergArr[j];
                j++;
            }
            comparisonCnt++;
            k++;
        }
        comparisonCnt++;
        while (i <= middle) {
        	comparisonCnt++;
            array[k] = tempMergArr[i];
            k++;
            i++;
        }
        comparisonCnt++;
 
    }
}

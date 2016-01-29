package ue04_neu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class UnsortListGenerator {

	private int start;
	private int end;
	private int maxLegth;
	private ArrayList<Integer> unsortList;
	
	public UnsortListGenerator(int start, int end, int maxLegth) {
		if (start < end) {
			this.start = start;
			this.end = end;
		} else throw new IllegalArgumentException("start musst giger then end!");
		
		if (maxLegth > 2)
			this.maxLegth = maxLegth;
		else throw new IllegalArgumentException("maxLegth musst larger then 2!");
		
		this.unsortList = new ArrayList<Integer>();
	}
	
	public void generate() {
		Random random = new Random();
		for (int i = 0; i < this.maxLegth; i++) {
			unsortList.add(random.nextInt(this.end - this.start + 1) + this.start);
		}
	}
	
	public ArrayList<Integer> getUnsortetList() {
		return this.unsortList;
	}
	
//	public static void main(String[] args) throws IOException {
//		UnsortListGenerator uListGenerator = new UnsortListGenerator(0, 10000, 1000000);
//		uListGenerator.generate();
////		System.out.println(uListGenerator.getUnsortetList());
//		System.out.println(uListGenerator.getUnsortetList().size());
//	}
	
}

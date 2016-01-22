package ue10;

import java.nio.charset.Charset;
import java.util.Random;

import sun.security.util.Length;

public class LCS {
	private int c[][] = {};
	private String b[][] = {};

	public void lengthLCS(String X[], String Y[]) {
		int m = X.length;
		int n = Y.length;		
		
//		seien b[1..m, 1..n] und c[0..m, 0..n] neue Tabellen

		c = new int[m][n];
		b = new String[m][n];
		
//		float c[][] = {};
//		String b[][] = {}; 
		for (int i = 0; i < m; i++)	c[i][0] = 0;
		for (int j = 0; j < n; j++)	c[0][j] = 0;

		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (X[i] == Y[j]) {
					c[i][j] = c[i-1][j-1] + 1;
					b[i][j] = "links oben";
				} else if (c[i-1][j] >= c[i][j-1]) {
					c[i][j] = c[i-1][j];
					b[i][j] = "oben";
				} else {
					c[i][j] = c[i][j-1];
					b[i][j] = "links";
				}
			}
		}	
	}
	
	public float printLCS(String b[][], float X[], int i, int j) {
		if (i == 0 || j == 0)
			return 0.0f;
		if (b[i][j] == "links oben"){
			printLCS(b, X, i-1, j-1);
			System.out.println(X[i]);
		} else if (b[i][j] == "oben") {
			printLCS(b, X, i-1, j);
		} else {
			printLCS(b, X, i, j-1);
		}
		return 0.0f;
	}
	
	public String[][] createChains(String elements, int maxChars, int maxX, int maxY){
		
		String [][] chains = new String [maxY][maxX];
		Random randomizer = new Random();
		for(int y = 0; y < maxY; y++){
			
			for(int x = 0; x < maxX; x++){
				
				chains[y][x] = randomString(elements, randomizer, maxChars);
			}
		}
		return chains;
	}
	
	public String randomString(String elements, Random random, int maxLength){
				
		String randomString = "";
		int maxChars = random.nextInt(maxLength);
		
		for(int c = 0; c < maxChars; c++){			
			randomString += elements.charAt(random.nextInt(elements.length()-1));
		}		
		return randomString;
	}
	
	private String removeDoubles(String segment){
		
		String removed = "";
		for(int i = 0; i < segment.length(); i++){
			
			if(! removed.contains(segment.substring(i, i))){
				removed += segment.charAt(i);
			}
		}				
		return removed;
	}
	
	public String [] createSegments(String segment){

		String [] segments = new String[segment.length()];
		
		for(int i = 0; i < segments.length; i++){
			
			segments[i] = segment.substring(i,i+1);
		}
		return segments;
	}
	
	
	public static void main(String[] args) {
		
		LCS lcs = new LCS();				
		
//		lcs.b = lcs.createChains("ACGT", 12, 10, 10);		
//		lcs.c = new float[10][10];		
		
		String [] X = lcs.createSegments("ACGCTAC");
		String [] Y = lcs.createSegments("CTAC");		
		lcs.lengthLCS(X, Y);		
	}
}

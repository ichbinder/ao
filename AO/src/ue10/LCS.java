package ue10;

public class LCS {
	private int c[][] = {};
	private String b[][] = {};
		
	public void lengthLCS(String X[], String Y[]) {
		int m = X.length;
		int n = Y.length;		

		c = new int[m][n];
		b = new String[m][n];		
		
		for (int i = 1; i < m; i++)	c[i][0] = 0;
		for (int j = 1; j < n; j++)	c[0][j] = 0;

		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (X[i].equals(Y[j])) {
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
	
	public void printLCS(String b[][], String X[], int i, int j) {
		if (i == 0 || j == 0) return;
		if (b[i][j] == "links oben"){
			printLCS(b, X, i-1, j-1);
			System.out.println(X[i]);
		} else if (b[i][j] == "oben") {
			printLCS(b, X, i-1, j);
		} else {
			printLCS(b, X, i, j-1);
		}
	}
	
	
	public String [] createSegments(String segment){

		segment = " "+segment;
		String [] segments = new String[segment.length()];
		
		for(int i = 0; i < segments.length; i++){
			
			segments[i] = segment.substring(i,i+1);
		}
		return segments;
	}
	
	/**Gibt eine LCS von zwei verglichenen Sequenzen als Tabelle zurück.*/
	public void createTable(String [] X, String [] Y){

		int x = X.length;
		int y = Y.length;

		for(int h = 0; h < y; h++){
			
			System.out.print( "  | " + Y[h] + " |");			
		}
		System.out.println();
		
		for(int i = 0; i < x; i++){
			
			System.out.print( X[i] );			
			for(int j = 0; j < y; j++){
				System.out.print( " | " + this.c[i][j] + " | ");
			}
			System.out.println();
		}		
		
		System.out.println();
		System.out.println();
	}
	
	public static void main(String[] args) {
		
		LCS lcs = new LCS();
		
		String [] X = lcs.createSegments("ACGCTAC");		
		String [] Y = lcs.createSegments("CTAC");
				
		//String [] X = lcs.createSegments("ABCB");	
		//String [] Y = lcs.createSegments("BDCAB");
		
		lcs.lengthLCS(X, Y);
		lcs.createTable(X, Y);

		System.out.println("LCS Ergebnis:\n");
		lcs.printLCS(lcs.b, X, X.length -1, Y.length -1);		
	}
}

package ue10;

public class LCS {
	private float c[][] = {};
	private String b[][] = {};

	public void lengthLCS(float X[], float Y[]) {
		int m = X.length;
		int n = Y.length;
//		float c[][] = {};
//		String b[][] = {};
		for (int i = 0; i < m; i++)
			c[i][0] = 0;
		for (int j = 0; j < n; j++)
			c[0][j] = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
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

	public float[][] getC() {
		return c;
	}

	public String[][] getB() {
		return b;
	}
}

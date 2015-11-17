package ue6;

import java.util.Random;

public class RabinMiller {

	public static void main(String[] args) {
		
	}
	
	public RabinMiller(int n) {
		if (n < 3)
			throw new IllegalArgumentException();	
		calc(n);
	}
	
	private boolean calc(int n) {
		int s = n - 1;
		int t = 0;
		while ((s % 2) == 0) {
			s = s / 2;
			t = t + 1;
		}
		
		int k = 0;
		
		while (k < 128) {
			Random rand = new Random();
			int a = rand.nextInt(((n-1) - 2) + 1) + 2;
			double v = Math.pow(a, 5) % n;
			if (v != 1) {
				int i = 0;
				while (v != (n - 1)) {
					if (i == (t - 1)) {
						return false;
					} else {
						v = Math.pow(v, 2) % n;
						i = i + 1;
					}
				}
			}
			k = k + 2;
		}
		return true;
	}
}

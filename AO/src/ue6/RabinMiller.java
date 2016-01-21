package ue6;

import java.math.BigInteger;
import java.util.Random;

public class RabinMiller {

	String sAccepted = "";
	int foundAccepted = 0;
	
	public static void main(String[] args) {
		
		RabinMiller rm = new RabinMiller(5);
	}
	
	public RabinMiller(int n) {
		if (n < 3)
			throw new IllegalArgumentException();
		
		int maxValue = 1000000;
		System.out.println("Check range from: " + n + " to " + maxValue);

		for(int i = n; i < maxValue; i+=2){
			
			boolean isPrime = calc(i);
//			System.out.println("R:" + i + ":" +isPrime);
			if(isPrime){
				foundAccepted++;
				sAccepted += i +";";				

				System.out.print(foundAccepted + ":");
				System.out.print(i + "|");				
				BigInteger big = new BigInteger("" +i);
				System.out.println("IsPrimeNumber:" + big.isProbablePrime(1));
				
			}
		}
		System.out.println("Found Accepted Numbers: " + foundAccepted);
//		System.out.println("Accepted Integers: " + sAccepted);

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

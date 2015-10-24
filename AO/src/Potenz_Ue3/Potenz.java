package Potenz_Ue3;

public class Potenz {

	public static int cnt = 0;

	public static void main(String[] args) {
		// TODO Auto-generated constructor stub
		int x = 3;
		int n = 0;
		long ergebnis;
//		if ((n & 1) == 1) {
//			System.out.println("jau");
//		}
		final long timeStartPotenzierenBit = System.nanoTime();
		for (int i = 0; i < 34; ++i) {
			cnt = 0;
			ergebnis = potenzierenBit(x, n + i);
			System.out.println("potenzierenBit: " + x + "^" + (n + i) + "=" + ergebnis + " Cnt = " + cnt);
		}
		final long timeStopPotenzierenBit = System.nanoTime();
		final long timeStartPotenzierenMod = System.nanoTime();
		for (int i = 0; i < 34; ++i) {
			cnt = 0;
			ergebnis = potenzierenMod(x, n + i);
			System.out.println("potenzierenMod: " + x + "^" + (n + i) + "=" + ergebnis + " Cnt = " + cnt);
		}
		final long timeStopPotenzierenMod = System.nanoTime();
		System.out.println("potenzierenBit Time: " + (timeStopPotenzierenBit - timeStartPotenzierenBit) + " Nanosek.");
		System.out.println("potenzierenMod Time: " + (timeStopPotenzierenMod - timeStartPotenzierenMod) + " Nanosek.");
	}

	public static long potenzierenBit(long x, long n) {
		// TODO Auto-generated method stub
		if (n == 0)
			return 1;
		if (n == 1)
			return x;
		else if ((n & 1) != 1) {
			long ergebnis = potenzierenBit(x, n / 2);
			cnt++;
			return ergebnis * ergebnis;
		} else {
			long ergebnis = potenzierenBit(x, (n - 1) / 2);
			cnt += 2;
			return ergebnis * ergebnis * x;
		}

	}
	
	public static long potenzierenMod(long x, long n) {
		// TODO Auto-generated method stub
		if (n == 0)
			return 1;
		if (n == 1)
			return x;
		else if (n % 2 == 0) {
			long ergebnis = potenzierenMod(x, n / 2);
			cnt++;
			return ergebnis * ergebnis;
		} else {
			long ergebnis = potenzierenMod(x, (n - 1) / 2);
			cnt += 2;
			return ergebnis * ergebnis * x;
		}

	}

//	public static long bin_expo(long x, long n) {
//		long res = 1;
//		long aktpot = x;
//		while (n > 0) {
//			if ((n % 2) == 1) {
//				cnt++;
//				res = res * aktpot;
//			}
//			cnt++;
//			aktpot = aktpot * aktpot;
//			n = n / 2;
//		}
//		return res;
//	}

}

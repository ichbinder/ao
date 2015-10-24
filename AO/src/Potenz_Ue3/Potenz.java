package Potenz_Ue3;

public class Potenz {

	public static int cnt = 0;

	public static void main(String[] args) {
		// TODO Auto-generated constructor stub

		int x = 2;
		int n = 0;
		long ergebnis;

		for (int i = 0; i < 34; ++i) {
			cnt = 0;
			ergebnis = potenzieren(x, n + i);
			System.out.println("\t  " + x + "^" + (n + i) + "=" + ergebnis + " Cnt = " + cnt);

			cnt = 0;
			ergebnis = potenzieren(x, n + i);
			System.out.println("Bin_Expo:" + x + "^" + (n + i) + "=" + ergebnis + "cnt =" + cnt);
		}
	}

	public static long potenzieren(long x, long n) {
		// TODO Auto-generated method stub
		if (n == 0)
			return 1;
		if (n == 1)
			return x;
		else if (n % 2 == 0) {
			long ergebnis = potenzieren(x, n / 2);
			cnt++;
			return ergebnis * ergebnis;
		} else {
			long ergebnis = potenzieren(x, (n - 1) / 2);
			cnt += 2;
			return ergebnis * ergebnis * x;
		}

	}

	public static long bin_expo(long x, long n) {
		long res = 1;
		long aktpot = x;
		while (n > 0) {
			if ((n % 2) == 1) {
				cnt++;
				res = res * aktpot;
			}
			cnt++;
			aktpot = aktpot * aktpot;
			n = n / 2;
		}
		return res;
	}

}

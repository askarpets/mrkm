package lab2.tests;

import lab2.Const;

import java.math.BigInteger;
import java.util.Random;

public class PrimalityTests {
    /**
     * @param number - number which is tested
     * @param certainty -
     * @return true - if number is prime, otherwise - false
     */
    public static boolean millerRabinTest(BigInteger number, int certainty) {
        Object[] factors = factorization(number.subtract(BigInteger.ONE));
        BigInteger x;
        Random random = new Random();

        for (int i = 0; i < certainty; i++) {
            x = new BigInteger(number.bitLength() - 1, random).add(BigInteger.ONE);
            BigInteger xr = x.modPow((BigInteger) factors[0], number);

            if (!number.gcd(x).equals(BigInteger.ONE)) {
                return false;
            } else if (xr.equals(BigInteger.ONE) || xr.equals(number.subtract(BigInteger.ONE))) {
                return true;
            } else {
                for (int j = 1; j < (int) factors[1] - 1; j++) {
                    xr = xr.multiply(xr).mod(number);
                    if (xr.equals(BigInteger.ONE))
                        return true;
                    if (xr.equals(number.subtract(BigInteger.ONE)))
                        return false;
                }
            }
        }

        return false;
    }

    /**
     * Gets factorization of p-1 = d * 2 ^ s
     * @param number = p - 1
     * @return array of [d, s]
     */
    private static Object[] factorization(BigInteger number) {
        Object[] factors = new Object[2];
        int s = 0;

        while (number.mod(Const.TWO).equals(BigInteger.ZERO)) {
            s++;
            number = number.divide(Const.TWO);
        }
        factors[0] = number;
        factors[1] = s;

        return factors;
    }
}

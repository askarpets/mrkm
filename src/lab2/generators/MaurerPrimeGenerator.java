package lab2.generators;

import java.math.BigInteger;
import java.util.Random;

import static lab2.Const.TWO;

public class MaurerPrimeGenerator {
    public BigInteger nextProbablePrime(int bitLength, int certainty, int M) {
        Random random = new Random();

        /**
         * If bitLength less than 20 bits, get random numbers with 20 bits length and check if it is prime.
         * If number is prime, return it
         */
        if (bitLength <= 20) {
            BigInteger number;
            do {
                number = new BigInteger(bitLength, random);
            } while (!number.isProbablePrime(certainty));
            return number;
        }

        double r;
        if (bitLength > 2 * M) {
            do {
                double s = random.nextDouble();
                r = Math.pow(2, s - 1);
            } while (bitLength - bitLength * r <= M);
        } else {
            r = 0.5;
        }

        /**
         * Usage of before generated numbers
         */
        BigInteger q = nextProbablePrime((int) Math.floor(r * bitLength), certainty, M);

        /**
         * I = 2 ^ (bitLength - 1) / 2 * q
         */
        BigInteger I = TWO.pow(bitLength - 1);
        BigInteger temp = TWO.multiply(q);
        I = I.divide(temp);

        boolean flag = false;

        BigInteger number = null;

        while (!flag) {

            BigInteger R;

            do {
                /**
                 * get R ∈ [I, 2I]
                 */
                R = getRandomLessThanBound(I, random);
                R = R.add(I);

                /**
                 * number = 2Rq + 1
                 */
                number = TWO.multiply(R).multiply(q).add(BigInteger.ONE);
            } while (!number.isProbablePrime(certainty));

            /**
             * a ∈ [2, number − 2]
             */
            BigInteger a = getRandomLessThanBound(number.subtract(TWO), random);

            /**
             * b = a ^ (number − 1) (mod number)
             */
            BigInteger b = a.modPow(number.subtract(BigInteger.ONE), number);

            if (b.compareTo(BigInteger.ONE) == 0) {
                b = TWO.modPow(TWO.multiply(R), number);
                BigInteger d = number.gcd(b.subtract(BigInteger.ONE));
                if (d.compareTo(BigInteger.ONE) == 0) {
                    flag = true;
                }
            }
        }

        return number;
    }

    private static BigInteger getRandomLessThanBound(BigInteger bound, Random random) {
        BigInteger randomNumber;

        do {
            randomNumber = new BigInteger(bound.bitLength(), random);
        } while (randomNumber.compareTo(bound) != -1);

        return randomNumber;
    }
}

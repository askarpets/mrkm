package lab1;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class TestCase {
    public static void main(String[] args) {
        int testCount = 50;
        testAddition(testCount);
        testSubtraction(testCount);
        testMultiplication(testCount);
        testDivision(testCount);
        testPower(testCount);
        testBarrett(testCount);
    }

    static void testAddition(int testCount) {
        System.out.println("Testing of addition algorithm using Big Integer:");
        int failures = 0;
        BigInt a1, a2;
        BigInteger t1, t2;

        for (int i = 20; i < testCount + 20; i++) {
            a1 = new BigInt(i);
            a2 = new BigInt(i);
            a1.setRandom();
            a2.setRandom();
            t1 = new BigInteger(a1.toHexString(), 16);
            t2 = new BigInteger(a2.toHexString(), 16);

            if (!a1.add(a2).toHexString().equals(t1.add(t2).toString(16))) {
                failures++;
            }
        }
        printTestResult(testCount, failures);
    }

    static void testSubtraction(int testCount) {
        System.out.println("Testing of subtraction algorithm using Big Integer:");
        int failures = 0, missed = 0;
        BigInt a1, a2;
        BigInteger t1, t2;

        for (int i = 20; i < testCount + 20; i++) {
            a1 = new BigInt(i + 5);
            a2 = new BigInt(i);
            a1.setRandom();
            a2.setRandom();
            t1 = new BigInteger(a1.toHexString(), 16);
            t2 = new BigInteger(a2.toHexString(), 16);

            if (a1.subtract(a2) == null) {
                missed++;
                continue;
            }

            if (!a1.subtract(a2).toHexString().equals(t1.subtract(t2).toString(16))) {
                failures++;
            }
        }
        printTestResult(testCount - missed, failures);
    }

    static void testMultiplication(int testCount) {
        System.out.println("Testing of multiplication algorithm using Big Integer:");
        int failures = 0;
        BigInt a1, a2;
        BigInteger t1, t2;

        for (int i = 20; i < testCount + 20; i++) {
            a1 = new BigInt(i);
            a2 = new BigInt(i + 10);
            a1.setRandom();
            a2.setRandom();
            t1 = new BigInteger(a1.toHexString(), 16);
            t2 = new BigInteger(a2.toHexString(), 16);

            if (!a1.multiply(a2).toHexString().equals(t1.multiply(t2).toString(16))) {
                failures++;
            }
        }
        printTestResult(testCount, failures);
    }

    static void testDivision(int testCount) {
        System.out.println("Testing of division algorithm using Big Integer:");
        int failures = 0;
        BigInt a1, a2;
        BigInteger t1, t2;

        for (int i = 20; i < testCount + 20; i++) {
            a1 = new BigInt(i);
            a2 = new BigInt(i + 10);
            a1.setRandom();
            a2.setRandom();
            t1 = new BigInteger(a1.toHexString(), 16);
            t2 = new BigInteger(a2.toHexString(), 16);

            if (!a1.divide(a2).toHexString().equals(t1.divide(t2).toString(16))) {
                failures++;
            }
        }
        printTestResult(testCount, failures);
    }

    static void testPower(int testCount) {
        System.out.println("Testing of power algorithm using Big Integer:");
        int failures = 0, exp;
        BigInt a1, a2;
        BigInteger t1;

        for (int i = 20; i < testCount + 20; i++) {
            exp = i;
            a1 = new BigInt(i);
            a2 = new BigInt(Integer.toHexString(exp));

            a1.setRandom();
            t1 = new BigInteger(a1.toHexString(), 16);

            if (!a1.pow(a2).toHexString().equals(t1.pow(exp).toString(16))) {
                failures++;
            }
        }
        printTestResult(testCount, failures);
    }

    static void testBarrett(int testCount) {
        System.out.println("Testing of Barrett algorithm using Big Integer:");
        int failures = 0;
        BigInt a1, a2;
        BigInteger t1, t2;

        for (int i = 20; i < testCount + 20; i++) {
            a1 = new BigInt(i * 2);
            a2 = new BigInt(i);
            a1.setRandom();
            a2.setRandom();
            t1 = new BigInteger(a1.toHexString(), 16);
            t2 = new BigInteger(a2.toHexString(), 16);

            if (!a1.mod(a2).toHexString().equals(t1.mod(t2).toString(16))) {
                failures++;
            }
        }
        printTestResult(testCount, failures);
    }

    static void printTestResult(int testCount, int failures) {
        DecimalFormat format = new DecimalFormat("#.####");
        format.setRoundingMode(RoundingMode.CEILING);
        System.out.println("Test completed:\n" + testCount + " tests, " + failures + " (" + format.format((100. * failures / testCount)) + "%) failed.");
    }
}

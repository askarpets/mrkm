package lab2.tests;

import lab2.generators.MaurerPrimeGenerator;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class TestCase {
    public static void main(String[] args) {
        MaurerPrimeGenerator maurerPrimeGenerator = new MaurerPrimeGenerator();
        testMR(200, 50);
        testMaurer(maurerPrimeGenerator, 200, 50);

    }

    static void testMR(int testCount, int certainty) {
        System.err.println("Testing of Miller Rabin algorithm using BigInteger:");
        Random random = new Random();
        int failures = 0;

        for (int i = 100; i <= testCount + 100; i++) {
            BigInteger temp = new BigInteger(i, random);
            boolean bigIntResult = temp.isProbablePrime(certainty);
            boolean localResult = PrimalityTests.millerRabinTest(temp, certainty);
            if (bigIntResult != localResult) {
                failures++;
            }

        }

        printTestResult(testCount, failures);
    }

    static void testMaurer(MaurerPrimeGenerator generator, int testCount, int certainty) {
        System.err.println("Testing of Maurer algorithm using Miller Rabin algorithm:");
        int failures = 0;

        BigInteger temp;
        for (int i = 100; i < testCount + 100; i++) {
            temp = generator.nextProbablePrime(i, certainty, 20);
            if (!PrimalityTests.millerRabinTest(temp, certainty)) {
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

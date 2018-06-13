package lab3;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;

public class TestCase {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
//        try {
//            testRabinEncrDecr(100, random);
//        } catch (MessageLengthException e) {
//            e.printStackTrace();
//        }
        testRabinSignature(100, random);
    }

    static void testRabinEncrDecr(int testCount, SecureRandom random) throws MessageLengthException {
        System.err.println("Testing of Rabin`s encryption/decryption algorithm:");
        int failures = 0;
        BigInteger originalMessage, decryptedMessage;
        Object[] encryptionData;
        Client client;

        for (int i = 768; i < testCount + 768; i++) {
            client = new Client(i);
            originalMessage = generateMessage(client.getPublicKey(), i, random);
            encryptionData = client.encrypt(originalMessage);
            decryptedMessage = client.decrypt(encryptionData);

            if (originalMessage.compareTo(decryptedMessage) != 0) {
                failures++;
            }
        }

        printTestResult(testCount, failures);
    }

    static void testRabinSignature(int testCount, SecureRandom random) {
        System.err.println("Testing of Rabin`s signature algorithm:");
        int failures = 0;
        BigInteger message;
        Object[] signature;
        Client client;

        for (int i = 768; i < testCount + 768; i++) {
            client = new Client(i);
            message = generateMessage(client.getPublicKey(), i, random);
            signature = client.sign(message);
            if (!Client.verifySignature(signature, client)) {
                failures++;
            }
        }

        printTestResult(testCount, failures);
    }

    static void printTestResult(int testCount, int failures) {
        DecimalFormat format = new DecimalFormat("#.####");
        format.setRoundingMode(RoundingMode.CEILING);
        System.out.println("TestCase completed:\n" + testCount + " tests, " + failures + " (" + format.format((100. * failures / testCount)) + "%) failed.");
    }

    private static BigInteger generateMessage(BigInteger publicKey, int bitCount, Random random) {
        BigInteger result;
        do {
            result = new BigInteger(bitCount + 1, random);
        } while (result.compareTo(publicKey) == -1 && result.compareTo(Client.sqrt(publicKey)) != 1);
        return result;
    }
}

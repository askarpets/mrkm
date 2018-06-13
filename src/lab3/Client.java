package lab3;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Client {
    private BigInteger p, q, publicKey;

    public Client(int keyLength) {
        SecureRandom random = new SecureRandom();
        p = getBlumPrime(keyLength, random);
        do {
            q = getBlumPrime(keyLength, random);
        } while (p.compareTo(q) == 0);
        publicKey = p.multiply(q);
    }

    /**
     * The most interesting things are here
     */

    public Object[] encrypt(BigInteger message) throws MessageLengthException {
        checkMessageLength(message);
        Object[] encryptionData = new Object[3];
        encryptionData[0] = message.modPow(Const.TWO, publicKey);
        encryptionData[1] = numberEven(message) ? 0 : 1;
        encryptionData[2] = jacobiSymbol(message, publicKey);

        return encryptionData;
    }

    public BigInteger decrypt(Object[] encryptionData) {
        BigInteger cipherText = (BigInteger) encryptionData[0];
        int firstAuthParam = (int) encryptionData[1], secondAuthParam = (int) encryptionData[2];
        BigInteger[] roots = getSquareRoots(cipherText);
        BigInteger message;

        message = (roots[0].multiply(q).multiply(q.modInverse(p))).add(roots[2].multiply(p).multiply(p.modInverse(q))).mod(publicKey);
        if (messageValid(message, firstAuthParam, secondAuthParam)) {
            return message;
        }

        message = (roots[0].multiply(q).multiply(q.modInverse(p))).add(roots[3].multiply(p).multiply(p.modInverse(q))).mod(publicKey);
        if (messageValid(message, firstAuthParam, secondAuthParam)) {
            return message;
        }

        message = (roots[1].multiply(q).multiply(q.modInverse(p))).add(roots[2].multiply(p).multiply(p.modInverse(q))).mod(publicKey);
        if (messageValid(message, firstAuthParam, secondAuthParam)) {
            return message;
        }

        message = (roots[1].multiply(q).multiply(q.modInverse(p))).add(roots[3].multiply(p).multiply(p.modInverse(q))).mod(publicKey);
        if (messageValid(message, firstAuthParam, secondAuthParam)) {
            return message;
        }

        return null;
    }

    public Object[] sign(BigInteger message) {
        Object[] signature = new Object[3];
        Sha256 sha256 = Sha256.getInstance();
        BigInteger hash;
        String sequence;

        do {
            sequence = getRandomSequence(1024);
            hash = new BigInteger(Sha256.bytesToHex(sha256.digest((new StringBuilder(message.toString(16)).append(sequence)).toString().getBytes())), 16).mod(publicKey);
        } while (!isQuadraticResidue(hash));

        BigInteger[] roots = getSquareRoots(hash);
        BigInteger finalRoot = (roots[0].multiply(q).multiply(q.modInverse(p))).add(roots[2].multiply(p).multiply(p.modInverse(q))).mod(publicKey);
        signature[0] = message;
        signature[1] = sequence;
        signature[2] = finalRoot;

        return signature;
    }

    public static boolean verifySignature(Object[] signature, Client client) {
        Sha256 sha256 = Sha256.getInstance();
        String hash = Sha256.bytesToHex(sha256.digest((new StringBuilder(((BigInteger) signature[0]).toString(16)).append((String) signature[1]).toString().getBytes())));
        BigInteger root = (BigInteger) signature[2];

        return root.pow(2).mod(client.getPublicKey()).compareTo(new BigInteger(hash, 16).mod(client.getPublicKey())) == 0;
    }


    /**
     * Insignificant bullshit :)
     */

    private boolean isQuadraticResidue(BigInteger value) {
        return value.modPow(p.subtract(BigInteger.ONE).divide(Const.TWO), p).compareTo(BigInteger.ONE) == 0 &&
                value.modPow(q.subtract(BigInteger.ONE).divide(Const.TWO), q).compareTo(BigInteger.ONE) == 0;
    }

    private String getRandomSequence(int length) {
        StringBuilder sequence = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sequence.append(random.nextInt(2));
        }

        return sequence.toString();
    }

    private void checkMessageLength(BigInteger message) throws MessageLengthException {
        if (message.compareTo(publicKey) == 1 || message.compareTo(sqrt(publicKey)) == -1) {
            throw new MessageLengthException("Incorrect length of message");
        }
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    private static BigInteger getBlumPrime(int bitLength, SecureRandom random) {
        BigInteger prime;

        do {
            prime = new BigInteger(bitLength - 1, random);
            prime = prime.multiply(Const.FOUR).add(Const.THREE);
        } while (!prime.isProbablePrime(50));

        return prime;
    }

    private boolean messageValid(BigInteger message, int firstAuthParam, int secondAuthParam) {
        int temp;
        if (numberEven(message)) {
            temp = 0;
        } else {
            temp = 1;
        }
        int jacobi = jacobiSymbol(message, publicKey);
        return temp == firstAuthParam && jacobi == secondAuthParam;
    }

    private static boolean numberEven(BigInteger number) {
        return number.getLowestSetBit() != 0;
    }

    private BigInteger[] getSquareRoots(BigInteger cipher) {
        BigInteger[] roots = new BigInteger[4];

        roots[0] = cipher.modPow(p.add(BigInteger.ONE).divide(Const.FOUR), p);
        roots[1] = roots[0].negate();
        roots[2] = cipher.modPow(q.add(BigInteger.ONE).divide(Const.FOUR), q);
        roots[3] = roots[2].negate();

        return roots;
    }

    /**
     * Calculates (a/n)
     *
     * @param a - first argument of Jacobi comparison
     * @param n - second argument of Jacobi comparison
     * @return 1, 0 or -1
     */
    private static int jacobiSymbol(BigInteger a, BigInteger n) {
        int result = 0;

        if (a.compareTo(BigInteger.ZERO) == 0)
            result = (n.compareTo(BigInteger.ONE) == 0) ? 1 : 0;
        else if (a.compareTo(Const.TWO) == 0) {

            BigInteger temp = n.mod(Const.EIGHT);
            if (temp.compareTo(BigInteger.ONE) == 0 || temp.compareTo(Const.SEVEN) == 0) {
                result = 1;
            }

            if (temp.compareTo(Const.THREE) == 0 || temp.compareTo(Const.FIVE) == 0) {
                result = -1;
            }

        } else if (a.compareTo(n) != -1)
            result = jacobiSymbol(a.mod(n), n);
        else if (a.mod(Const.TWO).compareTo(BigInteger.ZERO) == 0)
            result = jacobiSymbol(Const.TWO, n) * jacobiSymbol(a.divide(Const.TWO), n);
        else
            result = (a.mod(Const.FOUR).compareTo(Const.THREE) == 0 && n.mod(Const.FOUR).compareTo(Const.THREE) == 0) ? -jacobiSymbol(n, a) : jacobiSymbol(n, a);
        return result;
    }

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
        while (b.compareTo(a) >= 0) {
            BigInteger mid = a.add(b).shiftRight(1);
            if (mid.multiply(mid).compareTo(n) > 0) {
                b = mid.subtract(BigInteger.ONE);
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }
}

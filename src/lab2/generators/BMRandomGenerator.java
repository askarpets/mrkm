package lab2.generators;

import lab2.Const;

import java.math.BigInteger;
import java.util.Random;

public class BMRandomGenerator {
    private int byteLength;
    private BigInteger initial;

    public BMRandomGenerator(int bitLength, BigInteger initial) {
        this.byteLength = bitLength / 8;
        this.initial = initial;
    }

    public BMRandomGenerator(int bitLength) {
        this.byteLength = bitLength / 8;

        Random random = new Random();
        this.initial = new BigInteger(bitLength, random);
    }

    public BMRandomGenerator() {
        this.byteLength = 1024 / 8;

        Random random = new Random();
        this.initial = new BigInteger(byteLength, random);
    }

    public int[] nextRandom() {
        int[] byteSequence = new int[byteLength];
        int nextByte;

        for (int i = 0; i < byteLength; i++) {
            nextByte = 0;
            for (int j = 0; j < 8; j++) {
                if (initial.max(Const.BM_TRESHOLD).equals(Const.BM_TRESHOLD)) {
                    nextByte += (1 << j);
                }

                initial = Const.ALPHA.modPow(initial, Const.P);
            }
            byteSequence[i] = (nextByte);
        }

        return byteSequence;
    }
}
package lab2;

import java.math.BigInteger;

public interface Const {
    /**
     * Big prime number
     */
    BigInteger P = new BigInteger("0CEA42B987C44FA642D80AD9F51F10457690DEF10C83D0BC1BCEE12FC3B6093E3", 16);

    /**
     * Primitive
     */
    BigInteger ALPHA = new BigInteger("5B88C41246790891C095E2878880342E88C79974303BD0400B090FE38A688356", 16);

    /**
     * Numbers used for algorithms
     */
    BigInteger TWO = new BigInteger("2", 10);
    BigInteger THREE = new BigInteger("3", 10);
    BigInteger FOUR = new BigInteger("4", 10);

    /**
     * Value used for comparison in BM generator
     */
    BigInteger BM_TRESHOLD = P.subtract(BigInteger.ONE).divide(TWO);
}

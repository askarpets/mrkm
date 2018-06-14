package lab1;

import java.util.ArrayList;
import java.util.Random;

public class BigInt {
    public static final long BASE = 32L;
    public static final long MASK = 4294967295L;

    private ArrayList<Long> number;

    public BigInt(ArrayList<Long> number) {
        this.number = new ArrayList<>();
        this.number.addAll(number);
    }

    public BigInt(long[] number) {
        this.number = new ArrayList<>();
        for (int i = 0; i < number.length; i++) {
            this.number.add(number[i]);
        }
    }

    public BigInt(int size) {
        this.number = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.number.add(0L);
        }
    }

    public BigInt clone() {
        return new BigInt(this.number);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BigInt)) {
            return super.equals(obj);
        }

        this.removeLeadingZeros();
        ((BigInt) obj).removeLeadingZeros();

        if (this.number.size() != ((BigInt) obj).number.size()) {
            return false;
        }

        for (int i = 0; i < this.number.size(); i++) {
            if (this.number.get(i) != ((BigInt) obj).number.get(i)) {
                return false;
            }
        }

        return true;
    }

    public int compareTo(BigInt number) {
        if (this.equals(number)) {
            return 0;
        }

        this.removeLeadingZeros();
        number.removeLeadingZeros();

        if (this.number.size() > number.number.size()) {
            return 1;
        } else {
            return -1;
        }
    }

    public BigInt add(BigInt number) {
        BigInt result;
        long carry = 0, temp;
        int lengthDifference;

        if (this.number.size() > number.number.size()) {
            result = new BigInt(this.number.size());
            lengthDifference = this.number.size() - number.number.size();

            for (int i = number.number.size() - 1; i >= 0; i--) {
                temp = this.number.get(lengthDifference + i) + number.number.get(i) + carry;
                result.number.set(lengthDifference + i, temp & MASK);
                carry = temp >> BASE;
            }
            for (int i = lengthDifference - 1; i >= 0; i--) {
                temp = this.number.get(i) + carry;
                result.number.set(i, temp & MASK);
                carry = temp >> BASE;
            }
        } else {
            result = new BigInt(number.number.size());
            lengthDifference = number.number.size() - this.number.size();

            for (int i = this.number.size() - 1; i >= 0; i--) {
                temp = number.number.get(lengthDifference + i) + this.number.get(i) + carry;
                result.number.set(lengthDifference + i, temp & MASK);
                carry = temp >> BASE;
            }
            for (int i = lengthDifference - 1; i >= 0; i--) {
                temp = number.number.get(i) + carry;
                result.number.set(i, temp & MASK);
                carry = temp >> BASE;
            }
        }

        while (carry != 0) {
            result.number.add(0, carry & MASK);
            carry = carry >> BASE;
        }

        result.removeLeadingZeros();
        return result;
    }

    private void removeLeadingZeros() {
        while (this.number.get(0) == 0L && this.number.size() > 1) {
            this.number.remove(0);
        }
    }

    public void setRandom() {
        Random random = new Random();
        for (int i = 0; i < number.size(); i++) {
            number.set(i, random.nextLong() & MASK);
        }
    }

    public String toHexString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.number.size(); i++) {
            stringBuilder.append(Long.toHexString(this.number.get(i)));
        }
        return stringBuilder.toString();
    }
}

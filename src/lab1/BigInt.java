package lab1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class BigInt {
    public static final int BASE = 4;
    public static final int MASK = 15;

    private ArrayList<Integer> number;

    /**
     * Constructors
     */
    public BigInt(ArrayList<Integer> number) {
        this.number = new ArrayList<>();
        this.number.addAll(number);
    }

    public BigInt(int[] number) {
        this.number = new ArrayList<>();
        for (int i = 0; i < number.length; i++) {
            this.number.add((int) number[i]);
        }
    }

    public BigInt(int size) {
        this.number = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.number.add(0);
        }
    }

    public BigInt(String s) {
        this(s.split(""));
    }

    public BigInt(String[] array) {
        this.number = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            this.number.add(Integer.parseInt(array[i], 16));
        }
    }

    /**
     * Override of necessary methods
     */
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
        } else if (this.number.size() < number.number.size()) {
            return -1;
        } else {
            int i = 0;
            while (this.number.get(i) == number.number.get(i)) {
                i++;
                if (i == this.number.size()) {
                    break;
                }
            }
            if (i == this.number.size()) {
                return 0;
            } else if (this.number.get(i) > number.number.get(i)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * Basic arithmetic operations
     */
    public BigInt add(BigInt number) {
        BigInt result;
        int carry = 0, temp, lengthDifference;

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

    public BigInt subtract(BigInt number) {
        if (this.compareTo(number) == -1) {
            return null;
        }
        BigInt result = new BigInt(this.number.size());
        int borrow = 0;

        if (this.number.size() > number.number.size()) {
            number.padWithLeadingZeros(this.number.size());
        }

        for (int i = number.number.size() - 1; i >= 0; i--) {
            int temp = this.number.get(i) - number.number.get(i) - borrow;
            if (temp >= 0) {
                result.number.set(i, temp);
                borrow = 0;
            } else {
                result.number.set(i, MASK + 1 + temp);
                borrow = 1;
            }
        }

        result.removeLeadingZeros();
        return result;
    }

    public BigInt multiply(BigInt number) {
        BigInt result = new BigInt(this.number.size() * 2), temp;

        if (number.number.size() < this.number.size()) {
            for (int i = number.number.size() - 1; i >= 0; i--) {
                temp = this.multiplyByInt(number.number.get(i));
                temp.shiftDigits(number.number.size() - i - 1);
                result = result.add(temp);
            }
        } else {
            for (int i = this.number.size() - 1; i >= 0; i--) {
                temp = number.multiplyByInt(this.number.get(i));
                temp.shiftDigits(this.number.size() - i - 1);
                result = result.add(temp);
            }
        }

        result.removeLeadingZeros();
        return result;
    }

    public BigInt divide(BigInt number) {
        StringBuilder s = new StringBuilder(number.toBinaryString());
        while (s.charAt(0) == '0' && s.length() != 1) {
            s.replace(0, 1, "");
        }
        int k = s.toString().length();
        BigInt r = this.clone(), q = new BigInt(1), c;
        int t;

        while (r.compareTo(number) != -1) {
            s = new StringBuilder(r.toBinaryString());
            while (s.charAt(0) == '0' && s.length() != 1) {
                s.replace(0, 1, "");
            }
            t = s.toString().length();

            c = shiftBitsToHigh(number, t - k);

            if (r.compareTo(c) == -1) {
                t--;
                c = shiftBitsToHigh(number, t - k);
            }

            if (r.compareTo(c) != -1) {
                r = r.subtract(c);
            } else {
                for (int i = 0; i < r.number.size(); i++) {
                    r.number.set(i, 0);
                }
            }
            q = BigInt.setBit(q, t - k, '1');
        }

        q.removeLeadingZeros();
        return q;
    }

    public BigInt pow(BigInt number) {
        BigInt result = new BigInt("1");
        StringBuilder stringBuilder = new StringBuilder(number.toBinaryString());
        while (stringBuilder.charAt(0) == '0' && stringBuilder.length() != 1) {
            stringBuilder.replace(0, 1, "");
        }

        for (int i = stringBuilder.length() - 1; i >= 0; i--) {
            if (stringBuilder.charAt(i) == '1') {
                result = this.multiply(result);
            }
            this.number = this.multiply(this).number;
            this.removeLeadingZeros();
        }
        return result;
    }

    public BigInt mod(BigInt modulo) {
        BigInt result, m, q;
        int k = modulo.number.size();
        m = getM(modulo);

        q = this.clone().removeLastDigits(k - 1);

        if (q.number.size() == 0) {
            q.number.add(0);
        }

        q = q.multiply(m);
        q = q.removeLastDigits(k + 1);

        if (q.number.size() == 0) {
            q.number.add(0);
        }

        result = this.subtract(q.multiply(modulo));

        while (result.compareTo(modulo) != -1) {
            result = result.subtract(modulo);
        }

        result.removeLeadingZeros();
        return result;
    }

    /**
     * Additional useful operations
     */

    private static BigInt getM(BigInt number) {
        String s = Integer.toHexString(2 * number.number.size());
        BigInt m = new BigInt("10").pow(new BigInt(s));
        m = m.divide(number);
        return m;
    }

    private BigInt removeLastDigits(int numberOfDigits) {
        BigInt result = this.clone();
        for (int i = 0; i < numberOfDigits; i++) {
            try {
                result.number.remove(result.number.size() - 1);
            } catch (Exception e) {
                return new BigInt(1);
            }
        }
        return result;
    }

    private void shiftDigits(int position) {
        for (int i = 0; i < position; i++) {
            this.number.add(0);
        }
    }

    private BigInt multiplyByInt(int number) {
        BigInt result = new BigInt(this.number.size() + 1);
        int carry = 0, temp;

        for (int i = this.number.size() - 1; i >= 0; i--) {
            temp = this.number.get(i) * number + carry;
            result.number.set(i + 1, temp & MASK);
            carry = temp >> BASE;
        }
        result.number.set(0, carry);

        result.removeLeadingZeros();
        return result;
    }

    private void padWithLeadingZeros(int neededLength) {
        while (this.number.size() < neededLength) {
            this.number.add(0, 0);
        }
    }

    private void removeLeadingZeros() {
        while (this.number.size() > 1 && this.number.get(0) == 0) {
            this.number.remove(0);
        }
    }

    public void setRandom() {
        Random random = new Random();
        for (int i = 0; i < number.size(); i++) {
            number.set(i, random.nextInt() & MASK);
        }
    }

    public String toHexString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.number.size(); i++) {
            stringBuilder.append(Long.toHexString(this.number.get(i)));
        }
        return stringBuilder.toString();
    }

    public String toBinaryString() {
        StringBuilder s = new StringBuilder();
        for (Integer element : number) {
            switch (element) {
                case 0:
                    s.append("0000");
                    break;
                case 1:
                    s.append("0001");
                    break;
                case 2:
                    s.append("0010");
                    break;
                case 3:
                    s.append("0011");
                    break;
                case 4:
                    s.append("0100");
                    break;
                case 5:
                    s.append("0101");
                    break;
                case 6:
                    s.append("0110");
                    break;
                case 7:
                    s.append("0111");
                    break;
                case 8:
                    s.append("1000");
                    break;
                case 9:
                    s.append("1001");
                    break;
                case 10:
                    s.append("1010");
                    break;
                case 11:
                    s.append("1011");
                    break;
                case 12:
                    s.append("1100");
                    break;
                case 13:
                    s.append("1101");
                    break;
                case 14:
                    s.append("1110");
                    break;
                case 15:
                    s.append("1111");
                    break;
            }
        }
        return s.toString();
    }

    private static BigInt shiftBitsToHigh(BigInt number, int n) {
        StringBuilder s = new StringBuilder();
        s.append(number.toBinaryString());
        for (int i = 0; i < n; i++) {
            s.append(0);
        }
        while ((s.length() % 4) != 0) {
            s.reverse();
            s.append(0);
            s.reverse();
        }
        BigInt temp = new BigInt(s.length() / 4);
        int k = temp.number.size() - 1;
        for (int i = s.length() - 1; i >= 0; i = i - 4) {
            StringBuilder t = new StringBuilder();
            t.append(s.charAt(i - 3));
            t.append(s.charAt(i - 2));
            t.append(s.charAt(i - 1));
            t.append(s.charAt(i));

            switch (t.toString()) {
                case "0000":
                    temp.number.set(k, 0);
                    break;
                case "0001":
                    temp.number.set(k, 1);
                    break;
                case "0010":
                    temp.number.set(k, 2);
                    break;
                case "0011":
                    temp.number.set(k, 3);
                    break;
                case "0100":
                    temp.number.set(k, 4);
                    break;
                case "0101":
                    temp.number.set(k, 5);
                    break;
                case "0110":
                    temp.number.set(k, 6);
                    break;
                case "0111":
                    temp.number.set(k, 7);
                    break;
                case "1000":
                    temp.number.set(k, 8);
                    break;
                case "1001":
                    temp.number.set(k, 9);
                    break;
                case "1010":
                    temp.number.set(k, 10);
                    break;
                case "1011":
                    temp.number.set(k, 11);
                    break;
                case "1100":
                    temp.number.set(k, 12);
                    break;
                case "1101":
                    temp.number.set(k, 13);
                    break;
                case "1110":
                    temp.number.set(k, 14);
                    break;
                case "1111":
                    temp.number.set(k, 15);
                    break;
            }
            k--;
        }
        return temp;
    }

    public static BigInt setBit(BigInt Q, int n, char i) {
        String[] temp = Q.toBinaryString().trim().split("");
        try {
            StringBuilder s = new StringBuilder(Q.toBinaryString());
            while (s.charAt(0) == 0 && s.length() != 1) {
                s.replace(0, 1, "");
            }
            temp[s.toString().length() - n - 1] = Character.toString(i);
        } catch (Exception e) {
            if (n >= 0) {
                String[] r = new String[n + 1];
                for (int j = 0; j < temp.length; j++) {
                    r[n + 1 - temp.length + j] = temp[j];
                }
                for (int j = (n - temp.length); j >= 0; j--) {
                    r[j] = "0";
                }
                r[0] = "1";
                StringBuilder result = new StringBuilder();
                for (int j = 0; j < r.length; j++) {
                    result.append(r[j]);
                }
                return new BigInt(BigInt.binaryToHex(result.toString()));
            }
        }

        StringBuilder result = new StringBuilder();
        for (int j = 0; j < temp.length; j++) {
            result.append(temp[j]);
        }
        return new BigInt(BigInt.binaryToHex(result.toString()));
    }

    public static String binaryToHex(String source) {
        StringBuilder s = new StringBuilder(source);
        while ((s.length() % 4) != 0) {
            s.reverse();
            s.append(0);
            s.reverse();
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i = i + 4) {
            StringBuilder t = new StringBuilder();
            t.append(s.charAt(i));
            t.append(s.charAt(i + 1));
            t.append(s.charAt(i + 2));
            t.append(s.charAt(i + 3));

            switch (t.toString()) {
                case "0000":
                    result.append(0);
                    break;
                case "0001":
                    result.append(1);
                    break;
                case "0010":
                    result.append(2);
                    break;
                case "0011":
                    result.append(3);
                    break;
                case "0100":
                    result.append(4);
                    break;
                case "0101":
                    result.append(5);
                    break;
                case "0110":
                    result.append(6);
                    break;
                case "0111":
                    result.append(7);
                    break;
                case "1000":
                    result.append(8);
                    break;
                case "1001":
                    result.append(9);
                    break;
                case "1010":
                    result.append('A');
                    break;
                case "1011":
                    result.append('B');
                    break;
                case "1100":
                    result.append('C');
                    break;
                case "1101":
                    result.append("D");
                    break;
                case "1110":
                    result.append('E');
                    break;
                case "1111":
                    result.append('F');
                    break;
            }
        }
        return result.toString();
    }
}

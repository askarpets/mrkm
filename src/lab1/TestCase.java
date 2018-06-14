package lab1;

import java.math.BigInteger;

public class TestCase {
    public static void main(String[] args) {
        BigInt a1 = new BigInt(30), a2 = new BigInt(30);
        a1.setRandom();
        a2.setRandom();


        BigInteger t1=new BigInteger(a1.toHexString(), 16), t2=new BigInteger(a2.toHexString(), 16);

        System.out.println(a1.toHexString());
        System.out.println(a2.toHexString());
        System.out.println();

        System.out.println("MY: "+a1.add(a2).toHexString());
        System.out.println("BI: "+t1.add(t2).toString(16));

    }
}

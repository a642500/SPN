package me.toxz.school.encryption;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Carlos on 2015/9/30.
 */
public class PrimalityTest {
    private static final Random random = new Random();

    private static boolean primalityTestPass(BigInteger a, BigInteger n) {

        BigInteger nMinusOne = n.subtract(BigInteger.ONE);
        BigInteger d = nMinusOne;
        int s = d.getLowestSetBit();
        d = d.shiftRight(s);

        BigInteger pow = a.modPow(d, n);
        if (pow.equals(BigInteger.ONE)) return true;
        for (int i = 0; i < s - 1; i++) {

            if (pow.equals(nMinusOne)) return true;
            pow = pow.multiply(pow).mod(n);

        }
        return pow.equals(nMinusOne);

    }


    public static boolean primalityTest(BigInteger n, int repeatTimes) {

        for (int i = 0; i < repeatTimes; i++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), random);
            } while (a.equals(BigInteger.ZERO));
            if (!primalityTestPass(a, n)) {
                return false;
            }
        }
        return true;
    }

}

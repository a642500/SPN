package me.toxz.school.encryption;


import sun.security.util.BitArray;

import java.util.Arrays;

/**
 * Created by Carlos on 2015/9/1.
 */
public class PBox {
    private final int[] pi;
    private final int[] inversePi;

    public PBox(int[] bytes) {
        this.pi = bytes;
        this.inversePi = SPN.inverse(pi);
    }

    public byte[] encode(byte[] origin) {
        return apply(origin, pi);
    }


    public byte[] apply(byte[] origin, int[] fx) {
        BitArray bitArray = new BitArray(origin.length * 8, origin);
        BitArray re = new BitArray(bitArray.length());
        for (int i = 0; i < bitArray.length(); i++) {
            int group = i / fx.length;
            int offset = fx[i % fx.length];
            re.set(group * fx.length + offset, bitArray.get(i));
        }
        return re.toByteArray();
    }

    public byte[] decode(byte[] bytes) {
        return apply(bytes, inversePi);
    }

}

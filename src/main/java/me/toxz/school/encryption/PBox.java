package me.toxz.school.encryption;


import java.util.Arrays;

/**
 * Created by Carlos on 2015/9/1.
 */
public class PBox {
    public static final byte mask[] = new byte[]{
            0x1, 0x2, 0x4, 0x8
    };
    private final byte[] pi;
    private final byte[] inversePi;

    public PBox(byte[] bytes) {
        if (bytes.length != 8) {
            throw new IllegalArgumentException("You must provide bytes whose length is 8 ");
        }
        this.pi = bytes;
        this.inversePi = SPN.inverse(pi);
    }

    public byte[] encode(byte[] bytes) {
        byte[] re = new byte[bytes.length];
        short[] parts = new short[bytes.length / 2];
        for (int i = 0; i < parts.length; i++) {
            for (int j = 0; j < 2; j++) {
                parts[i] += bytes[2 * i + j] & (0x0f << (j * 4)) << 4 * j;
            }
        }
        for (int i = 0; i < parts.length; i++) {
            short part = parts[i];
            short rePart = 0;
            for (int j = 0; j < 16; j++) {
                rePart += ((part & (2 ^ j)) >> ((int) pi[j]) - j);
            }
            re[i * 2] = (byte) (rePart & 0xf);
            re[i * 2 + 1] = (byte) (rePart & 0xf0);
        }
        return re;
    }

    public byte[] decode(byte[] bytes) {
        byte[] re = new byte[bytes.length];
        for (int i = 0; i < re.length; i++) {
            re[inversePi[i]] = bytes[i];
        }
        return re;
    }

}

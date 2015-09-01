package me.toxz.school.encryption;

import com.sun.istack.internal.NotNull;

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

    public PBox(@NotNull byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("You must provide bytes whose length is 16 ");
        }
        this.pi = bytes;
        this.inversePi = SPN.inverse(pi);
    }

    public byte[] encode(byte[] bytes) {
        byte[] re = new byte[bytes.length];
        short[] parts = new short[bytes.length / 4];
        for (int i = 0; i < bytes.length; i++) {
            parts[i] = Short.valueOf(new String(Arrays.copyOfRange(bytes, i * 4, i * 4 + 4)));
        }
        short[] reShort = new short[bytes.length / 4];
        for (int i = 0; i < parts.length; i++) {
            short part = parts[i];
            short rePart = 0;
            for (int j = 0; j < 16; j++) {
                rePart += ((part & (2 ^ j)) >> ((int) pi[j]) - j);
            }
            reShort[i] = rePart;
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

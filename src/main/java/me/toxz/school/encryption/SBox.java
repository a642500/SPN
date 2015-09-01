package me.toxz.school.encryption;

import com.sun.istack.internal.NotNull;

/**
 * Created by Carlos on 2015/9/1.
 */
public class SBox {
    private final byte[] pi;
    private final byte[] inversePi;


    public SBox(@NotNull byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("You must provide bytes whose length is 16 ");
        }
        this.pi = bytes;
        inversePi = SPN.inverse(pi);
    }


    public byte[] encode(byte[] bytes) {
        byte[] re = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int index = bytes[i];
            re[i] = pi[index];
        }
        return re;
    }

    public byte[] decode(byte[] bytes) {
        byte[] re = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int index = bytes[i];
            re[i] = inversePi[index];
        }
        return re;
    }
}

package me.toxz.school.encryption;


import sun.security.util.BitArray;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carlos on 2015/9/1.
 */
public class SBox {
    private final Map<Byte, Byte> pi;
    private final Map<Byte, Byte> inversePi;
    private final int LENGTH;

    public Map<Byte, Byte> createMap(byte[] bytes) {
        Map<Byte, Byte> map = new HashMap<>(LENGTH * LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                byte key = (byte) (i ^ j << 4);
                byte value = (byte) (bytes[i] ^ bytes[j] << 4);
                map.put(key, value);
            }
        }
        return map;
    }

    public SBox(byte[] bytes) {
        LENGTH = bytes.length;
        this.pi = createMap(bytes);
        inversePi = SPN.inverse(pi);
    }


    public byte[] encode(byte[] bytes) {
        return apply(bytes, pi);
    }

    public byte[] decode(byte[] bytes) {
        return apply(bytes, inversePi);
    }

    private byte[] apply(byte[] origin, Map<Byte, Byte> table) {
        new BitArray(origin.length * 2, origin);
        byte[] re = new byte[origin.length];
        for (int i = 0; i < origin.length; i++) {
            re[i] = table.get(origin[i]);
        }
        return re;
    }
}

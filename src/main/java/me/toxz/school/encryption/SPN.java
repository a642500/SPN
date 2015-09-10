package me.toxz.school.encryption;

import sun.security.util.BitArray;

import java.util.*;

/**
 * Created by Carlos on 2015/9/1.
 */
public class SPN {
    private Padding mPadding = null;

    public static <T, K> Map<K, T> inverse(Map<T, K> origin) {
        HashMap<K, T> map = new HashMap<>(origin.size());
        origin.forEach((t, k) -> map.put(k, t));
        return map;
    }

    public static int[] inverse(int[] ints) {
        int[] re = new int[ints.length];
        for (int i = 0; i < ints.length; i++) {
            re[ints[i]] = i;
        }
        return re;
    }

    public static int printByte(byte b) {
        return b & 0xFF;
    }

    public static int[] printByteArray(byte[] bytes) {
        int[] re = new int[bytes.length];
        for (int i = 0; i < re.length; i++) {
            re[i] = printByte(bytes[i]);
        }
        return re;
    }

    public static List<String> printByteMap(Map<Byte, Byte> map) {
        ArrayList<String> re = new ArrayList<>(map.size());
        map.forEach((aByte, aByte2) -> re.add(printByte(aByte) + " -> " + printByte(aByte2)));
        return re;
    }

    public byte[] xorKey(byte[] key, byte[] origin) {
        byte[] an = new byte[origin.length];
        int keyLength = key.length;
        for (int i = 0; i < origin.length; i++) {
            an[i] = (byte) (origin[i] ^ key[i % keyLength]);
        }
        return an;
    }

    public void setPadding(Padding padding) {
        this.mPadding = padding;
    }

    /**
     * fill bytes length to 16
     */
    public interface Padding {
        byte[] padding(byte[] bytes);
    }

}

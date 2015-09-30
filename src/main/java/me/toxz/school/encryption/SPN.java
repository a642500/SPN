package me.toxz.school.encryption;

import com.sun.istack.internal.NotNull;
import sun.security.util.BitArray;

import java.util.*;

/**
 * Created by Carlos on 2015/9/1.
 */
public class SPN {
    private Padding mPadding = null;

    public SPN(@NotNull SBox sBox, @NotNull PBox pBox) {
        this.mSBox = sBox;
        mPBox = pBox;
    }

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

    public static String printByteBin(byte b) {
        byte[] bytes = new byte[]{b};
        BitArray bitArray = new BitArray(bytes.length * 8, bytes);
        return bitArray.toString();
    }

    public static int[] printByteArray(byte[] bytes) {
        int[] re = new int[bytes.length];
        for (int i = 0; i < re.length; i++) {
            re[i] = printByte(bytes[i]);
        }
        return re;
    }

    public static String printByteArrayBin(byte[] bytes) {
        BitArray bitArray = new BitArray(bytes.length * 8, bytes);
        return bitArray.toString();
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

    public byte[] encode(byte[] key, byte[] origin) {
        BitArray keyBit = new BitArray(key.length * 8, key);
        int time = key.length * 2 - 4;
        byte[] w = origin;
        for (int i = 0; i < time; i++) {
            BitArray kBit = bitArrayCopy(keyBit, i * 4, i * 4 + 16);
            byte[] k = kBit.toByteArray();
            byte[] u = xorKey(k, w);
            byte[] v = mSBox.encode(u);
            w = mPBox.encode(v);
        }
        byte[] k = bitArrayCopy(keyBit, time * 4, time * 4 + 16).toByteArray();
        return xorKey(k, w);
    }

    public BitArray bitArrayCopy(BitArray original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        BitArray bitArray = new BitArray(newLength);
        for (int i = from; i < to; i++) {
            bitArray.set(i - from, original.get(i));
        }
        return bitArray;
    }


    private final SBox mSBox;
    private final PBox mPBox;

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

package me.toxz.school.encryption;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carlos on 2015/9/1.
 */
public class SPN {
    private Padding mPadding = null;

    /**
     * swipe value with index
     */
    @Deprecated
    public static byte[] inverse(byte[] bytes) {
        byte[] inversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {

            int value = 2 * i + 1;
            int index = (bytes[i] & 0x0f) / 2;
            int high = (bytes[i] & 0x0f) % 2;
            inversed[index] += value << high;

            value = 2 * i;
            index = ((bytes[i] & 0xf0) >> 4) / 2;
            high = ((bytes[i] & 0xf0) >> 4) % 2;
            inversed[index] += value << high;
        }
        return inversed;
    }

    public static <T, K> Map<K, T> inverse(Map<T, K> origin) {
        Map<K, T> map = new HashMap<>(origin.size());
        map.forEach(map::put);
        return map;
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

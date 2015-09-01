package me.toxz.school.encryption;

import java.util.Arrays;

/**
 * Created by Carlos on 2015/9/1.
 */
public class ZeroPadding implements SPN.Padding {
    @Override
    public byte[] padding(byte[] bytes) {
        int t = bytes.length % 4;
        if (t == 0) return bytes;
        else return Arrays.copyOf(bytes, 4 * t + 4);
    }
}

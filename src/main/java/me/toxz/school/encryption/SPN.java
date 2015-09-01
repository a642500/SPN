package me.toxz.school.encryption;

/**
 * Created by Carlos on 2015/9/1.
 */
public class SPN {
    private Padding mPadding = null;

    /**
     * swipe value with index
     */
    public static byte[] inverse(byte[] bytes) {
        byte[] inversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int index = bytes[i];
            inversed[index] = (byte) i;
        }
        return inversed;
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

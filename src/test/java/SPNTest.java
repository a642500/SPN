import me.toxz.school.encryption.PBox;
import me.toxz.school.encryption.SBox;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by Carlos on 2015/9/1.
 */

public class SPNTest {
    public static final byte[] SBOX_SAMPLE = new byte[]{
            0xe, 0x4, 0xd, 0x1, 0x2, 0xf, 0xb, 0x8, 0x3, 0xa, 0x6, 0xc, 0x5, 0x9, 0x0, 0x7
    };
    public static final byte[] X_SAMPLE = new byte[]{
            0x2, 0x6, 0xb, 0x7
    };
    public static final byte[] K_SAMPLE = new byte[]{
            0x3, 0xa, 0x9, 0x4, 0xd, 0x6, 0x3, 0xf
    };
    public static final byte[] PBOX_SAMPLE = new byte[]{
            0x0, 0x4, 0x8, 0xc, 0x1, 0x5, 0x9, 0xd, 0x2, 0x6, 0xa, 0xe, 0x3, 0x7, 0xb, 0xf
    };

    @Test
    public void testSBox() {
        SBox sBox = new SBox(SBOX_SAMPLE);
        byte[] k1 = Arrays.copyOf(K_SAMPLE, 4);
        byte[] u1 = new byte[X_SAMPLE.length];
        for (int i = 0; i < X_SAMPLE.length; i++) {
            byte b = X_SAMPLE[i];
            u1[i] = (byte) (b ^ k1[i % 4]);

        }
        assertArrayEquals(new byte[]{
                0x1, 0xc, 0x2, 0x3
        }, u1);

        byte[] v1 = sBox.encode(u1);
        assertArrayEquals(new byte[]{
                0x4, 0x5, 0xd, 0x1
        }, v1);

        assertArrayEquals(u1, sBox.decode(v1));
    }

    @Test
    public void testPBox() {
        PBox pBox = new PBox(PBOX_SAMPLE);

        SBox sBox = new SBox(SBOX_SAMPLE);
        byte[] k1 = Arrays.copyOf(K_SAMPLE, 4);
        byte[] u1 = new byte[X_SAMPLE.length];
        for (int i = 0; i < X_SAMPLE.length; i++) {
            byte b = X_SAMPLE[i];
            u1[i] = (byte) (b ^ k1[i % 4]);

        }
        byte[] v1 = sBox.encode(u1);
        byte[] w1 = pBox.encode(v1);

        assertArrayEquals(new byte[]{
                0x2, 0xe, 0x0, 0x7
        }, w1);
    }
}

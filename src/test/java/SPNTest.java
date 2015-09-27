import me.toxz.school.encryption.PBox;
import me.toxz.school.encryption.SBox;
import me.toxz.school.encryption.SPN;
import org.junit.Before;
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
    public static final byte[] ORIGIN_SAMPLE = new byte[]{
            0x26, (byte) 0xb7
    };
    public static final byte[] KEY_SAMPLE = new byte[]{
            0x3a, (byte) 0x94, (byte) 0xd6, 0x3f
    };
    public static final int[] PBOX_SAMPLE = new int[]{
            0x0, 0x4, 0x8, 0xc, 0x1, 0x5, 0x9, 0xd, 0x2, 0x6, 0xa, 0xe, 0x3, 0x7, 0xb, 0xf
    };

    @Test
    public void testConstantValue() {
        System.out.println(Arrays.toString(ORIGIN_SAMPLE));
        System.out.println(Arrays.toString(KEY_SAMPLE));
        System.out.println(Arrays.toString(SBOX_SAMPLE));
        System.out.println(Arrays.toString(PBOX_SAMPLE));
    }

    @Before
    public void setSPN() {
        mSBox = new SBox(SBOX_SAMPLE);
        mPBox = new PBox(PBOX_SAMPLE);
        mSPN = new SPN(mSBox, mPBox);
    }

    private SPN mSPN;
    private SBox mSBox;
    private PBox mPBox;


    @Test
    public void testXOR() {
        assertArrayEquals(new byte[]{
                0x1c, 0x23
        }, mSPN.xorKey(KEY_SAMPLE, ORIGIN_SAMPLE));
    }

    private byte[] getU1() {
        return mSPN.xorKey(KEY_SAMPLE, ORIGIN_SAMPLE);
    }

    private byte[] getV1() {
        return mSBox.encode(getU1());
    }

    @Test
    public void testSBox() {
        byte[] v1 = getV1();

        assertArrayEquals(new byte[]{0x45, (byte) 0xd1}, v1);
        assertArrayEquals(getU1(), mSBox.decode(v1));
    }

    @Test
    public void testPBox() {
        byte[] w1 = mPBox.encode(getV1());
        assertArrayEquals(new byte[]{0x2e, 0x07}, w1);
        assertArrayEquals(getV1(), mPBox.decode(w1));
    }

    @Test
    public void testSPN() {
        byte[] y = mSPN.encode(KEY_SAMPLE, ORIGIN_SAMPLE);

        assertArrayEquals(new byte[]{(byte) 0xbc, (byte) 0xd6}, y);
    }
}

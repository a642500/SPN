import me.toxz.school.encryption.PBox;
import me.toxz.school.encryption.Pair;
import me.toxz.school.encryption.SBox;
import me.toxz.school.encryption.SPN;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Carlos on 2015/9/29.
 */
public class SPNCracker {
    private File mFile;
    public static final String TEST_FILE = "/home/carlos/tmp/test.spn";
    public static final int KEY_COUNT = 10000;
    public static final byte[] SBOX_KEY = new byte[]{};
    public static final int[] PBOX_KEY = new int[]{};
    public static final byte[] KEY = new byte[]{};
    private SPN mSpn;

    @Before
    public void setFile() {
        mFile = new File(TEST_FILE);
        mSpn = new SPN(new SBox(SBOX_KEY), new PBox(PBOX_KEY));
    }

    private void createFile() {
        Random random = new Random();
        if (mFile.exists()) {
            System.out.println("file exist, not create it!");
            return;
        }


        try (FileOutputStream stream = new FileOutputStream(mFile);
             ObjectOutputStream out = new ObjectOutputStream(stream)
        ) {
            List<Pair<byte[], byte[]>> list = new ArrayList<>();
            for (int i = 0; i < KEY_COUNT; i++) {
                byte[] key = new byte[4];
                random.nextBytes(key);
                byte[] value = mSpn.encode(KEY, key);
                list.add(new Pair<>(key, value));
            }
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Pair<byte[], byte[]>> readFile() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(mFile));
        return (List<Pair<byte[], byte[]>>) in.readObject();
    }

    private int[] expandToIntArray(byte[] b) {

    }

    private byte[] toByteArray(int[] b) {

    }

    private static final int[] SBox_C = {0xe, 0x3, 0x4, 0x8, 0x1, 0xc, 0xa, 0xf, 0x7, 0xd, 0x9, 0x6, 0xb, 0x2, 0x0, 0x5};
    private static final int[] SBOX_ORIGIN = {0xe, 0x4, 0xd, 0x1, 0x2, 0xf, 0xb, 0x8, 0x3, 0xa, 0x6, 0xc, 0x5, 0x9, 0x0, 0x7};


    @Test
    public void tryLinearAnalysis() throws IOException, ClassNotFoundException {
        analysis((or, u42, u44) ->
                (((or[1] >> 3) & 0x1)
                        ^ (((or[1]) >> 1) & 0x1)
                        ^ (or[1] & 0x1)
                        ^ ((u42 >> 2) & 0x1)
                        ^ (u42 & 0x1)
                        ^ ((u44 >> 2) & 0x1)
                        ^ (u44 & 0x1)) == 0
        );
    }

    private int[] bruteForce(int[] result, SPN spn, Pair<byte[], byte[]> anyPair) {

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    for (int l = 0; l < 16; l++) {
                        for (int m = 0; m < 16; m++) {
                            for (int n = 0; n < 16; n++) {
                                result[0] = i;
                                result[1] = j;
                                result[2] = k;
                                result[3] = l;
                                result[4] = m;
                                result[6] = n;

                                if (spn.encode(toByteArray(result), anyPair.first) == anyPair.second)
                                    return result;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Test
    public void tryDifferentialCryptanalysis() throws IOException, ClassNotFoundException {
        analysis((or, u42, u44) ->
                (((or[1] >> 3) & 0x1)
                        ^ (((or[1]) >> 1) & 0x1)
                        ^ (or[1] & 0x1)
                        ^ ((u42 >> 2) & 0x1)
                        ^ (u42 & 0x1)
                        ^ ((u44 >> 2) & 0x1)
                        ^ (u44 & 0x1)) == 0);
    }

    private void analysis(Analyser analyser) throws IOException, ClassNotFoundException {
        int[][] countL1L2 = new int[16][16];
        int[] result = new int[8];
        int[] maxKey = new int[2];


        List<Pair<byte[], byte[]>> list = readFile();

        list.forEach(pair -> {
            int v42, v44, u42, u44;

            for (int j = 0; j < 16; j++) {

                for (int k = 0; k < 16; k++) {
                    v42 = j ^ (expandToIntArray(pair.first)[1]);
                    v44 = k ^ (expandToIntArray(pair.first)[3]);

                    u42 = SBox_C[v42];
                    u44 = SBox_C[v44];

                    if ((analyser.isCount(expandToIntArray(pair.second), u42, u44))) {
                        countL1L2[j][k] += 1;
                    }

                }
            }


        });

        int max = -1;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                countL1L2[i][j] -= list.size() / 2;
                if (countL1L2[i][j] > max) {
                    max = countL1L2[i][j];
                    maxKey[0] = i;
                    maxKey[1] = j;
                }
            }
        }


        result[5] = maxKey[0];
        result[7] = maxKey[1];


        Assert.assertNotNull(bruteForce(result, mSpn, list.get(0)));
    }

    public interface Analyser {
        boolean isCount(int[] or, int u42, int u44);
    }

}

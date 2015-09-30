package me.toxz.school.encryption;


import java.math.BigInteger;
import java.util.Date;
import java.util.Random;


public class RSA {
    public static final int DEFAULT_BIG_INTEGER_LENGTH = 1024;

    private int mBigIntegerLength;
    private Mode mMode;

    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger N;
    private BigInteger e;
    private BigInteger d;
    private String publicKey;
    private String privateKey;

    public enum Mode {MODE_QUICK, MODE_MOD_REPEAT_SQUARE, MODE_CHINESE_REMAINDER_THEOREM, MODEMONTGOMERY_RSA}


    public RSA() {
        init(Mode.MODE_QUICK, DEFAULT_BIG_INTEGER_LENGTH);
    }


    public RSA(Mode mode) {
        init(mode, DEFAULT_BIG_INTEGER_LENGTH);
    }

    public RSA(Mode mode, int bigIntegerLength) {
        init(mode, bigIntegerLength);
    }

    private void init(Mode mode, int bigIntegerLength) {
        mMode = mode;
        mBigIntegerLength = bigIntegerLength;

        p = BigInteger.probablePrime(bigIntegerLength, new Random(new Date().getTime()));
        q = BigInteger.probablePrime(bigIntegerLength, new Random(new Date().getTime()));
        n = p.multiply(q);
        N = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bigIntegerLength, new Random(new Date().getTime()));

        d = inverseMod(N, e);

        publicKey = e.toString(10);
        privateKey = d.toString(10);
    }

    public class BigIntegerLengthOutOfBoundException extends IllegalArgumentException {
    }


    public String RSAEncryption(String proclaimedText) {
        String cipherText;

        if (new BigInteger(proclaimedText).bitLength() > mBigIntegerLength)
            throw new BigIntegerLengthOutOfBoundException();
        else switch (mMode) {
            case MODE_QUICK:
                cipherText = quickRSAEncryption(proclaimedText);
                break;
            case MODE_MOD_REPEAT_SQUARE:
                cipherText = modRepeatSquareRSAEncryption(proclaimedText);
                break;
            case MODE_CHINESE_REMAINDER_THEOREM:
                cipherText = chineseRemainderTheoremRSAEncryption(proclaimedText);
                break;
            case MODEMONTGOMERY_RSA:
                cipherText = montgomeryRSAEncryption(proclaimedText);
                break;
            default:
                throw new IllegalStateException("No RSA mode!");
        }
        return cipherText;
    }


    public String RSADecryption(String cipherText) {
        if (new BigInteger(cipherText).bitLength() > mBigIntegerLength * 2) {
            throw new BigIntegerLengthOutOfBoundException();
        } else
            switch (mMode) {
                case MODE_QUICK:
                    return quickRSADecryption(cipherText);
                case MODE_MOD_REPEAT_SQUARE:
                    return modRepeatSquareRSADecryption(cipherText);
                case MODE_CHINESE_REMAINDER_THEOREM:
                    return chineseRemainderTheoremRSADecryption(cipherText);
                case MODEMONTGOMERY_RSA:
                    return montgomeryRSADecryption(cipherText);
                default:
                    throw new IllegalStateException("No RSA mode!");
            }
    }

    private String quickRSAEncryption(String proclaimedText) {
        BigInteger proclaimedTextBigInteger = new BigInteger(proclaimedText);
        BigInteger cipherTextBigInteger = proclaimedTextBigInteger;
        while (proclaimedTextBigInteger.subtract(BigInteger.ONE).compareTo(BigInteger.ZERO) == 1) {
            cipherTextBigInteger = cipherTextBigInteger.multiply(cipherTextBigInteger).mod(n);
        }
        return cipherTextBigInteger.toString();
    }

    private String quickRSADecryption(String cipherText) {
        BigInteger proclaimedTextBigInteger = new BigInteger(cipherText);
        while (e.subtract(BigInteger.ONE).compareTo(BigInteger.ZERO) == 1) {
            proclaimedTextBigInteger = proclaimedTextBigInteger.multiply(proclaimedTextBigInteger).mod(n);
        }
        return proclaimedTextBigInteger.toString();
    }


    private String modRepeatSquareRSAEncryption(String proclaimedText) {
        return modRepeatSquare(new BigInteger(proclaimedText), e, n).toString();
    }


    private String modRepeatSquareRSADecryption(String cipherText) {

        return modRepeatSquare(new BigInteger(cipherText), d, n).toString();
    }


    private String chineseRemainderTheoremRSAEncryption(String proclaimedText) {
        return chineseRemainderTheoremRSA(proclaimedText, e);
    }


    private String chineseRemainderTheoremRSADecryption(String cipherText) {
        return chineseRemainderTheoremRSA(cipherText, d);
    }

    private String chineseRemainderTheoremRSA(String target, BigInteger radix) {
        BigInteger R1 = modRepeatSquare(new BigInteger(target), radix, p);
        BigInteger R2 = modRepeatSquare(new BigInteger(target), radix, q);

        BigInteger M1 = inverseMod(p, q);
        BigInteger M2 = inverseMod(q, p);
        return R1.multiply(M1).multiply(q).add(R2.multiply(M2).multiply(p)).mod(n).toString();
    }


    public BigInteger modRepeatSquare(BigInteger radix, BigInteger index, BigInteger module) {
        BigInteger two = new BigInteger("2");
        BigInteger a = BigInteger.ONE;
        BigInteger b = radix;
        BigInteger n = index;
        while (n.compareTo(BigInteger.ZERO) == 1) {
            if (n.mod(two).compareTo(BigInteger.ONE) == 0) {
                a = a.multiply(b).mod(module);
            }
            b = b.multiply(b).mod(module);
            n = n.divide(two);

        }
        return a;
    }


    public static BigInteger inverseMod(BigInteger N, BigInteger e) {
        BigInteger[] m = {BigInteger.ONE, BigInteger.ZERO, N};
        BigInteger[] n = {BigInteger.ZERO, BigInteger.ONE, e};
        BigInteger[] tmp = new BigInteger[3];
        //Initial some filed.
        BigInteger q;
        boolean flag = true;

        while (flag) {
            q = m[2].divide(n[2]);
            for (int i = 0; i < 3; i++) {
                tmp[i] = m[i].subtract(q.multiply(n[i]));
                m[i] = n[i];
                n[i] = tmp[i];
            }
            if (n[2].compareTo(BigInteger.ONE) == 0) {
                if (n[1].compareTo(BigInteger.ZERO) == -1) {
                    n[1] = n[1].add(N);
                }
                return n[1];
            }
            if (n[2].compareTo(BigInteger.ZERO) == 0) {
                flag = false;
            }
        }
        return BigInteger.ZERO;

    }


    private String montgomeryRSAEncryption(String proclaimedText) {
        return montgomeryPowerMultiply(new BigInteger(proclaimedText), e, n).toString();
    }


    private String montgomeryRSADecryption(String cipherText) {
        return montgomeryPowerMultiply(new BigInteger(cipherText), d, n).toString();
    }


    public BigInteger montgomeryPowerMultiply(BigInteger radix, BigInteger index, BigInteger module) {
        int tmp[] = new int[index.bitLength()];
        BigInteger result = radix;
        BigInteger indexTmp = index;
        BigInteger two = new BigInteger("2");
        BigInteger _k = exponentMultiply(two, index.bitLength());
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = indexTmp.mod(two).compareTo(BigInteger.ONE) == 0 ? 1 : 0;
            indexTmp = indexTmp.divide(two);
        }

        for (int anTmp : tmp) {
            if (anTmp == 1) {
                result = montgomeryMultiply(result, module).multiply(result).multiply(_k).mod(module);
            } else {
                result = montgomeryMultiply(result, module);
            }
        }

        return result;
    }

    /**
     * Calculate A*A*2^(-k)(mod n)
     */
    public BigInteger montgomeryMultiply(BigInteger a, BigInteger module) {
        BigInteger result = BigInteger.ZERO;
        int k = module.bitLength();
        BigInteger TWO = new BigInteger("2");
        for (int i = 0; i < k; i++) {
            result = result.add(a.multiply(a));
            if ((result.mod(TWO)).compareTo(BigInteger.ONE) == 0) {
                result = result.add(module);
            }
            result = result.divide(TWO);
            if (result.compareTo(module) == 1) {
                result = result.subtract(module);
            }
        }

        return result;
    }

    /**
     * Calculate exponent multiply. radix^k
     */
    public BigInteger exponentMultiply(BigInteger radix, int k) {
        BigInteger result = radix;
        for (int i = 0; i < k; i++) {
            result = result.multiply(radix);
        }
        return result;
    }
}

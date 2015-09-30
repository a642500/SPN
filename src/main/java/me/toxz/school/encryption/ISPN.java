package me.toxz.school.encryption;

/**
 * Created by Carlos on 2015/9/30.
 */
public interface ISPN {
    byte[] encode(byte[] key, byte[] origin);

    byte[] decode(byte[] key, byte[] origin);
}

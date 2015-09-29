package me.toxz.school.encryption;

/**
 * Created by Carlos on 2015/9/30.
 */
public class Pair<F, S> {

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public final F first;
    public final S second;
}

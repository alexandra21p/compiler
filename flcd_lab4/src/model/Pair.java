package model;

/**
 * Created by Alexandra on 17/01/2017.
 */
public class Pair {
    String s1;
    String s2;

    public Pair(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }

    @Override
    public int hashCode() {
        return (s1 == null ? 0 : s1.hashCode()) ^ (s2 == null ? 0 : s2.hashCode());
    }
    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Pair)) return false;
        Pair p = (Pair)o;
        return p.s1.equals(s1) && p.s2.equals(s2);
    }

    public String toString() {
        return  "(" + s1 + ", " + s2 + ")";
    }

}

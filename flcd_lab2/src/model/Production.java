package model;

/**
 * Created by Alexandra on 27/11/2016.
 */
public class Production {
    String left;
    String delim;
    String right;

    public Production(String left, String right) {
        this.left = left;
        this.delim = "->";
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }
}

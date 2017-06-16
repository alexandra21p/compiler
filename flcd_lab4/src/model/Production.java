package model;

/**
 * Created by Alexandra on 17/01/2017.
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

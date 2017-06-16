package model;

import java.util.ArrayList;

/**
 * Created by Alexandra on 17/01/2017.
 */
public class First {
    String nonterminal;
    ArrayList<String> terminals;

    public First(String nonterminal, ArrayList<String> terminals) {
        this.nonterminal = nonterminal;
        this.terminals = terminals;
    }

    public String getNonterminal() {
        return nonterminal;
    }
    public ArrayList<String> getTerminals() { return terminals; }
    public void addTerminal(String t) { terminals.add(t); }

    public String toString()
    {
        String result = "First element: ";

        result += this.nonterminal + " = {";
        for (String t : terminals) {
            result += t + ", ";
        }
        result += "}";
        int ind = result.lastIndexOf(",");
        if (ind != -1) {
            result = new StringBuilder(result).replace(ind, ind + 1, "").toString();
        }

        return result;
    }
}

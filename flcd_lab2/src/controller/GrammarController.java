package controller;
import model.NonTerminal;
import model.Production;
import model.Terminal;
import model.Transition;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Alexandra on 27/11/2016.
 */
public class GrammarController {
    ArrayList<Terminal> terminals;
    ArrayList<NonTerminal> nonterminals;
    //ArrayList<Production> productions;
    Map<String, ArrayList<String>> productions;
    ArrayList<Production> allProds;
    String initialState;


    public GrammarController() {
        terminals = new ArrayList<Terminal>();
        nonterminals = new ArrayList<NonTerminal>();
        //productions = new ArrayList<Production>();
        productions = new HashMap<String, ArrayList<String>>();
        allProds = new ArrayList<Production>();

    }


    public void readMinilanguage(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            //read = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                ArrayList<String> rhs = new ArrayList<String>();
                String[] str = line.split("::=");
                // left part of production
                String newStr = "";
                int delStart = str[0].indexOf("<");
                int delEnd = str[0].indexOf(">");
                for (int i = delStart+1; i < delEnd; i++) {
                    newStr += str[0].charAt(i);
                }
                //System.out.println(newStr);
                NonTerminal n = new NonTerminal(newStr);
                nonterminals.add(n);

                // right part of production
                if (str[1].contains("//")) {
                    System.out.println("[" + str[1] + "]");
                    String[] prods = str[1].split("//");
                    System.out.println("[" + prods[0] + "]");
                    for (String p : prods) {
                        System.out.println(p);
                        String[] el = p.split(" ");
                        String newRhs = "";
                        for (String token : el) {
                            if (token.contains("<")) {  // tokens between <> are nonterminals
                                String s = token.replaceAll("[<> ]", "");
                                //System.out.println(s);
                                NonTerminal nt = new NonTerminal(s);
                                newRhs += nt.getValue() + " ";
                            }
                            else if (token.contains("\"")) {  // tokens between "" are terminals
                                System.out.println();
                                String s = token.replaceAll("[\" ]+", "");
                                System.out.println(s);
                                Terminal term = new Terminal(s);
                                if (!terminals.contains(term)) {
                                    terminals.add(term);
                                }
                                newRhs += term.getValue() + " ";
                            }
                        }
                        rhs.add(newRhs);
                    }
                    productions.put(n.getValue(), rhs);

                }
                else {
                    String[] elems = str[1].split(" ");
                    String newRhs = "";
                    for (String token : elems) {
                        if (token.contains("<")) {  // tokens between <> are nonterminals
                            String s = token.replaceAll("[<> ]", "");
                            //System.out.println(s);
                            NonTerminal nt = new NonTerminal(s);
//                            if (!nonterminals.contains(nt)) {
//                                nonterminals.add(nt);
//                            }
                            newRhs += nt.getValue() + " ";
                        }
                        else if (token.contains("\"")) {  // tokens between "" are terminals
                            String s = token.replaceAll("[\" ]+", "");
                            System.out.println(s);
                            Terminal term = new Terminal(s);
                            if (!terminals.contains(term)) {
                                terminals.add(term);
                            }
                            newRhs += term.getValue() + " ";
                        }
                    }
                    rhs.add(newRhs);
                    productions.put(n.getValue(), rhs);
                }

            }
            String res = "P = {";
            for(String s : productions.keySet()) {
                res += s + " -> ";
                for (String prod : productions.get(s)) {
                    res += prod + " | ";
                }
                res += ", ";
                int ind = res.lastIndexOf("|");
                res = new StringBuilder(res).replace(ind, ind+2, "").toString();
            }
            int i = res.lastIndexOf(",");
            res = new StringBuilder(res).replace(i, i+2, "").toString();
            res += "}\n";
            System.out.println(res);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readGrammar(String fileName) {
        //boolean read = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            //read = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                //You should also check if a line is a comment
                if (line.trim().endsWith("}")) {
                    //query = new StringBuilder();
                    if (line.charAt(0) == 'N') {     // NON-TERMINALS
                        int delim1 = line.indexOf('{');
                        int delim2 = line.indexOf('}');
                        String newLine = "";
                        for (int i = delim1 + 1; i < delim2; i++) {
                            newLine += line.charAt(i);
                        }
                        if (!newLine.contains(",")) { // there's only one element
                            //System.out.println(newLine);
                            NonTerminal n = new NonTerminal(newLine);
                            nonterminals.add(n);
                        } else {
                            for (String retval : newLine.split(",")) {
                                NonTerminal n = new NonTerminal(retval.replace(" ", ""));
                                nonterminals.add(n);
                            }
                        }
                    } else if (line.charAt(0) == 'T') {    // TERMINALS
                        int delim1 = line.indexOf('{');
                        int delim2 = line.indexOf('}');
                        String newLine = "";
                        for (int i = delim1 + 1; i < delim2; i++) {
                            newLine += line.charAt(i);
                        }
                        if (!newLine.contains(",")) { // there's only one element
                            //System.out.println(newLine);
                            Terminal t = new Terminal(newLine);
                            terminals.add(t);
                        } else {
                            for (String retval : newLine.split(",")) {
                                Terminal t = new Terminal(retval.replace(" ", ""));
                                terminals.add(t);
                            }
                        }
                    } else if (line.charAt(0) == 'P') {    // PRODUCTIONS
                        int delim1 = line.indexOf('{');
                        int delim2 = line.indexOf('}');
                        String[] atoms;
                        String newLine = "";
                        for (int i = delim1 + 1; i < delim2; i++) {
                            newLine += line.charAt(i);
                        }
                        System.out.println(newLine);

                        atoms = newLine.split(",");
                        for (String atom : atoms) {
                            System.out.println(atom + ", ");
                            String[] atomsProd = atom.split("->");
                            Production p = new Production(atomsProd[0].replace(" ", ""), atomsProd[1].replace(" ", ""));
                            allProds.add(p);

                            if (productions.isEmpty() || !productions.containsKey(atomsProd[0].replace(" ", ""))) {
                                ArrayList<String> prods = new ArrayList<String>();
                                prods.add(p.getRight());
                                productions.put(p.getLeft(), prods);
                            } else {
                                if (productions.containsKey(atomsProd[0].replace(" ", ""))) {
                                    //ArrayList<String> prods = new ArrayList<>();
                                    ArrayList<String> prods = productions.get(atomsProd[0].replace(" ", ""));
                                    prods.add(atomsProd[1].replace(" ", ""));
                                    productions.put(atomsProd[0].replace(" ", ""), prods);
                                }
                            }
                        }
                        String ceva = "";
                        for (String key : productions.keySet()) {
                            ceva += " " + key + productions.get(key) + "\n";
                        }
                        System.out.println(ceva);
                    }

                } else {
                    if (line.charAt(0) == 'S') {
                        int delim = line.indexOf('=');
                        String newStr = "";
                        initialState = newStr + line.charAt(delim + 2);
                        System.out.println(initialState);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return read;
    }

    public boolean checkIfRegular() {
        boolean isRightLinear = false;
        ArrayList<String> rh = new ArrayList<>();
        for (String key : productions.keySet()) {
            rh.addAll(productions.get(key).stream().collect(Collectors.toList()));
        }
        System.out.println(rh);

        String str = "";
        // right
        str += "E|[";
        for (Terminal t : terminals) {
            str += t.getValue();
        }
        str += "][";
        for (NonTerminal n : nonterminals) {
            str += n.getValue();
        }
        str += "]?";


        for (String s : rh) {
            isRightLinear = (s.length() <= 2 && Pattern.matches(str, s));
        }

        // checks if the rhs of any production contains epsilon
        // if it does and the symbol is not the initial state S, it returns false
        boolean epsilonInitialState = false;
        boolean rightHandInitialState = false;
        System.out.println(productions.keySet());
        for (String symbol : productions.keySet()) {
            if (productions.get(symbol).contains("E") && !symbol.equals(initialState)) {
                return false;
                // the grammar cannot be regular
            }
            // if it does contain epsilon, must check if the initial state is on the rhs of any production,
            // and if it is, the grammar is not regular
            else {
                if (productions.get(symbol).contains("E") && symbol.equals(initialState)) {
                    epsilonInitialState = true;
                }
            }
        }

        if (epsilonInitialState) {
            StringBuilder sb = new StringBuilder();
            for (String s : rh) {
                sb.append(s);
            }
            if (sb.toString().contains(initialState)) {
                rightHandInitialState = true;
            } else {
                rightHandInitialState = false;
            }

            System.out.println(rightHandInitialState);
        }

        System.out.println(isRightLinear + ", " + epsilonInitialState + ", " + rightHandInitialState);
        return isRightLinear && !rightHandInitialState;

    }

    public String constructFA() {
        String res = "Q = {";
        for (NonTerminal n : nonterminals) {
            res += n.getValue() + ", ";
        }
        int idx = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(idx, idx + 2, "").toString();
        res += "} \nA = {";

        for (Terminal t : terminals) {
            res += t.getValue() + ", ";
        }
        int index = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(index, index + 2, "").toString();
        res += "} \ntransitions = {";


        HashMap<Transition, ArrayList<String>> transitions = new HashMap<Transition, ArrayList<String>>();
        for (String key : productions.keySet()) {
            ArrayList<String> prods = productions.get(key);
            for (String s : prods) {
                if (s.length() == 1) {
                    Transition t = new Transition(key, s);
                    if (transitions.containsKey(t)) {
                        ArrayList<String> nextStates = transitions.get(t);
                        nextStates.add("K");
                        transitions.put(t, nextStates);
                    } else {
                        ArrayList<String> nextStates = new ArrayList<>();
                        nextStates.add("K");
                        transitions.put(t, nextStates);
                    }
                } else {
                    String[] newStr = s.split("");
                    Transition t = new Transition(key, newStr[0]);
                    if (transitions.containsKey(t)) {
                        ArrayList<String> nextStates = transitions.get(t);
                        nextStates.add(newStr[1]);
                        transitions.put(t, nextStates);
                    } else {
                        ArrayList<String> nextStates = new ArrayList<>();
                        nextStates.add(newStr[1]);
                        transitions.put(t, nextStates);
                    }
                }

            }
        }
        for (Transition key : transitions.keySet()) {
            res += "(" + key.getCurrentState() + ", " + key.getSymbol() + ") -| (";
            for (String s : transitions.get(key)) {
                res += s + ", ";
            }
            int idxx = res.lastIndexOf(",");
            res = new StringBuilder(res).replace(idxx, idxx + 2, "").toString();
            res += "); ";
        }
        int ind = res.lastIndexOf(";");
        res = new StringBuilder(res).replace(ind, ind + 2, "").toString();
        res += "} \n";
        res += "q0 = " + initialState;

        return res;
    }

    public String showTerminals() {
        String res = "\tTERMINALS: \n { ";
        for (Terminal t : terminals) {
            res += t.getValue() + ", ";
        }
        int ind = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(ind, ind + 1, "").toString();
        res += "}\n";
        return res;
    }

    public String showNonTerminals() {
        String res = "\tNON-TERMINALS: \n { ";
        for (NonTerminal n : nonterminals) {
            res += n.getValue() + ", ";
        }
        int ind = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(ind, ind + 1, "").toString();
        res += "}\n";
        return res;
    }

    public String showProductions() {
        String res = "\tPRODUCTIONS: \n { ";
        for (Production p : allProds) {
            res += p.getLeft() + " -> " + p.getRight() + ", ";
        }
        int ind = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(ind, ind + 1, "").toString();
        res += "}\n";
        return res;
    }

    public String showProdForNonTerminal(String token) {
        String res = "\tPRODUCTIONS FOR GIVEN NON-TERMINAL: ";
        res += token + "\n";
        for (String key : productions.keySet()) {
            if (key.equals(token)) {
                ArrayList<String> prods = productions.get(key);
                res += "{ " + key + " -> ";
                for (String p : prods) {
                    res += p + " | ";
                }
                int ind = res.lastIndexOf("|");
                res = new StringBuilder(res).replace(ind, ind + 2, "").toString();
                res += "} \n";
            }
        }
        return res;
    }


    public String elimInaccessible() {
        String res = "";
        ArrayList<String> finalSet = new ArrayList<>();
        finalSet.add(initialState);
        ArrayList<String> clone = new ArrayList<>();
        clone.add(initialState);
        //int i = 1;
        while (!clone.isEmpty()) {
            String frst = clone.get(0); // get the first
            ArrayList<String> prods = productions.get(frst);
            for (String str : prods) {
                for (NonTerminal nt : nonterminals) {
                    if (str.contains(nt.getValue()) && !finalSet.contains(nt.getValue())) {
                        clone.add(nt.getValue());
                        finalSet.add(nt.getValue());
                    }
                }
            }
            clone.remove(frst);
            System.out.println("Accessible symbols: " + clone);
        }

        System.out.println(finalSet);

        // eliminate the productions of inaccessible symbols
        ArrayList<NonTerminal> copy = (ArrayList<NonTerminal>) nonterminals.clone();
        for (NonTerminal symb : copy) {
            if (!finalSet.contains(symb.getValue())) {  // is inaccessible
                productions.remove(symb.getValue()); // remove its productions
                nonterminals.remove(symb);
            }
        }

        res += "G = {N = {";
        for (NonTerminal n : nonterminals) {
            res += n.getValue() + ", ";
        }
        int idx = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(idx, idx+2, "").toString();
        res += "}, T = {";

        for (Terminal t : terminals) {
            res += t.getValue() + ", ";
        }
        int index = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(index, index+2, "").toString();
        res += "}, ";
        res += ", P = {";

        for(String s : productions.keySet()) {
            res += s + " -> ";
            for (String prod : productions.get(s)) {
                res += prod + " | ";
            }
            res += ", ";
            int ind = res.lastIndexOf("|");
            res = new StringBuilder(res).replace(ind, ind+2, "").toString();
        }
        int i = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(i, i+2, "").toString();

        res += "}, S = " + initialState + "}}\n";

        return res;
    }
}

//
//input grammar N = {S, A, B}
//        T = {a, b}
//        S = S
//        P = {S -> aS, S -> aA, A -> bS, A -> aB, B -> bS, S -> a}
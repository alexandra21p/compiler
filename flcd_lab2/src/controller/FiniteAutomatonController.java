package controller;

import model.NonTerminal;
import model.Production;
import model.Terminal;
import model.Transition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexandra on 06/12/2016.
 */
public class FiniteAutomatonController {
    ArrayList<String> states;
    ArrayList<String> alphabet;
    HashMap<Transition, ArrayList<String>> transitions;
    ArrayList<String> finalStates;
    String initialState;

    public FiniteAutomatonController() {
        states = new ArrayList<String>();
        alphabet = new ArrayList<String>();
        transitions = new HashMap<Transition, ArrayList<String>>();
        finalStates = new ArrayList<String>();
    }

    public void readFA(String fileName) {
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
            while( (line=br.readLine()) != null)
            {
                //You should also check if a line is a comment
                if(line.trim().endsWith("}"))
                {
                    //query = new StringBuilder();
                    if (line.charAt(0) == 'Q') {     // STATES
                        int delim1 = line.indexOf('{');
                        int delim2 = line.indexOf('}');
                        String newLine = "";
                        for (int i = delim1+1; i < delim2; i++) {
                            newLine += line.charAt(i);
                        }
                        if (!newLine.contains(",")) { // there's only one element
                            //System.out.println(newLine);
                            states.add(newLine);
                        }
                        else {
                            for (String retval: newLine.split(",")) {
                                states.add(retval.replace(" ", ""));
                            }
                        }
                    }
                    else if (line.charAt(0) == 'A') {    // ALPHABET
                        int delim1 = line.indexOf('{');
                        int delim2 = line.indexOf('}');
                        String newLine = "";
                        for (int i = delim1+1; i < delim2; i++) {
                            newLine += line.charAt(i);
                        }
                        if (!newLine.contains(",")) { // there's only one element
                            //System.out.println(newLine);
                            alphabet.add(newLine);
                        }
                        else {
                            for (String retval: newLine.split(",")) {
                                alphabet.add(retval.replace(" ", ""));
                            }
                        }
                    }
                    else if (line.charAt(0) == 'F') {    // FINAL STATE/S
                        int delim1 = line.indexOf('{');
                        int delim2 = line.indexOf('}');
                        String[] atoms;
                        String newLine = "";
                        for (int i = delim1+1; i < delim2; i++) {
                            newLine += line.charAt(i);
                        }

                        if (!newLine.contains(",")) { // there's only one element
                            //System.out.println(newLine);
                            finalStates.add(newLine);
                        }
                        else {
                            for (String retval: newLine.split(",")) {
                                finalStates.add(retval.replace(" ", ""));
                            }
                        }
                    }

                } else {
                    if (line.charAt(0) == 'q') {
                        int delim = line.indexOf('=');
                        String newStr = "";
                        initialState = newStr + line.charAt(delim+2);
                        System.out.println(initialState);
                    }
                    else {
                        //System.out.println(line + " ceva ");
                        String[] atoms = line.split(" ");
                        //System.out.println(atoms.length + "," + atoms[0] + "," + atoms[1] +"!!!");
                        String[] transElem = atoms[0].split(",");
                        Transition t = new Transition(transElem[0], transElem[1]);
                        //System.out.println("CURRENT STATE: " + t.getCurrentState() + " SYMBOL: " + t.getSymbol() + "~");
                        if (transitions.isEmpty() || !(transitions.containsKey(t))) {
                            ArrayList<String> arr = new ArrayList<>();
                            arr.add(atoms[1]);
                            System.out.println(arr);
                            transitions.put(t, arr);
                        }
                        else {
                            if (transitions.containsKey(t)) {
                                //System.out.println("hacker voice: IM IN");
                                ArrayList<String> arr = transitions.get(t);
                                arr.add(atoms[1]);
                                transitions.put(t, arr);
                            }
                        }

                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return read;
    }

    public String constructRG() {
        String res = "N = {";
        for (String s : states) {
            res += s + ", ";
        }
        int idx = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(idx, idx+2, "").toString();
        res += "} \nT = {";

        for (String a : alphabet) {
            res += a + ", ";
        }
        int index = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(index, index+2, "").toString();
        res += "} \n";
        res += "S = " + initialState + "\n";


        HashMap<String, ArrayList<String>> productions = new HashMap<String, ArrayList<String>>();
        for (Transition key : transitions.keySet()) {
            String currentState = key.getCurrentState();
            String symb = key.getSymbol();
            ArrayList<String> nextStates = transitions.get(key);
            if (productions.containsKey(currentState)) {
                ArrayList<String> prods = productions.get(currentState);
                for (String p : nextStates) {
                    if (p.equals("E")) {
                        prods.add(symb);
                    }
                    else {
                        String newStr = symb + p;
                        prods.add(newStr);
                    }
                }
                productions.put(currentState, prods);
            }
            else {
                ArrayList<String> prods = new ArrayList<String>();
                for (String p : nextStates) {
                    if (p.equals("E")) {
                        prods.add(symb);
                    }
                    else {
                        String newStr = symb + p;
                        prods.add(newStr);
                    }
                }
                productions.put(currentState, prods);
            }
        }

        for(String s : productions.keySet()) {
            System.out.println("Non-terminal: " + s + " has productions: " + productions.get(s));
        }
        return res;
    }

    public String showStates() {
        String res = "\tSTATES: \n { ";
        for (String s : states) {
            res += s + ", ";
        }
        int ind = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(ind, ind+1, "").toString();
        res += "}\n";
        return res;
    }

    public String showAlphabet() {
        String res = "\tALPHABET: \n { ";
        for (String s : alphabet) {
            res += s + ", ";
        }
        int ind = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(ind, ind+1, "").toString();
        res += "}\n";
        return res;
    }

    public String showFinalStates() {
        String res = "\tSET OF FINAL STATE: \n { ";
        for (String s : finalStates) {
            res += s + ", ";
        }
        int ind = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(ind, ind+1, "").toString();
        res += "}\n";
        return res;
    }

    public String showTransitions() {
        String res = "\tTRANSITIONS: \n { ";
        for (Transition key : transitions.keySet()) {
            res += "(" + key.getCurrentState() + ", " + key.getSymbol() + ") -| (";
            for (String s : transitions.get(key)) {
                res += s + ", ";
            }
            int idx = res.lastIndexOf(",");
            res = new StringBuilder(res).replace(idx, idx+2, "").toString();
            res += "); ";
        }
        int ind = res.lastIndexOf(";");
        res = new StringBuilder(res).replace(ind, ind+1, "").toString();
        res += "} \n";
        return res;
    }
}

package controller;

import model.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Alexandra on 17/01/2017.
 */
public class Controller {
    ArrayList<String> terminals;
    ArrayList<String> nonterminals;
    Map<String, ArrayList<String>> productions;
    ArrayList<Production> allProds;
    String initialState;

    Map<Pair, String> parsingTable;

    ArrayList<First> finalFirstList;
    ArrayList<Follow> finalFollowList;


    public Controller() {
        terminals = new ArrayList<String>();
        nonterminals = new ArrayList<String>();
        productions = new HashMap<String, ArrayList<String>>();
        allProds = new ArrayList<Production>();

        parsingTable = new LinkedHashMap<Pair, String>();

        finalFirstList = new ArrayList<>();
        finalFollowList = new ArrayList<>();
    }


    public void addFirst(String a, ArrayList<String> b, ArrayList<First> list) {
        First firstElem = new First(a, b);
        list.add(firstElem);
    }

    //comparing two lists of "first elements"
    public boolean equalFirstLists(ArrayList<First> a, ArrayList<First> b) {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getTerminals().size() != b.get(i).getTerminals().size())
                return false;
        }
        return true;
    }

    //comparing two lists of "follow elements"
    public boolean equalFollowLists(ArrayList<Follow> a, ArrayList<Follow> b) {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getTerminals().size() != b.get(i).getTerminals().size())
                return false;
        }
        return true;
    }


    public void constructFirstTable() {
        ArrayList<First> firstList = new ArrayList<>(); // initial list
        ArrayList<First> nextFirstList = new ArrayList<>();
        ArrayList<First> tempFirstList = new ArrayList<>();
        ArrayList<First> tempNextFirstList = new ArrayList<>();


        // initialize first list for terminals with themselves
        for (String terminal : terminals) {
            ArrayList<String> l = new ArrayList<String>();
            l.add(terminal);
            addFirst(terminal, l, firstList);
        }

        // initialize first list for each nonterminal with the empty set
        for (String nonterm : nonterminals) {
            ArrayList<String> l = new ArrayList<String>();
            addFirst(nonterm, l, firstList);
        }

        int i = 0; // first iteration
        for (Production p : allProds) {
            char s = p.getRight().charAt(0);
            String frstElemRightSide = "" + s;
            if (terminals.contains(frstElemRightSide) || frstElemRightSide.equals("$")) { // terminal or epsilon
                firstList.stream().filter(f -> f.getNonterminal().equals(p.getLeft())).forEach(f -> {
                    f.addTerminal(frstElemRightSide);
                });
            }
        }


        for (int q = 1; q <= 4; q++) {
            i++;

            for (First aFirstList : firstList) {
                addFirst(aFirstList.getNonterminal(), aFirstList.getTerminals(), nextFirstList);
            }

            for (Production prod : allProds) {
                char c = prod.getRight().charAt(0);
                String rightSideElemAsString = "" + c;
                if (nonterminals.contains(rightSideElemAsString)) { // if it's nonterminal
                    firstList.stream().filter(f -> f.getNonterminal().equals(rightSideElemAsString)).forEach(f -> {
                        nextFirstList.stream().filter(next -> next.getNonterminal().equals(prod.getLeft())).forEach(next -> {
                            f.getTerminals().stream().filter(s -> !next.getTerminals().contains(s)).forEach(next::addTerminal);
                        });
                    });
                }
            }


            while (tempFirstList.size() != 0) tempFirstList.remove(0);
            while (tempNextFirstList.size() != 0) tempNextFirstList.remove(0);
            tempFirstList.addAll(firstList.stream().collect(Collectors.toList()));

            tempNextFirstList.addAll(nextFirstList.stream().collect(Collectors.toList()));

            while (firstList.size() != 0) firstList.remove(0);
            while (nextFirstList.size() != 0) nextFirstList.remove(0);

            firstList.addAll(tempNextFirstList.stream().collect(Collectors.toList()));

            while (finalFirstList.size() != 0) finalFirstList.remove(0);
            finalFirstList.addAll(tempNextFirstList.stream().collect(Collectors.toList()));
        }

    }


    public void constructFollowTable() {
        ArrayList<Follow> followList = new ArrayList<>(); // initial list
        ArrayList<Follow> nextFollowList = new ArrayList<>();
        ArrayList<Follow> tempFollowList = new ArrayList<>();
        ArrayList<Follow> tempNextFollowList = new ArrayList<>();

        for (String s : nonterminals) { // for nonterminals initialize the empty list; for initial state add $
            if (s.equals(initialState)) {
                ArrayList<String> l = new ArrayList<>();
                l.add("$");
                Follow el = new Follow(s, l);
                followList.add(el);
            } else {
                ArrayList<String> l = new ArrayList<>();
                Follow el = new Follow(s, l);
                followList.add(el);
            }
        }

        do {
            // add each nonterminal into the next follow list
            for (String nont : nonterminals) {
                ArrayList<String> l = new ArrayList<>();
                Follow fl = new Follow(nont, l);
                nextFollowList.add(fl);

                for (Follow fol : followList) { // iterate initial follow list
                    if (fol.getNonterminal().equals(nont)) { // find the corresp nontermnal
                        // also find it in the next follow list and add the terminals
                        nextFollowList.stream().filter(next -> next.getNonterminal().equals(nont)).forEach(next -> {
                            fol.getTerminals().forEach(next::addTerminal);
                        });
                    }

                }


                for (Production p : allProds) { // iterate prods and find the corresp nonterminal on the rhs
                    String[] chars = p.getRight().split("");
                    for (int i  = 0; i < chars.length; i++) { // check each token from the rhs
                        if (chars[i].equals(nont)) {
                            if (i < chars.length-1) { // if it's not the last symbol from the rhs
                                boolean hasEpsilon = false;
                                // must see if First contains $ and add terminals from Follow(p.getLeft())
                                String next = chars[i + 1];
                                for (Follow f : nextFollowList)
                                {
                                    if (f.getNonterminal().equals(nont)) { // where to add
                                        for (First frst : finalFirstList) {
                                            if (frst.getNonterminal().equals(next)) {
                                                for (String s : frst.getTerminals()) {
                                                    if (s.equals("$")) hasEpsilon = true;

                                                    if (!f.getTerminals().contains(s))
                                                    f.addTerminal(s);
                                                }
                                            }
                                        }

                                        // the string s has epsilon -> add previous follow of nonterminal (Fi-1)
                                        if (hasEpsilon) {
                                            followList.stream().filter(foll -> foll.getNonterminal().equals(p.getLeft())).forEach(foll -> {
                                                foll.getTerminals().stream().filter(s -> !f.getTerminals().contains(s)).forEach(f::addTerminal);
                                            });
                                        }

                                    }
                                }

                            }
                            if (i == chars.length - 1)  { // if it's the last one
                                // iterate next follow list
// where to add
                                nextFollowList.stream().filter(next -> next.getNonterminal().equals(nont)).forEach(next -> {
                                    followList.stream().filter(fol -> fol.getNonterminal().equals(p.getLeft())).forEach(fol -> {
                                        fol.getTerminals().stream().filter(s -> !next.getTerminals().contains(s)).forEach(next::addTerminal);
                                    });
                                });
                            }

                        }
                    }
                }


            }

            while (tempFollowList.size() != 0) tempFollowList.remove(0);
            while (tempNextFollowList.size() != 0) tempNextFollowList.remove(0);
            tempFollowList.addAll(followList.stream().collect(Collectors.toList()));
            tempNextFollowList.addAll(nextFollowList.stream().collect(Collectors.toList()));

            while (followList.size() != 0) followList.remove(0);
            while (nextFollowList.size() != 0) nextFollowList.remove(0);

            followList.addAll(tempNextFollowList.stream().collect(Collectors.toList()));

            while (finalFollowList.size() != 0) finalFollowList.remove(0);
            finalFollowList.addAll(tempNextFollowList.stream().collect(Collectors.toList()));

        } while(!equalFollowLists(tempFollowList, tempNextFollowList));

    }

    public void printFirstTable() {
        System.out.println("\n______________________________________\n");
        System.out.println("\t\t\tFIRST TABLE: \n");
        System.out.println("______________________________________\n");
        for (First el : finalFirstList) {
            System.out.println(el.toString());
        }
    }
    public void printFollowTable() {
        System.out.println("\n______________________________________\n");
        System.out.println("\t\t\tFOLLOW TABLE: \n");
        System.out.println("______________________________________\n");
        for (Follow el : finalFollowList) {
            System.out.println(el.toString());
        }
    }

    public void constructLL1Table() {

        Map<String, ArrayList<String>> firstListDict = new HashMap<>();
        Map<String, ArrayList<String>> followListDict = new HashMap<>();

        for (First f : finalFirstList) {
            firstListDict.put(f.getNonterminal(), f.getTerminals());
        }

        for (Follow fo : finalFollowList) {
            followListDict.put(fo.getNonterminal(), fo.getTerminals());
        }

        int i = 1;
        for (Production p : allProds) {
            char c  = p.getRight().charAt(0);
            String firstRight = "" + c;

            if(firstRight.equals("$")) {
                ArrayList<String> followLst = followListDict.get(p.getLeft());
                for (String s : followLst) {
                    Pair pr = new Pair(p.getLeft(), s);
                    String newEl = p.getRight() + "," + i;
                    parsingTable.put(pr, newEl);
                }

            }
            else if (terminals.contains(firstRight)) {
                Pair pr = new Pair(p.getLeft(), firstRight);
                String el = p.getRight() + "," + i;
                parsingTable.put(pr,el);
            }
            else if (nonterminals.contains(firstRight)) {
                int j = 0;
                ArrayList<String> frstList = firstListDict.get(firstRight);
                for (String e : frstList) {
                    if (e.equals("$")) j = 1;
                }

                for (String s : frstList) {
                    if(!s.equals("$")) {
                        Pair pr = new Pair(p.getLeft(), s);
                        String newEl = p.getRight() + "," + i;
                        parsingTable.put(pr, newEl);
                    }
                }
                if (j == 1) {
                    ArrayList<String> followLst = followListDict.get(p.getLeft());
                    for (String s : followLst) {
                        Pair pr = new Pair(p.getLeft(), s);
                        String newEl = p.getRight() + "," + i;
                        parsingTable.put(pr, newEl);
                    }
                }
            }
            i++;
        }

        for (String terminal : terminals) {
            Pair p = new Pair(terminal, terminal);
            String newEl = "pop";
            parsingTable.put(p, newEl);
        }

        Pair pair = new Pair("$", "$");
        String newFin = "acc";
        parsingTable.put(pair, newFin);

        System.out.println("\n______________________________________\n");
        System.out.println("\t\t\tLL(1) TABLE: \n");
        System.out.println("______________________________________\n");
        for (Pair p : parsingTable.keySet()) {
            System.out.println("M(" + p.getS1() + ", " + p.getS2() + ") = (" + parsingTable.get(p) + ")");
        }
    }

    public void analysis() {
        System.out.println("\n______________________________________\n");
        System.out.println("\t\tLL(1) SEQUENCE PARSING: \n");
        System.out.println("______________________________________\n");

        String res = "";
        String inputStack= "a+a$";
        String workingStack = initialState + "$";
        String output = "";
        boolean accepted = true;

        while(!workingStack.equals(inputStack)) {
            char c = inputStack.charAt(0);
            String firstInput = "" + c;
            char ch = workingStack.charAt(0);
            String firstWork = "" + ch;
            //System.out.println(firstInput + "," + firstWork);

            if(firstInput.equals(firstWork)) {
                res = inputStack.substring(1);
                inputStack = res;

                res = workingStack.substring(1);
                workingStack = res;
            } else {
                Pair p = new Pair(firstWork, firstInput);
                if (!parsingTable.containsKey(p)) {
                    accepted = false;
                    System.out.println("năcaz - grammar rejected");
                    break;
                } else {
                    res = workingStack.substring(1);
                    workingStack = res;

                    Pair pr = new Pair(firstWork, firstInput);
                    if (parsingTable.containsKey(pr)) {
                        String[] production = parsingTable.get(pr).split(",");
                        workingStack = production[0] + workingStack;
                        char ws = workingStack.charAt(0);
                        res = "" + ws;
                        if(res.equals("$")) {
                            workingStack = workingStack.substring(1);
                        }
                        output += production[1];
                    }
                }
            }
            System.out.println("input: " + inputStack + " working: " + workingStack + " output: " + output);
        }

        if(accepted) {
            System.out.println("______________________________________\n");
            System.out.println("Productions string output: " + output);
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
                            //NonTerminal n = new NonTerminal(newLine);
                            nonterminals.add(newLine);
                        } else {
                            for (String retval : newLine.split(",")) {
                                //NonTerminal n = new NonTerminal(retval.replace(" ", ""));
                                nonterminals.add(retval.replace(" ", ""));
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
                            //Terminal t = new Terminal(newLine);
                            terminals.add(newLine);
                        } else {
                            for (String retval : newLine.split(",")) {
                                //Terminal t = new Terminal(retval.replace(" ", ""));
                                terminals.add(retval.replace(" ", ""));
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

                        atoms = newLine.split(",");
                        for (String atom : atoms) {
                            String[] atomsProd = atom.split("->");
                            Production p = new Production(atomsProd[0].replace(" ", ""), atomsProd[1].replace(" ", ""));
                            allProds.add(p);

                            if (productions.isEmpty() || !productions.containsKey(atomsProd[0].replace(" ", ""))) {
                                ArrayList<String> prods = new ArrayList<String>();
                                prods.add(p.getRight());
                                productions.put(p.getLeft(), prods);
                            } else {
                                if (productions.containsKey(atomsProd[0].replace(" ", ""))) {
                                    ArrayList<String> prods = productions.get(atomsProd[0].replace(" ", ""));
                                    prods.add(atomsProd[1].replace(" ", ""));
                                    productions.put(atomsProd[0].replace(" ", ""), prods);
                                }
                            }
                        }
                    }

                } else {
                    if (line.charAt(0) == 'S') {
                        int delim = line.indexOf('=');
                        String newStr = "";
                        initialState = newStr + line.charAt(delim + 2);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String showGrammar() {
        String res = "Non-terminals: ";
        for (String n : nonterminals) {
            res += n + ", ";
        }
        int ind = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(ind, ind + 1, "").toString();
        res += "\nTerminals: ";

        for (String t : terminals) {
            res += t + ", ";
        }
        int idx = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(idx, idx + 1, "").toString();
        res += "\nProductions: \n";

        for (Production p : allProds) {
            res += p.getLeft() + " -> " + p.getRight() + "\n";
        }
        int idxx = res.lastIndexOf(",");
        res = new StringBuilder(res).replace(idxx, idxx + 1, "").toString();
        res += "Initial State: " + initialState;
        return res;

    }

}
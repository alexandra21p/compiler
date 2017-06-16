package view;

import controller.FiniteAutomatonController;
import controller.GrammarController;


import java.util.Scanner;

/**
 * Created by Alexandra on 27/11/2016.
 */
public class View {
    GrammarController grCtrl;
    FiniteAutomatonController faCtrl;
    Scanner input;

    public View(GrammarController grCtrl, FiniteAutomatonController faCtrl) {
        this.grCtrl = grCtrl;
        this.faCtrl = faCtrl;
        input = new Scanner(System.in);
    }

    public void mainMenu() {
        System.out.println("___________________________________________\n");
        System.out.println("\tREGULAR GRAMMARS AND FINITE AUTOMATA\n");
        System.out.println("___________________________________________\n");
        System.out.println("\t1 - Grammar\n");
        System.out.println("\t2 - Finite Automaton\n");
        System.out.println("\t3 - Minilanguage to CFG\n");
        System.out.println("\t0 - Terminate the program.");
    }

    public void grammarSubMenu() {
        System.out.println("_________________\n");
        System.out.println("\tGRAMMAR\n");
        System.out.println("_________________\n");
        System.out.println("\t1 - Set of non-terminals");
        System.out.println("\t2 - Set of terminals");
        System.out.println("\t3 - Set of productions");
        System.out.println("\t4 - Productions of given non-terminal symbol");
        System.out.println("\t5 - Check if grammar is regular");
        System.out.println("\t6 - Construct the corresponding finite automaton");
        System.out.println("\t7 - Eliminate inaccessible symbols");
        System.out.println("\t0 - BACK");
    }

    public void faSubMenu() {
        System.out.println("_________________________\n");
        System.out.println("\tFINITE AUTOMATON\n");
        System.out.println("_________________________\n");
        System.out.println("\t1 - Set of states");
        System.out.println("\t2 - Alphabet");
        System.out.println("\t3 - Transitions");
        System.out.println("\t4 - Set of final state");
        System.out.println("\t5 - Construct the corresponding regular grammar");
        System.out.println("\t0 - BACK");
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        String opt = "";
        boolean stop = false;
        boolean stop2 = false;
        while (true) {
            mainMenu();
            System.out.println("Input the command: ");
            opt = input.next();
            input.nextLine();
            switch (opt) {
                case "0":
                    break;
                case "1":
                    while(true) {
                        if (!stop) {
                            System.out.println("_________________\n");
                            System.out.println("\tGRAMMAR\n");
                            System.out.println("_________________\n");
                            System.out.println("\t\tGive the name of the file: ");
                            String fileName = input.nextLine();
                            grCtrl.readGrammar(fileName);
                            stop = true;
                            //break;
                        }
                        else {
                            grammarSubMenu();
                            System.out.println("Input the command: ");
                            String cmd = input.next();
                            if (cmd.equals("0")) {
                                break;
                            }
                            if (cmd.equals("1")) {
                                System.out.println(grCtrl.showNonTerminals());
                            }
                            if (cmd.equals("2")) {
                                System.out.println(grCtrl.showTerminals());
                            }
                            if (cmd.equals("3")) {
                                System.out.println(grCtrl.showProductions());
                            }
                            if (cmd.equals("4")) {
                                System.out.println("Input the non-terminal: ");
                                String token = input.next();
                                System.out.println(grCtrl.showProdForNonTerminal(token));
                            }
                            if (cmd.equals("5")) {
                                boolean res = grCtrl.checkIfRegular();
                                if (res) { System.out.println("THE GRAMMAR IS REGULAR.");
                                }
                                else { System.out.println("THE GRAMMAR IS NOT REGULAR.");}
                            }
                            if (cmd.equals("6")) {
                                boolean res = grCtrl.checkIfRegular();
                                if (res) { System.out.println("THE GRAMMAR IS REGULAR.");
                                    System.out.println("The corresponding finite automaton is:  ");
                                    System.out.println(grCtrl.constructFA());
                                }
                                else { System.out.println("The grammar is NOT REGULAR, thus it is not possible to construct a FA.");}
                            }
                            if (cmd.equals("7")) {
                                System.out.println(grCtrl.elimInaccessible());
                            }
                            //break;
                        }
                        //break;

                    }
                case "2":
                    while(true) {

                        if (!stop2) {
                            System.out.println("_________________\n");
                            System.out.println("\tFINITE AUTOMATON\n");
                            System.out.println("_________________\n");
                            System.out.println("\t\tGive the name of the file: ");
                            String fileName = input.nextLine();
                            faCtrl.readFA(fileName);
                            stop2 = true;
                            //break;
                        } else {
                            faSubMenu();
                            System.out.println("Input the command: ");
                            String cmd = input.next();

                            if (cmd.equals("0")) {
                                break;
                            }
                            if (cmd.equals("1")) {
                                System.out.println(faCtrl.showStates());
                            }
                            if (cmd.equals("2")) {
                                System.out.println(faCtrl.showAlphabet());
                            }
                            if (cmd.equals("3")) {
                                System.out.println(faCtrl.showTransitions());
                            }
                            if (cmd.equals("4")) {
                                System.out.println(faCtrl.showFinalStates());
                            }
                            if (cmd.equals("5")) {
                                System.out.println("The corresponding regular grammar is: ");
                                System.out.println(faCtrl.constructRG());
                            }
                        }
                    }
                case "3": {
                    String filename = "minilanguage.txt";
                    grCtrl.readMinilanguage(filename);
                    System.out.println(grCtrl.showNonTerminals());
                    System.out.println(grCtrl.showTerminals());
                    //System.out.println(grCtrl.showProductions());

                }
                break;
                default:
                    System.out.println("Invalid input! Pls try again.");
                    break;
            }
        }
    }

}

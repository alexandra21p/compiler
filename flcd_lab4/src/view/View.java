package view;

import controller.Controller;

import java.util.Scanner;

/**
 * Created by Alexandra on 17/01/2017.
 */
public class View {
    Controller grCtrl;
    Scanner input;

    public View(Controller grCtrl) {
        this.grCtrl = grCtrl;
        input = new Scanner(System.in);
    }

    public void mainMenu() {
        System.out.println("___________________________________________\n");
        System.out.println("\t\t\tLL(1) PARSER\n");
        System.out.println("___________________________________________\n");
        System.out.println("\t1 - Read Grammar\n");
        System.out.println("\t0 - Terminate the program.");
        System.out.println("Input the command: ");
    }


    public void run() {
        Scanner input = new Scanner(System.in);
        mainMenu();
        String opt = input.next();

        switch (opt) {
            case "0":
                break;
            case "1":
                System.out.println("\t\tGive the name of the file: ");
                String fileName = input.next();
                grCtrl.readGrammar(fileName);
                System.out.println(grCtrl.showGrammar());
                grCtrl.constructFirstTable();
                grCtrl.printFirstTable();
                grCtrl.constructFollowTable();
                grCtrl.printFollowTable();
                grCtrl.constructLL1Table();
                grCtrl.analysis();
//            default:
//                System.out.println("Invalid input! Pls try again.");
        }

    }

}

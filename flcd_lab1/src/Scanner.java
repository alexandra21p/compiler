import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Pattern;

/**
 * Created by Alexandra on 08/11/2016.
 */
public class Scanner {


    public static void main(String[] args) throws IOException {


        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("Please insert the source program file name: ");
        String name = sc.nextLine();

        File file = new File(name + ".txt");
        System.out.println("Attempting to read from file in: "+file.getCanonicalPath());
        System.out.println(file.exists());
        java.util.Scanner input = new java.util.Scanner(file);

        File codif = new File("codification.txt");
        System.out.println("Attempting to read from file in: "+codif.getCanonicalPath());
        System.out.println(codif.exists());
        java.util.Scanner codifInput = new java.util.Scanner(codif);


        String key;

        Hashtable<String, String> codificare = new Hashtable<>();
        // the nodes will be added into an array once they're added into the ST to be able to work with positions
        ArrayList<String> nodes = new ArrayList<>();


        while (codifInput.hasNext()) {
            String tokenType = codifInput.next();
            String code = codifInput.next();
            codificare.put(tokenType, code);
        }

        String token = "";
        boolean check;

        File pif = new File("PIF.txt");
        File symTbl = new File("ST.txt");
        PrintWriter pif1 = new PrintWriter(pif, "UTF-8");
        PrintWriter symTable1 = new PrintWriter(symTbl, "UTF-8");

        BinarySearchTree symTable = new BinarySearchTree();

        int pos;

        while(input.hasNext()) {
            token = input.next();
            check = codificare.containsKey(token);
            // checks if it's a reserved word or operator
            if (check) {
                pif1.append(codificare.get(token) + " \t -1 ");
                pif1.append("\n");
            }
            else  {
                // checks if it's an identifier
                if (Pattern.matches("[a-zA-Z][a-zA-Z0-9]*", token) && token.length() <= 8) {
                    if (!symTable.exists(token)) {
                        symTable.insert(token);
                        nodes.add(token);
                        pos = nodes.indexOf(token);
                        symTable1.append(token + " \t " + pos);
                        symTable1.append("\n");
                    }
                    else {
                        pos = nodes.indexOf(token);
                    }

                    pif1.append("0  \t " + pos);
                    pif1.append("\n");

                }
                else {
                    // checks if it's a constant (integer / string / char)
                    if (Pattern.matches("0|[-|+][1-9][0-9]*|[1-9][0-9]*|\"[a-zA-Z]*\"|'[a-zA-Z]'", token)) {
                        if (!symTable.exists(token)) {
                            symTable.insert(token);
                            nodes.add(token);
                            pos = nodes.indexOf(token);
                            symTable1.append(token + " \t " + pos);
                            symTable1.append("\n");

                        }
                        else {
                            pos = nodes.indexOf(token);
                        }
                        pif1.append("1  \t " + pos);
                        pif1.append("\n");
                    }
                    else System.out.println("Lexical error at atom: " + token);
                }
            }
        }


        symTable.printPreorder(symTable.root);
        symTable1.close();
        pif1.close();
    }

}

import java.io.*;
import java.util.Scanner;

import myUtilities.myCodeGen;
import myUtilities.parser_utils.*;
import myUtilities.scanner_utils.*;

public class Main {

    //HANDLING KEYBOARD AND INDIRECTION INPUT METHOD
    public static void handleKeyboard() throws IOException {
        myScanner myScanner = new myScanner(); //the scanner module
        myParser myParser = new myParser(); //the parser module
        mySemChecker mySemChecker = new mySemChecker(); //the semantics checker module
        myCodeGen myCodeGen = new myCodeGen();
        File input = null;

        String filename = utils.writeToFile(utils.read_keyboard()); //Saving user input into a file
        try {
            if(filename != null) { //checking that the file that should have been made, was made
                input = new File(filename); //if so, open the file
                input.deleteOnExit();
            } else {
                System.out.println("MAIN: User input was not saved correctly. Aborting...");
                System.exit(-1); //the file was not made or could not be found, exiting
            }
            if(input.exists()) {
                myScanner.scan(input); //scanning file and getting token list
                Node tree = null;
                if((tree = myParser.parse(input)) != null) { //parsing the tokens produced by scanner
                    mySemChecker.checkSemantics(tree); //running the semantics checker
                    myCodeGen.generate(tree,filename); //running the code generator
                }
            }
        } catch (NullPointerException e) {
            System.out.println("MAIN: File with keyboard input could not be found!");
        }
    }

    //HANDLING FILE INPUT METHOD
    public static void handleFile(String[] args) throws IOException {
        myScanner myScanner = new myScanner(); //the scanner module
        myParser myParser = new myParser(); //the parser module
        mySemChecker mySemChecker = new mySemChecker(); //the semantics checker module
        myCodeGen myCodeGen = new myCodeGen();
        File input = null;

//      try {
//            myScanner.scan(new File(args[0])); //Scanning the provided file
//            input = new File("input.tmp"); //opening the token file created by scanner
//            input.deleteOnExit();
//            if(input.exists()) { //ensuring the file could be opened1
//                Node tree = null;
//                if((tree = myParser.parse(input)) != null) { //parsing the tokens produced by scanner
//                    mySemChecker.checkSemantics(tree); //running the semantics checker
//                    myCodeGen.generate(tree,args[0]); //running the code generator
//                }
//            } else {
//                System.out.println("MAIN: Token file could not be located. Aborting...");
//            }
//        } catch (NullPointerException e) {
//            System.out.println("MAIN: Requested file could not be found!"); //The user provided file could not be located
//        }
        myScanner.scan(new File(args[0])); //Scanning the provided file
        input = new File("input.tmp"); //opening the token file created by scanner
        input.deleteOnExit();
        if(input.exists()) { //ensuring the file could be opened1
            Node tree = null;
            if((tree = myParser.parse(input)) != null) { //parsing the tokens produced by scanner
                mySemChecker.checkSemantics(tree); //running the semantics checker
                myCodeGen.generate(tree,args[0]); //running the code generator
            }
        } else {
            System.out.println("MAIN: Token file could not be located. Aborting...");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        //Handling keyboard input as well as indirection
        if(args.length == 0) {
            handleKeyboard();
        } else if (args.length == 1) {
            handleFile(args);
        } else {
            System.out.println("MAIN: Too many command line arguments supplied."); //User added too much stuff to the command
        }
        return; //return statement just to show the end of the main file
//        //TEMPORARY TESTING FILE
//        System.out.print("Enter Test File Number: ");
//        Scanner scanner = new Scanner(System.in);
//        StringBuilder input = new StringBuilder("Tests/");
//        input.append("test_" + scanner.next() + ".txt");
//
//        String[] in = new String[1];
//        in[0] = input.toString();
//        handleFile(in);
    }
}
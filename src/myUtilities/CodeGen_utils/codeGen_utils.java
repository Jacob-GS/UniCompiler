package myUtilities.CodeGen_utils;

import myUtilities.SemChecker_utils.stackNode;
import myUtilities.parser_utils.Node;
import myUtilities.scanner_utils.Token;
import myUtilities.SemChecker_utils.myStack;

import java.io.FileWriter;
import java.util.*;

public class codeGen_utils {
    public static boolean checkInitializations(myStack stack, Token token, int depth) {
        //System.out.println("checking initialization of " + token.getInstance() + " at depth " + depth);
        stackNode newNode = new stackNode(token.getInstance().toString(), depth); //creating a new stackNode
        int status = stack.find(newNode); //see if the newly made node is in the stack
        //System.out.println("status: " + status);
        if (status >= -1 || stack.isEmpty()) { //the node exists in the stack but is NOT in the same scope or global or does not exist on the stack
            return true;
        } else if (status == -2) { //the new node is being initialized again within the same scope
            System.out.println("SEMANTICS ERROR: Variable " + token.getInstance() + " at " + token.getPosition() + " was initialized more than once within a scope.");
            return false;
        } else if (status == -3) { //the new node was initialized with the same name as a global variable
            System.out.println("SEMANTICS ERROR: Variable " + token.getInstance() + " at " + token.getPosition() + " has the same name as a global variable.");
            return false;
        } else {
            return false; //idk what happened bro ¯\_(ツ)_/¯
        }
    }

    public static String getAsmFileName (String fn) {
        //FIXING UP FILE NAME
        String asm = "";
        String[] parse = fn.split("/");
        String name = parse[parse.length-1];
        parse = name.split("\\.");
        asm = parse[0];
        if(Objects.equals(asm, "input")) {
            asm = "kb";
        }
        return asm.concat(".asm");
    }
}

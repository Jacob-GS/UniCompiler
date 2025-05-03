package myUtilities;

import myUtilities.CodeGen_utils.genStack;
import myUtilities.parser_utils.Node;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

import myUtilities.SemChecker_utils.myStack;
import myUtilities.scanner_utils.Token;
import myUtilities.CodeGen_utils.*;

public class myCodeGen {
    private int tempCount = 0;
    private int labelCount = 0;
    private FileWriter writer; //writer for writing generated code to file
    private final genStack genStack = new genStack();
    public myCodeGen() { } //default constructor
    private String saveTemp = null;
    private String prevTemp = null;
    private String makeTemp() {
        return "T" + tempCount++;
    } //creates a new temp variable string
    private String makeLabel() { return "L" + labelCount++ + ":"; }
    private String getLastLabel() { return "L" + labelCount; }
    private Boolean inConditional = false;
    private Stack<String> buffer = new Stack<>();
    private String loopLabel = "";
    private String portalLabel = "";
    private String ifLabel = "";
    private int statTracker = 0;

    public void generate(Node root, String fn) throws IOException {
        int depth = 0;

        String asm = codeGen_utils.getAsmFileName(fn);

        //creating the file writer
        try {
            writer = new FileWriter(asm);
        } catch (IOException e) {
            System.out.println("CODE GEN ERROR: Could not create file to write code to!");
        }

        //GENERATING CODE:

        //WHAT TO DO BEFORE THE PROGRAM STARTS

        traverse(root, depth);
        //WHAT TO DO AT THE END OF THE PROGRAM

        //printing STOP command to file
        writer.write("STOP\n");
        //Printing global vars
        int global_count = 0;
        for(genStackNode node : genStack.getStack()) {
            writer.write(node.tempVar + " " + node.value + "\n");
            global_count += 1; //if anything was just printed, it was a global variable and was left on the stack
        }
        for(int i = global_count; i < tempCount; i++) {
            writer.write("T" + i + " 0 \n");
        }

        writer.close(); //closing writer
        System.out.println("Code Gen: OK");

        //PRINTING THE CODE GENERATED
//        try (BufferedReader reader = new BufferedReader(new FileReader(asm))) {
//            String line;
//            System.out.println("\nGENERATED CODE FOR " + asm + ": \n==============================");
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            System.out.println("CODE GEN ERROR: Could not open file for inspection");
//        }
    }

    private void traverse(Node root, int depth) throws IOException {
        if(root == null) return;

        //going through children nodes
        for(Node child : root.getChildren()) {
            ArrayList<Token<String,String,String>> tokens = child.getTokens(); //grabbing tokens

            //doing action based on the creator
            switch (child.getCreator()) {
                case "block":
                    //THINGS TO DO WHEN ENTERING A BLOCK
                    genStack.add("BREAK", "NULL", depth, "NULL", -1);

                    traverse(child, depth + 1); //Traversing deeper into the tree

                    //THINGS TO DO WHEN LEAVING A BLOCK
                    genStackNode popping = genStack.peek();
                    while (!Objects.equals(popping.instance, "BREAK")) {
                        writer.write("POP\n"); //popping local variables off of the stack
                        genStackNode popped = genStack.pop();
                        popping = genStack.peek();
                    }
                    genStack.pop(); //pop the "BREAK" off the stack
                    break;
                case "vars":
                    //WHAT TO DO WHEN ENTERING A VARS NODE
                    //getting variable and adding it onto the stack
                    String instance = "";
                    for(Token token : root.getTokens()) { //going through the tokens associated with the current node
                        if("idTok".equals(token.getID())) { //finding the idTok tokens
                            instance = token.getInstance().toString(); //saving name
                        }
                        if("intTok".equals(token.getID())) {
                            String temp = makeTemp();
                            genStack.adjustStackPos("+"); //increment all items forward on stack
                            genStack.add(instance, temp, depth, token.getInstance().toString(), 0);
                            if(depth > 0) { //is it a local variable?
                                writer.write("PUSH\n"); //push the stack
                                writer.write("LOAD " + token.getInstance() + "\n"); //load the variable into a temp var
                                writer.write("STACKW 0\n"); //write the temp variable to the stack
                            }
                        }
                    }

                    traverse(child, depth); //Traversing deeper into the tree

                    //WHAT TO DO WHEN LEAVING A VARS NODE

                    break;
                case "expr":
                    //WHAT TO DO WHEN ENTERING A EXPR NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A EXPR NODE

                    break;
                case "exprPrime":
                    //WHAT TO DO WHEN ENTERING A EXPRPRIME NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A EXPRPRIME NODE
                    if(tokens.size() == 1 && Objects.equals(tokens.get(0).getID(), "subTok")) { //is the token a colon token?
                        writer.write("LOAD " + prevTemp + "\n"); //need to load previous working value back into ACC
                        writer.write("SUB " + saveTemp + "\n"); //div the ACC by the requested value
                    }

                    break;
                case "N":
                    //WHAT TO DO WHEN ENTERING A N NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A N NODE

                    break;
                case "NPrime":
                    //WHAT TO DO WHEN ENTERING A NPRIME NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A NPRIME NODE
                    //see what operator it is
                    if(tokens.size() == 1 && Objects.equals(tokens.get(0).getID(), "addTok")) { //is the token a colon token?
                        writer.write("LOAD " + prevTemp + "\n"); //need to load previous working value back into ACC
                        writer.write("ADD " + saveTemp + "\n"); //div the ACC by the requested value
                    } else if(tokens.size() == 1 && Objects.equals(tokens.get(0).getID(), "multiTok")) { //is the token a colon token?
                        writer.write("LOAD " + prevTemp + "\n"); //need to load previous working value back into ACC
                        writer.write("MULT " + saveTemp + "\n"); //div the ACC by the requested value
                    }

                    break;
                case "A":
                    //WHAT TO DO WHEN ENTERING AN A NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING AN A NODE

                    break;
                case "APrime":
                    //WHAT TO DO WHEN ENTERING AN APRIME NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING AN APRIME NODE
                    if(tokens.size() == 1 && Objects.equals(tokens.get(0).getID(), "divTok")) { //is the token a colon token?
                        writer.write("LOAD " + prevTemp + "\n"); //need to load previous working value back into ACC
                        writer.write("DIV " + saveTemp + "\n"); //div the ACC by the requested value
                    }

                    break;
                case "M":
                    //WHAT TO DO WHEN ENTERING A M NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A M NODE
                    if(tokens.size() == 1 && Objects.equals(tokens.get(0).getID(), "colonTok")) { //is the token a colon token?
                        writer.write("MULT -1\n"); //inverting ACC
                        writer.write("STORE " + saveTemp + "\n"); //soring the variable back into the temp var
                    }

                    break;
                case "R":
                    //WHAT TO DO WHEN ENTERING A R NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A R NODE
                    //checking to see if it is an int, Identifier, or parentheses
                    if(Objects.equals(tokens.get(0).getID(), "intTok")) {
                        //IT WAS A STATIC INTEGER
                        String temp = makeTemp();
                        if(inConditional) {
                            writer.write("SUB " + tokens.get(0).getInstance() + "\n");
                            inConditional = false;
                            while(!buffer.empty()) {
                                writer.write(buffer.pop());
                            }
                        } else {
                            writer.write("LOAD " + tokens.get(0).getInstance() + "\n"); //Write out "LOAD *number*"
                            writer.write("STORE " + temp + "\n"); //storing it into a temp variable
                        }

                        prevTemp = saveTemp;
                        saveTemp = temp;
                    } else if (Objects.equals(tokens.get(0).getID(), "idTok")) {
                        //IT WAS AN IDENTIFIER
                        if(genStack.hasInstance(tokens.get(0).getInstance())) { //does the identifier exist in the stack?
                            //check to see if it is a local or global variable
                            if(genStack.getDepth(tokens.get(0).getInstance()) > 0) {
                                //IT WAS A LOCAL VARIABLE
                                int stackPos = genStack.getALStackPos(tokens.get(0).getInstance()); //get the position on the stack
                                writer.write("STACKR " + stackPos + "\n"); //get the variable off the stack and into the ACC
                                writer.write("STORE " + genStack.getTemp(tokens.get(0).getInstance()) + "\n");
                                String temp = makeTemp(); //get a temp variable
                                prevTemp = saveTemp;
                                saveTemp = temp;
                            } else {
                                //IT WAS A GLOBAL VARIABLE
                                String temp = genStack.getTemp(tokens.get(0).getInstance()); //if so get the temp var associated with it
                                if(inConditional) {
                                    writer.write("SUB " + temp + "\n");
                                    inConditional = false;
                                    while(!buffer.empty()) {
                                        writer.write(buffer.pop());
                                    }
                                } else {
                                    writer.write("LOAD " + temp + "\n"); //writing out "LOAD IDENTIFIER"
                                }
                                prevTemp = saveTemp;
                                saveTemp = temp;
                            }
                        }
                    } else if(Objects.equals(tokens.get(0).getID(), "lParaTok")) {
                        //FIRST TOKEN WAS A PARENTHESES
                        writer.write("STORE " + saveTemp + "\n");
                    }

                    break;
                case "stats":
                    //WHAT TO DO WHEN ENTERING A STATS NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A STATS NODE

                    break;
                case "mStat":
                    //WHAT TO DO WHEN ENTERING A MSTAT NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A MSTAT NODE

                    break;
                case "stat":
                    //WHAT TO DO WHEN ENTERING A STAT NODE
                    statTracker += 1;

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A STAT NODE
                    statTracker -= 1;
                    if(statTracker == 1) {
                        if(!ifLabel.isEmpty()) {
                            writer.write("BR " + ifLabel + "\b\n");
                        }
                    }

                    break;
                case "in":
                    //WHAT TO DO WHEN ENTERING A IN NODE
                    for(Token token : child.getTokens()) {
                        if("idTok".equals(token.getID())) {
                            if(genStack.hasInstance(token.getInstance().toString())) { //does it exist in the map?
                                String temp = genStack.getTemp(token.getInstance().toString());
                                String value = genStack.getValue(token.getInstance().toString());
                                int depthInit = genStack.getDepth(token.getInstance().toString());
                                int stackPos = genStack.getALStackPos(token.getInstance().toString());
                                if(temp != null && value != null) {
                                    if(depthInit <= 0) { //depth initialized is global
                                        writer.write("READ " + temp + "\n"); //writing command
                                        writer.write("LOAD " + temp + "\n"); //loading into ACC
                                    } else { //depth initialized was local
                                        writer.write("READ " + temp + "\n"); //Reading input into temp var
                                        writer.write("LOAD "  + temp + "\n"); //loading temp var into ACC
                                        writer.write("STACKW " + stackPos + "\n" ); //saving temp variable into stack
                                    }
                                }
                            }
                        }
                    }
                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING AN IN NODE

                    break;
                case "out":
                    //WHAT TO DO WHEN ENTERING A OUT NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A OUT NODE
                    writer.write("STORE " + saveTemp + "\n"); //storing the integer into the temp variable
                    writer.write("WRITE " + saveTemp + "\n"); //writing the integer out to user

                    break;
                case "if":
                    //WHAT TO DO WHEN ENTERING A IF NODE
                    ifLabel = makeLabel();

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A IF NODE
                    writer.write(ifLabel + " NOOP\n");
                    ifLabel = "";

                    break;
                case "else":
                    //WHAT TO DO WHEN ENTERING A ELSE NODE
                    writer.write(makeLabel() + " NOOP\n");

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A ELSE NODE

                    break;
                case "loop":
                    //WHAT TO DO WHEN ENTERING A LOOP NODE
                    if(Objects.equals(child.getTokens().get(0).getID(), "whileTok")) {
                        //its a while loop
                        loopLabel = makeLabel(); //making a label for looping back to start of loop
//                        buffer.push(loopLabel + " NOOP\n"); //adding to buffer to be printed after R node
                        writer.write(loopLabel + " NOOP\n");
                    } else {
                        //it HAS to be a until loop if its not a while loop
                        loopLabel = makeLabel(); //making a label for looping back to start of loop
                        buffer.push("BRZERO " + getLastLabel() + "\n"); //break out of loop
                        buffer.push(loopLabel + " NOOP\n"); //adding to buffer to be printed after R node
                        inConditional = true;
                    }


                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A LOOP NODE
                    writer.write("BR " + loopLabel + "\b\n"); //unconditional loop back to the loopLabel
                    loopLabel = ""; //resetting loop label as it should not be used again in future while/until loops
                    writer.write(makeLabel() + " NOOP\n");

                    break;
                case "assign":
                    //WHAT TO DO WHEN ENTERING A ASSIGN NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A ASSIGN NODE
                    if(genStack.getALStackPos(tokens.get(1).getInstance()) > 0) { //check if var is local
                        //THE VARIABLE WAS LOCAL
                        String temp = genStack.getTemp(tokens.get(1).getInstance()); //get the var from stack
                        int pos = genStack.getALStackPos(tokens.get(1).getInstance());
                        writer.write("STORE " + temp + "\n");
                        writer.write("STACKW " + pos + "\n");
                    } else {
                        //THE VARIABLE WAS GLOBAL
                        String temp = genStack.getTemp(tokens.get(1).getInstance()); //get the var from stack
                        writer.write("STORE " + temp + "\n");
                    }

                    break;
                case "RO":
                    //WHAT TO DO WHEN ENTERING A RO NODE
                    //finding which relational operator was used
                    switch(tokens.get(0).getID()) {
                        case "gtTok":
                            //buffer = "BRNEG " + getLastLabel() + "\n";
                            buffer.push("BRNEG " + getLastLabel() + "\n");
                            break;
                        case "gteTok":
                            //buffer = "BRZNEG " + getLastLabel() + "\n";
                            buffer.push("BRZNEG " + getLastLabel() + "\n");
                            break;
                        case "ltTok":
                            //buffer = "BRPOS " + getLastLabel() + "\n";
                            buffer.push("BRPOS " + getLastLabel() + "\n");
                            break;
                        case "lteTok":
                            //buffer = "BRZPOS " + getLastLabel() + "\n";
                            buffer.push("BRZPOS " + getLastLabel() + "\n");
                            break;
                        case "equivTok":
                            //buffer = "BRZERO " + getLastLabel() + "\n";
                            buffer.push("BRZERO " + getLastLabel() + "\n");
                            break;
                    }

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A RO NODE
                    inConditional = true;

                    break;
                case "label":
                    //WHAT TO DO WHEN ENTERING A LABEL NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A LABEL NODE
                    portalLabel = makeLabel();
                    writer.write("BR " + portalLabel + "\b\n");

                    break;
                case "goto":
                    //WHAT TO DO WHEN ENTERING A GOTO NODE

                    traverse(child, depth);

                    //WHAT TO DO WHEN LEAVING A GOTO NODE
                    writer.write(portalLabel + " NOOP\n");
                    portalLabel = "";

                    break;
                default:
                    traverse(child,depth);
            }
        }
    }
}

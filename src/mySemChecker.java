import myUtilities.SemChecker_utils.myStack;
import myUtilities.SemChecker_utils.stackNode;
import myUtilities.parser_utils.Node;
import myUtilities.scanner_utils.Token;
import java.util.*;


public class mySemChecker {
    private int depth; //tracking the scope of the current node
    private myStack stack = new myStack(); //stack object


    public mySemChecker() { }

    public void checkSemantics(Node root) {
        traverseTree(root, 0);
        System.out.println("Semantics: OK");
    }

    private void traverseTree(Node root, int depth) {
        if(root == null) return;

        //Checking variable initialization and usage, will exit on illegal init or usage
        checkVariables(root, depth);

        //traversing to child nodes
        for(Node node : root.getChildren()) {
            if(node.getCreator().equals("block")) { //are you about to enter a <block> node?
                stack.push(new stackNode("BREAK",depth));
                depth += 1;
            }
            traverseTree(node, depth); //currently causing a stack overflow error
            if(node.getCreator().equals("block")) { //are you about to leave a <block> node?
                stackNode popping = (stackNode) stack.peek();
                while(!popping.getInstance().equals("BREAK")) {
                    //System.out.println("Popping " + popping.getInstance() + " off of the stack.");
                    stack.pop();
                    popping = (stackNode)stack.peek();
                }
                stack.pop(); //popping separator off stack
                depth--;
            }
        }
    }

    private void checkVariables(Node root, int depth) {
        //WHAT TO DO WHEN A VARIABLE IS BEING INITIALIZED
        if(Objects.equals(root.getCreator(), "vars")) {
            for(Token token : root.getTokens()) { //going through the tokens associated with the current node
                if("idTok".equals(token.getID())) { //finding the idTok tokens
                    stackNode newNode = new stackNode(token.getInstance().toString(),depth); //creating a new stackNode
                    if(!checkInitializations(token, depth)) { //check to see if the variable is legal
                        System.exit(-1); //the new variable initialization was illegal, error out. Message already printed
                    } else {
                        stack.push(newNode); //the initialization was legal, put the new variable on the stack
                        //System.out.println("Pushed var " + newNode.getInstance() + "," + newNode.getDepth() + " onto the stack");
                    }
                }
            }
        } else {
            //WHAT TO DO IF A VARIABLE IS BEING USED
            for(Token token : root.getTokens()) { //going through the tokens associated with the current node
                if("idTok".equals(token.getID())) { //finding the idTok tokens
                    stackNode newNode = new stackNode(token.getInstance().toString(),depth); //creating a new stackNode
                    int status = stack.find(newNode); //check if it's in the stack, and if so where it is

                    if(status == -1) { //the object was NOT in the stack ie using an uninitialized variable
                        //printing error and leaving program
                        System.out.println("SEMANTICS ERROR: Variable " + token.getInstance() + " at " + token.getPosition() + " has not been initialized.");
                        System.exit(-2);
                    } else {
                        //nothing to do, the variable being used has been initialized and is legal to use
                        //just left this else block for readability :-)
                    }
                }
            }
        }
    }

    private boolean checkInitializations(Token token, int depth) {
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
}

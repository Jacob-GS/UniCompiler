package myUtilities.SemChecker_utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class myStack extends Stack {
    public ArrayList<stackNode> list = new ArrayList<>(); //a list of the stack items for easy item retrieval
    public myStack() { }

    @Override
    public synchronized Object pop() {
        if(list.size() - 1 < 0) { //make sure there are items on the stack
            return -1;
        } else {
            list.remove(list.size()-1);
            return super.pop();
        }
    }

    @Override
    public Object push(Object item) {
        list.add((stackNode) item); //adding item to the list
        return super.push(item); //returning default return value of the stack push method
    }

    public int find(stackNode node) {
        for(stackNode LI : list) { //go through all items in the list
            if(Objects.equals(LI.getInstance(), node.getInstance())) { //check to see if their names are the same
                if(LI.getDepth() == node.getDepth()) { //check to see if they are in the same scope
                     return -2; //a variable was initialized twice within the same scope
                } else if(LI.getDepth() == 0) {
                    return -3; //a variable was initialized with the same name as a global variable
                } else {
                    //the node isn't initialized twice in the same scope
                    //and is not the same variable name as a global variable
                    //so return 0
                    return 0;
                }
            }
        }
        return -1; //the node is not in the stack
    }

    public void printStack() {
        for (myUtilities.SemChecker_utils.stackNode stackNode : list) {
            System.out.println(stackNode.getInstance() + " ");
        }
    }
}

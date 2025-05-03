package myUtilities.CodeGen_utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class genStack {
    private ArrayList<genStackNode> stack;
    public int size;

    public genStack () {
        stack = new ArrayList<>();
        this.size = 0;
    }

    public void add(String instance, String tempVar, int depth, String value, int ALStackPos) {
        stack.add(0, new genStackNode(instance, tempVar, depth, value, ALStackPos)); //add at front of stack
        this.size += 1;
    }

    public void printStack() {
        for(genStackNode node : stack) {
            System.out.println("[Instance: " + node.instance + ", tempVar: " + node.tempVar + ", depth: " + node.depthInitialized + ", value: " + node.value);
        }
    }

    public ArrayList<genStackNode> getStack () { return this.stack; }

    //peek at top of stack
    public genStackNode peek() { return stack.get(0); }

    public genStackNode pop() {
        return stack.remove(0);
    }

    public boolean hasInstance(String instance) {
        for(genStackNode node : stack) {
            if(node.instance.equals(instance)) return true;
        }
        return false;
    }

    public String getTemp(String instance) {
        for(genStackNode node : stack) {
            if(node.instance.equals(instance)) return node.tempVar;
        }
        return null;
    }

    public String getValue(String instance) {
        for(genStackNode node : stack) {
            if(node.instance.equals(instance)) return node.value;
        }
        return null;
    }

    public int getDepth(String instance) {
        for(genStackNode node : stack) {
            if(node.instance.equals(instance)) return node.depthInitialized;
        }
        return -1;
    }

    public int getALStackPos(String instance) {
        for(genStackNode node : stack) {
            if(node.instance.equals(instance)) return node.ALStackPos;
        }
        return -1;
    }

    public void adjustStackPos(String direction) {
        for(genStackNode node : stack) {
            if(Objects.equals(direction, "+")) {
                node.ALStackPos += 1;
            } else if (Objects.equals(direction, "-")) {
                node.ALStackPos -= 1;
            } else {
                return;
            }
        }
    }
}

package myUtilities.parser_utils;

import myUtilities.scanner_utils.Token;
import java.util.*;

public class Node {
    private ArrayList<Token<String,String,String>> tokens; //the tokens itself
    private int token_num;
    private String creator; //the function that created the node
    private ArrayList<Node> children; //any children nodes
    public int children_num;

    public Node() {}

    public Node(String creator) {
        this.tokens = new ArrayList<>();
        this.token_num = 0;
        this.creator = creator;
        this.children = new ArrayList<>();
        this.children_num = 0;
    }

    public boolean addChildren(Node child) {
        if(child == null) return false;
        children.add(child);
        this.children_num++;
        return true;
    }
    public void addToken(Token<String,String,String> token) {
        tokens.add(token);
        this.token_num++;
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public ArrayList<Token<String,String,String>> getTokens() {
        return this.tokens;
    }
    public String getCreator() { return this.creator; }

    public Node getChildAt(int num) {
        if(num > children_num) return null;
        return children.get(num);
    }
}

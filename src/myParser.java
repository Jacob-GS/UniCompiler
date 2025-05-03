import myUtilities.parser_utils.*;
import myUtilities.scanner_utils.Token;

import java.io.*;
import java.util.*;
public class myParser {
    private ArrayList<Token<String,String,String>> tokenList = new ArrayList<>(); //The array list containing the list of tokens from the parser
    private int index = 0;
    private Token<String,String,String> currentToken;

    //constructor for myParser
    public myParser(){ }

    //parses the list of tokens
    public Node parse(File file) {
        //ensuring parsing starts with a clear slate
        this.tokenList = null;
        index = 0;
        //initializing the token list and the current token
        this.tokenList = parser_utils.readTokenFile(file); //getting the list of tokens from the file
        this.currentToken = tokenList.get(index); //getting first token

        Node root = NT_program("START");
        if(currentToken == null) {
            System.out.println("Parsing: OK");
        } else {
            System.out.println("PARSER ERROR: There are still " + (tokenList.size() - index) + " tokens left to parse!");
            return null;
//            for(int i = index; i < tokenList.size(); i++) {
//                System.out.println(tokenList.get(i).toString());
//            }
        }
        //printParseTree(root,0); //printing the tree
        return root;
    }

    //gets the next token in the list
    private void nextToken() {
        if(index < tokenList.size() - 1) {
            //System.out.println("consuming: " + tokenList.get(index).toString());
            index++;
            currentToken = tokenList.get(index);
        } else {
            currentToken = null; //no more tokens to get
        }
    }

    //checks the current token and see if it matches the expected token
    private boolean match(String expectedToken, Node node) {
//        System.out.println("matching tokens: C-" + currentToken.getID() + ", " + currentToken.getInstance()
//                + " vs E-" + expectedToken);
        if(currentToken != null && currentToken.getID().equals(expectedToken)) {
            node.addToken(currentToken);
            nextToken();
            return true;
        }
        return false;
    }
    private Node NT_program(String caller) {
        //System.out.println(caller + "->program");
        Node node = new Node("program"); //creating a new node

        if(match("beginTok", node)) {
            if(node.addChildren(NT_vars(caller + "->program"))) {
                if(node.addChildren(NT_block(caller + "->program"))) {
                    if(match("endTok",node) && match("eofTok", node)) {
                        return node;
                    } else {
                        parser_error_utils.getErrorMessage(-2, currentToken); //no end token
                        return null;
                    }
                }
            }
        } else {
            parser_error_utils.getErrorMessage(-1, currentToken); //no begin token
            return null;
        }
        return null;
    }
    private Node NT_block(String caller) {
        //System.out.println(caller + "->block");
        Node node = new Node("block"); //creating a new node
        if(match("lCurlTok",node)) {
            node.addChildren(NT_vars(caller + "->block"));
            node.addChildren(NT_stats(caller + "->block"));
            if(match("rCurlTok",node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(-4, currentToken); //block does not have concluding '}'
                return null;
            }
        } else {
            parser_error_utils.getErrorMessage(-3, currentToken); //block does not have preceding '{'
            return null;
        }
    }
    private Node NT_vars(String caller) {
        //System.out.println( caller + "->vars");
        Node node = new Node("vars"); //creating a new node
        if(match("identifierTok",node)) {
            if(match("idTok",node)) {
                if(match("assignTok",node)) {
                    if(match("intTok",node) || match("floatTok",node)) {
                        if(match("semiTok",node)) {
                            if(node.addChildren(NT_vars(caller + "->vars"))) {
                                return node;
                            }
                        } else {
                            parser_error_utils.getErrorMessage(0, currentToken);
                            return null;
                        }
                    } else {
                        parser_error_utils.getErrorMessage(-8, currentToken);
                        return null;
                    }
                } else {
                    parser_error_utils.getErrorMessage(-7, currentToken);
                    return null;
                }
            } else {
                parser_error_utils.getErrorMessage(-6, currentToken);
                return null;
            }
            return null;
        }
        return node;
    }
    private Node NT_expr(String caller) {
        //System.out.println(caller + "->expr");
        Node node = new Node("expr"); //creating a new node
        node.addChildren(NT_N(caller + "->expr"));
        node.addChildren(NT_exprPrime(caller + "->expr"));
        return node;
    }
    private Node NT_exprPrime(String caller) {
        //System.out.println(caller + "->exprPprime");
        Node node = new Node("exprPrime"); //creating a new node
        if(match("subTok",node)) {
            node.addChildren(NT_N(caller + "->exprPrime"));
            node.addChildren(NT_exprPrime(caller + "->exprPrime"));
        }
        return node;
    }
    private Node NT_N(String caller) {
        //System.out.println(caller + "->N");
        Node node = new Node("N"); //creating a new node
        node.addChildren(NT_A(caller + "->N"));
        node.addChildren(NT_NPrime(caller + "->N"));
        return node;
    }
    private Node NT_NPrime(String caller) {
        //System.out.println(caller + "->NPrime");
        Node node = new Node("NPrime"); //creating a new node
        if(match("addTok",node)) {
            node.addChildren(NT_A(caller + "->NPrime"));
            node.addChildren(NT_NPrime(caller + "->NPrime"));
            return node;
        } else if (match("multiTok",node)) {
            node.addChildren(NT_A(caller + "->NPrime"));
            node.addChildren(NT_NPrime(caller + "->NPrime"));
            return node;
        }
        return node;
    }
    private Node NT_A(String caller) {
        //System.out.println(caller + "->A");
        Node node = new Node("A"); //creating a new node
        node.addChildren(NT_M(caller + "->A"));
        node.addChildren(NT_APrime(caller + "->A"));
        return node;
    }
    private Node NT_APrime(String caller) {
        //System.out.println(caller + "->A prime");
        Node node = new Node("APrime"); //creating a new node
        if(match("divTok",node)) {
            node.addChildren(NT_M(caller + "->APrime"));
            node.addChildren(NT_APrime(caller + "->APrime"));
        }
        return node;
    }
    private Node NT_M(String caller) {
        //System.out.println(caller + "->M");
        Node node = new Node("M"); //creating a new node
        if(match("colonTok",node)) {
            node.addChildren(NT_M(caller + "->M"));
        } else {
            node.addChildren(NT_R(caller + "->M"));
        }
        return node;
    }
    private Node NT_R(String caller) {
        //System.out.println(caller + "->R");
        Node node = new Node("R"); //creating a new node
        if(match("lParaTok",node)) {
            node.addChildren(NT_expr(caller + "->R"));
            if(match("rParaTok",node)) {
                return node;
            }
        } else if (match("idTok",node)) {
            return node;
        } else if (match("intTok",node) || match("floatTok",node)) {
            return node;
        }
        return null;
    }
    private Node NT_stats(String caller) {
        //System.out.println(caller + "->stats");
        Node node = new Node("stats"); //creating a new node
        node.addChildren(NT_stat(caller + "->stats"));
        node.addChildren(NT_mStat(caller + "->stats"));
        return node;
    }
    private Node NT_mStat(String caller) {
        //System.out.println(caller + "->mStat");
        Node node = new Node("mStat"); //creating a new node
        if(isStatement(currentToken.getID())){
            node.addChildren(NT_stat(caller + "->mStat"));
            node.addChildren(NT_mStat(caller + "->mStat"));
        }
        return node;
    }
    //program never makes it back to block after stat is called
    private Node NT_stat(String caller) {
        //System.out.println(caller + "->stat");
        Node node = new Node("stat"); //creating a new node

        if(Objects.equals(currentToken.getID(), "cinTok")) {
            node.addChildren(NT_in(caller + "->stat"));
            if(match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        } else if(Objects.equals(currentToken.getID(), "coutTok")) {
            node.addChildren(NT_out(caller + "->stat"));
            if(match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        } else if(Objects.equals(currentToken.getID(),"lCurlTok")) { //<block>
            node.addChildren(NT_block(caller + "->stat"));
            return node;
        } else if(Objects.equals(currentToken.getID(), "ifTok")) {
            node.addChildren(NT_if(caller + "->stat"));
            if(match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        } else if(Objects.equals(currentToken.getID(), "whileTok")) {
            node.addChildren(NT_loop(caller + "->stat"));
            if (match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        } else if(Objects.equals(currentToken.getID(), "untilTok")) {
            node.addChildren(NT_loop(caller + "->stat"));
            if (match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        } else if(Objects.equals(currentToken.getID(), "setTok")) {
            node.addChildren(NT_assign(caller + "->stat"));
            if(match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        } else if(Objects.equals(currentToken.getID(), "gatewayTok")) {
            node.addChildren(NT_goto(caller + "->stat"));
            if(match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        } else if(Objects.equals(currentToken.getID(), "portalTok")) {
            node.addChildren(NT_label(caller + "->stat"));
            if(match("semiTok", node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(0, currentToken);
            }
        }

        return null;
    }
    private Node NT_in(String caller) {
        //System.out.println(caller + "->in");
        Node node = new Node("in"); //creating a new node
        if(match("cinTok",node)) {
            if(match("idTok",node)) {
                return node;
            }
        }
        return null;
    }
    private Node NT_out(String caller) {
        //System.out.println(caller + "->out");
        Node node = new Node("out"); //creating a new node
        if(match("coutTok",node)) {
            node.addChildren(NT_expr(caller + "->out"));
            return node;
        }
        return null;
    }
    private Node NT_if(String caller) {
        //System.out.println(caller + "->if");
        Node node = new Node("if"); //creating a new node
        if(match("ifTok",node)) {
            if(match("lBracketTok",node)) {
                if(node.addChildren(NT_expr(caller + "->if"))) {
                    if(node.addChildren(NT_RO(caller + "->if"))) {
                        if(node.addChildren(NT_expr(caller + "->if"))) {
                            if(match("rBracketTok",node)) {
                                if(match("thenTok",node)) {
                                    if(node.addChildren(NT_stat(caller + "->if"))) {
                                        node.addChildren(NT_else(caller + "->if"));
                                        return node;
                                    }
                                } else {
                                    parser_error_utils.getErrorMessage(-10, currentToken);
                                    return null;
                                }
                            } else {
                                parser_error_utils.getErrorMessage(-9, currentToken);
                                return null;
                            }
                        }
                    }
                }
            } else {
                parser_error_utils.getErrorMessage(-8, currentToken);
                return null;
            }
        }
        return null;
    }
    private Node NT_else(String caller) {
        //System.out.println(caller + "->else");
        Node node = new Node("else"); //creating a new node
        if(match("elseTok",node)) {
           node.addChildren(NT_stat(caller + "->else"));
           return node;
        }
        return null;
    }
    private Node NT_loop(String caller) {
        //System.out.println(caller + "->loop");
        Node node = new Node("loop"); //creating a new node
        if(match("whileTok",node)) {
            if(match("lBracketTok",node)) {
                if(node.addChildren(NT_expr(caller + "->loop"))) {
                    if(node.addChildren(NT_RO(caller + "->loop"))) {
                        if(node.addChildren(NT_expr(caller + "->loop"))) {
                            if(match("rBracketTok",node)) {
                                node.addChildren(NT_stat(caller + "->loop"));
                                return node;
                            } else {
                                parser_error_utils.getErrorMessage(-12, currentToken);
                                return null;
                            }
                        }
                    }
                }
            } else {
                parser_error_utils.getErrorMessage(-11, currentToken);
                return null;
            }
        } else if(match("untilTok",node)) {
            if(match("lBracketTok",node)) {
                if(node.addChildren(NT_expr(caller + "->loop"))) {
                    if(match("rBracketTok",node)) {
                        node.addChildren(NT_stat(caller + "->loop"));
                        return node;
                    } else {
                        parser_error_utils.getErrorMessage(-19, currentToken);
                        return null;
                    }
                }
            } else {
                parser_error_utils.getErrorMessage(-18, currentToken);
                return null;
            }
            return node;
        }
        return null;
    }
    private Node NT_assign(String caller) {
        //System.out.println(caller + "->assign");
        Node node = new Node("assign"); //creating a new node
        if(match("setTok",node)) {
            if(match("idTok",node)) {
                if(match("assignTok",node)) {
                    if(node.addChildren(NT_expr(caller + "->assign"))) {
                        return node;
                    }
                } else {
                    parser_error_utils.getErrorMessage(-14, currentToken);
                    return null;
                }
            } else {
                parser_error_utils.getErrorMessage(-13, currentToken);
                return null;
            }
        }
        return null;
    }
    private Node NT_RO(String caller) {
        //System.out.println(caller + "->RO");
        Node node = new Node("RO"); //creating a new node
        if(match("gtTok",node)) {
            return node;
        } else if (match("ltTok",node)) {
            return node;
        } else if (match("equivTok",node)) {
            return node;
        } else if (match("gteTok",node)) {
            return node;
        } else if (match("lteTok",node)) {
            return node;
        }
        return null;
    }
    private Node NT_label(String caller) {
        //System.out.println(caller + "->label");
        Node node = new Node("label"); //creating a new node
        if(match("portalTok",node)) {
            if(match("idTok",node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(-17, currentToken);
                return null;
            }
        }
        return null;
    }
    private Node NT_goto(String caller) {
        //System.out.println(caller + "->goto");
        Node node = new Node("goto"); //creating a new node
        if(match("gatewayTok",node)) {
            if(match("idTok",node)) {
                return node;
            } else {
                parser_error_utils.getErrorMessage(-16, currentToken);
                return null;
            }
        }
        return null;
    }

    private boolean isStatement(String id) {
        switch(id) {
            case "cinTok":
            case "coutTok":
            case "lCurlTok":
            case "ifTok":
            case "setTok":
            case "whileTok":
            case "untilTok":
            case "assignTok":
            case "portalTok":
            case "gatewayTok":
                return true;
        }
        return false;
    }

    //prints the parse tree in PREORDER traversal
    public void printParseTree(Node root, int depth){
        if(root == null) return;
        //creating proper spacing to show depth
        String depthSpacing = "";
        for(int i = 0; i < depth; i++) {
            depthSpacing = depthSpacing.concat("   ");
        }
        //printing spacing and calling function
        System.out.print(depthSpacing);
        System.out.print(root.getCreator() + ": ");
        //printing tokens inside of node
        for(Token<String,String,String> token : root.getTokens()) {
            System.out.print(token.getID() + "(" + token.getInstance() + "), ");
        }
        System.out.println(""); //printing newline

        for(Node node : root.getChildren()) {
            printParseTree(node,depth+1);
        }
    }
}

package myUtilities.scanner_utils;

import java.util.Objects;
import java.util.Scanner;
import java.io.*;

public class utils {

    /*  public static String[] read_keyboard()
    INPUT: None
    OUTPUT: String array containing the strings read from keyboard
    FUNCTION: Reads input from keyboard and converts it into a string array
 */
    public static String read_keyboard(){
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        try {
            if(System.in.available() == 0) {
                return scanner.nextLine();
            } else {
                while(scanner.hasNextLine()) {
                    input.append(scanner.nextLine());
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return input.toString();
    }

    public static String writeToFile(String input) {
        try {
            FileWriter writer = new FileWriter("input.tmp");
            writer.write(input);
            writer.close();
            return "input.tmp";

        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Determines the token id of the string
     * @param tokenId The string
     * @param type the type of token
     * @return String of the corresponding keyword token if it exists
     */
    public static String getTokenID(String tokenId, String type) {
        if(Objects.equals(type, "KEYWORD"))
        {
            if(Objects.equals(tokenId, "begin")) { return "beginTok"; }
            if(Objects.equals(tokenId, "end")) { return "endTok"; }
            if(Objects.equals(tokenId, "while")) { return "whileTok"; }
            if(Objects.equals(tokenId, "until")) { return "untilTok"; }
            if(Objects.equals(tokenId, "done")) { return "doneTok"; }
            if(Objects.equals(tokenId, "gateway")) { return "gatewayTok"; }
            if(Objects.equals(tokenId, "exit")) { return "exitTok"; }
            if(Objects.equals(tokenId, "cin")) { return "cinTok"; }
            if(Objects.equals(tokenId, "cout")) { return "coutTok"; }
            if(Objects.equals(tokenId, "tape")) { return "tapeTok"; }
            if(Objects.equals(tokenId, "portal")) { return "portalTok"; }
            if(Objects.equals(tokenId, "if")) { return "ifTok"; }
            if(Objects.equals(tokenId, "then")) { return "thenTok"; }
            if(Objects.equals(tokenId, "else")) { return "elseTok"; }
            if(Objects.equals(tokenId, "identifier")) { return "identifierTok"; }
            if(Objects.equals(tokenId, "set")) { return "setTok"; }
            if(Objects.equals(tokenId, "func")) { return "funcTok"; }
        }
        if(Objects.equals(type, "OPERATOR")) return getOperatorTok(tokenId);
        if(Objects.equals(type, "ID")) return "idTok";
        if(Objects.equals(type, "INTEGER")) return "intTok";
        if(Objects.equals(type, "FLOAT")) return "floatTok";
        if(Objects.equals(type, "BRACKET")) return getBracketTok(tokenId);
        if(Objects.equals(type, "COMMA")) return "commaTok";
        if(Objects.equals(type, "COLON")) return "colonTok";
        if(Objects.equals(type, "SEMICOLON")) return "semiTok";
        if(Objects.equals(type, "ELLIPSES")) return "ellipsTok";
        if(Objects.equals(type, "DECIMAL")) return "decimalTok";

        return null;
    }

    /**
     * Determines the type of input
     * @param c the input
     * @return A string corresponding to the input type of c
     */
    public static String getType(char c) {
        if(Character.isWhitespace(c) || c == '\n') return "WHITESPACE";
        if(Character.isLetter(c)) return "LETTER";
        if(Character.isDigit(c)) return "DIGIT";
        if(c == '$') return "DOLLAR";
        if(c == '+' || c == '-' || c == '*' || c == '/' || c == '='
            || c == '<' || c == '>' || c == '^' || c == ':' || c == '&' || c == '|' || c == '!') return "OPERATOR";
        if(c == '_') return "UNDERSCORE";
        if(c == '.') return "DECIMAL";
        if(c == ',') return "COMMA";
        if(c == ';') return "SEMICOLON";
        if(c == '(' || c == ')' || c == '{' || c == '}' || c == '[' ||c == ']') return "BRACKET";
        if(c == '#') return "HASHTAG";
        return "OTHER";
    }

    /**
     * Takes in a character and returns the token name of the character if it is an operator
     * @param c operator
     * @return returns the correct token for the operator type
     */
    private static String getOperatorTok(String c) {
        if(Objects.equals(c, "+")) return "addTok";
        if(Objects.equals(c, "-")) return "subTok";
        if(Objects.equals(c, "*")) return "multiTok";
        if(Objects.equals(c, "/")) return "divTok";
        if(Objects.equals(c, "^")) return "expoTok";
        if(Objects.equals(c, "=")) return "assignTok";
        if(Objects.equals(c, "==")) return "equivTok";
        if(Objects.equals(c, "!=")) return "neTok";
        if(Objects.equals(c, ":=")) return "assignTok";
        if(Objects.equals(c, ">")) return "gtTok";
        if(Objects.equals(c, ">=")) return "gteTok";
        if(Objects.equals(c, "<")) return "ltTok";
        if(Objects.equals(c, "<=")) return "lteTok";
        if(Objects.equals(c, "||")) return "orTok";
        if(Objects.equals(c, "&&")) return "andTok";
        if(Objects.equals(c, "...")) return "ellipsTok";
        if(Objects.equals(c, ":")) return "colonTok";
        return null;
    }

    private static String getBracketTok(String c) {
        if(Objects.equals(c, "(")) return "lParaTok";
        if(Objects.equals(c, "{")) return "lCurlTok";
        if(Objects.equals(c, "[")) return "lBracketTok";
        if(Objects.equals(c, ")")) return "rParaTok";
        if(Objects.equals(c, "}")) return "rCurlTok";
        if(Objects.equals(c, "]")) return "rBracketTok";
        return null;
    }
}

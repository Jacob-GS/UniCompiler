package myUtilities.parser_utils;

import myUtilities.scanner_utils.Token;
import java.util.HashMap;
import java.util.Map;

public class parser_error_utils {
    private static final Map<Integer, String> ParserError_table = new HashMap<>();

   static {
       ParserError_table.put(0, "PARSER ERROR: Missing concluding ';'");
       ParserError_table.put(-1, "PARSER ERROR: Program must start with keyword 'begin'");
       ParserError_table.put(-2, "PARSER ERROR: Program must finish with keyword 'end'");
       ParserError_table.put(-3, "PARSER ERROR: Blocks must start with '{'");
       ParserError_table.put(-4, "PARSER ERROR: Blocks must finish with '}'");
       ParserError_table.put(-5, "PARSER ERROR: Expected Identifier initialization must have an id proceeding 'identifier' keyword");
       ParserError_table.put(-6, "PARSER ERROR: Expected Identifier initialization is missing assignment operator ':='");
       ParserError_table.put(-7, "PARSER ERROR: ExpectedIdentifier initialization failed, missing assigned value");
       ParserError_table.put(-8, "PARSER ERROR: If statement missing opening '[' wrapping expression");
       ParserError_table.put(-9, "PARSER ERROR: If statement is missing closing ']' wrapping expression");
       ParserError_table.put(-10, "PARSER ERROR: If statement is missing 'then' keyword preceding the conditional block");
       ParserError_table.put(-11, "PARSER ERROR: While statement is missing opening '[' wrapping expression");
       ParserError_table.put(-12, "PARSER ERROR: While statement is missing closing ']' wrapping expression");
       ParserError_table.put(-13, "PARSER ERROR: No id given for assignment");
       ParserError_table.put(-14, "PARSER ERROR: Assignment statement is missing '='");
       ParserError_table.put(-15, "PARSER ERROR: Unidentified expression in place of relational operator");
       ParserError_table.put(-16, "PARSER ERROR: Portal keyword must have a following id");
       ParserError_table.put(-17, "PARSER ERROR: Gateway keyword must have a following id");
       ParserError_table.put(-18, "PARSER ERROR: Until statement is missing opening '[' wrapping expression");
       ParserError_table.put(-19, "PARSER ERROR: Until statement is missing closing ']' wrapping expression");
   }

    public static void getErrorMessage(int errno, Token token) {
        if(ParserError_table.containsKey(errno)) {
            String[] pos = token.getPosition().toString().split("-");
            System.out.println(ParserError_table.get(errno) + " on line " + pos[0] + " character " + pos[1]);

            System.exit(-1);
        } else {
            System.out.println("PARSER ERROR: State passed does not have an associated message!");
        }
    }
}

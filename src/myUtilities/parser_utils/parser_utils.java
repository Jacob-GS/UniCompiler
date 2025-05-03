package myUtilities.parser_utils;

import myUtilities.scanner_utils.Token;
import java.io.*;
import java.util.*;

public class parser_utils {
    public static ArrayList<Token<String,String,String>> readTokenFile(File file) {
        ArrayList<Token<String,String,String>> tokens = new ArrayList<>();
        try {
            Scanner reader = new Scanner(file);
            while(reader.hasNext()) {
                tokens.add(new Token<>(reader.next()));
            }
            return tokens;
        } catch (IOException e) {
            System.out.println("PARSER ERROR: Could not open token file!");
            System.exit(-1);
        }
        return null;
    }
}

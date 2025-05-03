import myUtilities.scanner_utils.Token;
import myUtilities.scanner_utils.*;

import java.io.*;
import java.util.*;

import static myUtilities.scanner_utils.scanner_state_utils.*;

public class myScanner {
    private int LINE_NUMBER = 1;
    private int CHAR_NUMBER = 0;

    private final ArrayList<Token<String,String,String>> tokenList = new ArrayList<>();
    public myScanner() {

    }

    //wrapper for rest of driver function
    public void scan(File file) {
        //resetting items for scan
        tokenList.clear();
        LINE_NUMBER = 1;
        CHAR_NUMBER = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line;
            int result = 0;
            while ((line = reader.readLine()) != null) {

                //inputting the entire line into the function
                result = getToken(line);
                if(result == -1) break;
                CHAR_NUMBER = 0;
                LINE_NUMBER++;
            }
            if(result != -1) {
                tokenList.add(new Token<String, String, String>("eofTok", "EOF", (LINE_NUMBER + "-" + CHAR_NUMBER)));
                //PRINTING THE EOF TOKEN AT THE END OF THE SCANNING
                //System.out.println(tokenList.get(tokenList.size()-1));
                System.out.println("Scanning: OK");
            }
        } catch(IOException e){
            System.out.println("File " + file.getName() + " could not be located!");
        }

        try {
            //Code for testing during development
            //StringBuilder fileName = new StringBuilder("Tests/ParserTests/");
            //fileName.append(file.getName().replace(".txt", ".tok"));
            //FileWriter writer = new FileWriter(fileName.toString());
            FileWriter writer = new FileWriter("input.tmp");
            for(Token<String, String, String> token : tokenList) {
                writer.write(token + " ");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("SCANNER ERROR: Could not write tokens to file!");
        }
    }
    private int getToken(String string) {
        scanner_state_utils.State currentState = scanner_state_utils.State.S0;
        StringBuilder str = new StringBuilder();
        String in_type = new String();
        int i = 0;

        //reads entire line input
        while(i < string.length()) {
            char letter = string.charAt(i);
            in_type = utils.getType(letter); //getting input type

            //getting state from table
            currentState = getState(currentState,in_type);

            //checking that the current input is the second hashtag input in a row
            if(currentState == State.commentTok) {
                return 0; //there was a comment, block out rest of line
            }

            //checking if current state is an error state
            if(isError(currentState)) {
                System.out.println(scanner_error_utils.getErrorMessage(currentState, string, letter));
                return -1;
            }

            //checking if current state is a transitions state
            if(isTransition(currentState)) {
                str.append(letter);
                CHAR_NUMBER++;
                i++;
                continue;
            }

            //checking for final state
            if(isFinal(currentState)) {
                if(str.length() == 0) {
                    str.append(letter);
                    i++;
                }
                //creating the token
                selectAndCreateToken(currentState, str.toString().trim());


                //resetting state and string
                currentState = State.S0;
                str.setLength(0);
                CHAR_NUMBER++;
            }
        } //END OF FOR LOOP

        //cleaning strings at eol
        if(isTransition(currentState)) {
            currentState = getFinal(currentState);
        }
        if(isFinal(currentState)) {
            selectAndCreateToken(currentState, str.toString().trim());
        }
        return 0;
    }

    private void selectAndCreateToken(State currentState, String tokenString) {
        StringBuilder str = new StringBuilder(tokenString.toString().trim());
        if(str.length() == 0) return;
        switch (currentState) {
            case idTok:
                if(isKeyword(str.toString())) {
                    tokenList.add(createToken(str.toString(), "KEYWORD"));
                } else {
                    tokenList.add(createToken(str.toString(), "ID"));
                }
                break;
            case intTok:
                tokenList.add(createToken(str.toString(), "INTEGER"));
                break;
            case floatTok:
                tokenList.add(createToken(str.toString(), "FLOAT"));
                break;
            case opTok:
                tokenList.add(createToken(str.toString(), "OPERATOR"));
                break;
            case bracketTok:
                tokenList.add(createToken(str.toString(), "BRACKET"));
                break;
            case commaTok:
                tokenList.add(createToken(str.toString(), "COMMA"));
                break;
            case semiTok:
                tokenList.add(createToken(str.toString(), "SEMICOLON"));
                break;
            case ellipsTok:
                tokenList.add(createToken(str.toString(), "ELLIPSES"));
                break;
            case decimalTok:
                tokenList.add(createToken(str.toString(), "DECIMAL"));
                break;
        }

        if(tokenList.get(tokenList.size() - 1).getID() == null) {
            System.out.println(scanner_error_utils.getErrorMessage(State.ES14,str.toString(),str.charAt(str.length()-1)));
            tokenList.remove(tokenList.size() - 1);
        } else {
            //PRINTING THE TOKEN AFTER IT HAS BEEN CREATED
            //System.out.println(tokenList.get(tokenList.size() - 1));
        }
    }

    private Token<String,String,String> createToken(String str, String tokType) {
        String tokenId = utils.getTokenID(str,tokType);

        if(CHAR_NUMBER > 0) {
            return new Token<>(tokenId,str,
                    LINE_NUMBER + "-" + (CHAR_NUMBER - str.length()));
        }
        return new Token<>(tokenId,str,
                LINE_NUMBER + "-" + CHAR_NUMBER);
    }
}

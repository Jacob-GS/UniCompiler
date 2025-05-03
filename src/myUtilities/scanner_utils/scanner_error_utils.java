package myUtilities.scanner_utils;

import java.util.HashMap;
import java.util.Map;

public class scanner_error_utils extends scanner_state_utils {
    private static final Map<State, String> errorState_table = new HashMap<>();

    static {
        //Initializing the error messages
        errorState_table.put(State.ES0, "SCANNER ERROR: Could not get next state.");
        errorState_table.put(State.ES1, "SCANNER ERROR: Symbol '$' can only be at the start of an identifier.");
        errorState_table.put(State.ES2, "SCANNER ERROR: Misplaced symbol in identifier.");
        errorState_table.put(State.ES3, "SCANNER ERROR: Misplaced letter in integer.");
        errorState_table.put(State.ES4, "SCANNER ERROR: Misplaced symbol in integer.");
        errorState_table.put(State.ES5, "SCANNER ERROR: Misplaced letter in float.");
        errorState_table.put(State.ES6, "SCANNER ERROR: Misplaced symbol in float.");
        errorState_table.put(State.ES7, "SCANNER ERROR: Floating '#'. Can only be followed by itself.");
        errorState_table.put(State.ES8, "SCANNER ERROR: Floating '#'.");
        errorState_table.put(State.ES9, "SCANNER ERROR: Unrecognized Symbol.");
        errorState_table.put(State.ES10, "SCANNER ERROR: Identifiers cannot start with symbols.");
        errorState_table.put(State.ES11, "SCANNER ERROR: Floating '.'.");
        errorState_table.put(State.ES12, "SCANNER ERROR: Floating double '.'.");
        errorState_table.put(State.ES13, "SCANNER ERROR: Operators can only have length of at most 2.");
        errorState_table.put(State.ES14, "SCANNER ERROR: Unrecognized two character operator.");
    }

    /**
     * Returns the error message corresponding with the error state code
     * @param state the error state code
     * @param line the offending string
     * @param letter the offending letter
     * @return A string containing the error message
     */
    public static String getErrorMessage(State state, String line, char letter) {
        if(scanner_state_utils.isError(state)) {
            System.out.println(errorState_table.get(state) + " ( '" + letter + "' in " + line.trim() + " )\n");
            System.exit(-1);
        }
        return null;
    }

}

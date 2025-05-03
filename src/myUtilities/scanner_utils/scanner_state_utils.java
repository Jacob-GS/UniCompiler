package myUtilities.scanner_utils;

import java.util.*;

public class scanner_state_utils {
    public enum State {
        //Transition states
        S0, S1, S2, S3, S4, S5, S6, S7, S8, S9,
        //Error states
        ES0, ES1, ES2, ES3, ES4, ES5, ES6, ES7, ES8, ES9, ES10, ES11, ES12, ES13, ES14,
        //Final states
        idTok, intTok, floatTok, opTok, bracketTok,
        commentTok, ellipsTok, commaTok, semiTok, decimalTok
    }
    private static final Map<String, State> fsa_table = new HashMap<>();
    private static final Set<String> reservedKeywords = new HashSet<>();
    private static final Set<State> TransitionStates = new HashSet<>();
    private static final Set<State> ErrorStates = new HashSet<>();
    private static final Set<State> FinalStates = new HashSet<>();


    static {
        //--- FILLING THE FSA TABLE ---
        //State 0
        fsa_table.put("S0_WHITESPACE", State.S0);           fsa_table.put("S0_LETTER", State.S1);
        fsa_table.put("S0_DIGIT", State.S2);                fsa_table.put("S0_OPERATOR", State.S4);
        fsa_table.put("S0_DOLLAR", State.S1);               fsa_table.put("S0_DECIMAL", State.S6);
        fsa_table.put("S0_BRACKET", State.bracketTok);      fsa_table.put("S0_HASHTAG", State.S5);
        fsa_table.put("S0_COMMA", State.commaTok);          fsa_table.put("S0_SEMICOLON", State.semiTok);
        fsa_table.put("S0_OTHER", State.ES9);               fsa_table.put("S0_UNDERSCORE", State.ES0);

        //State 1
        fsa_table.put("S1_WHITESPACE", State.idTok);        fsa_table.put("S1_LETTER", State.S1);
        fsa_table.put("S1_DIGIT", State.S1);                fsa_table.put("S1_OPERATOR", State.idTok);
        fsa_table.put("S1_DOLLAR", State.ES1);              fsa_table.put("S1_DECIMAL", State.idTok);
        fsa_table.put("S1_BRACKET", State.idTok);           fsa_table.put("S1_HASHTAG", State.idTok);
        fsa_table.put("S1_COMMA", State.idTok);             fsa_table.put("S1_SEMICOLON", State.idTok);
        fsa_table.put("S1_OTHER", State.ES9);               fsa_table.put("S1_UNDERSCORE", State.S1);

        //State 2
        fsa_table.put("S2_WHITESPACE", State.intTok);       fsa_table.put("S2_LETTER", State.ES3);
        fsa_table.put("S2_DIGIT", State.S2);               fsa_table.put("S2_OPERATOR", State.intTok);
        fsa_table.put("S2_DOLLAR", State.ES4);              fsa_table.put("S2_DECIMAL", State.S3);
        fsa_table.put("S2_BRACKET", State.intTok);          fsa_table.put("S2_HASHTAG", State.ES2);
        fsa_table.put("S2_COMMA", State.ES4);               fsa_table.put("S2_SEMICOLON", State.intTok);
        fsa_table.put("S2_OTHER", State.ES9);               fsa_table.put("S2_UNDERSCORE", State.ES4);

        //State 3
        fsa_table.put("S3_WHITESPACE", State.floatTok);     fsa_table.put("S3_LETTER", State.ES3);
        fsa_table.put("S3_DIGIT", State.S3);                fsa_table.put("S3_OPERATOR", State.floatTok);
        fsa_table.put("S3_DOLLAR", State.ES6);              fsa_table.put("S3_DECIMAL", State.ES6);
        fsa_table.put("S3_BRACKET", State.floatTok);        fsa_table.put("S3_HASHTAG", State.ES6);
        fsa_table.put("S3_COMMA", State.ES6);               fsa_table.put("S3_SEMICOLON", State.floatTok);
        fsa_table.put("S3_OTHER", State.ES9);               fsa_table.put("S3_UNDERSCORE", State.ES6);

        //State 4
        fsa_table.put("S4_WHITESPACE", State.opTok);        fsa_table.put("S4_LETTER", State.opTok);
        fsa_table.put("S4_DIGIT", State.opTok);             fsa_table.put("S4_OPERATOR", State.S8);
        fsa_table.put("S4_DOLLAR", State.opTok);            fsa_table.put("S4_DECIMAL", State.ES11);
        fsa_table.put("S4_BRACKET", State.opTok);           fsa_table.put("S4_HASHTAG", State.ES8);
        fsa_table.put("S4_COMMA", State.ES8);               fsa_table.put("S4_SEMICOLON", State.ES8);
        fsa_table.put("S4_OTHER", State.ES9);               fsa_table.put("S4_UNDERSCORE", State.opTok);

        //State 5
        fsa_table.put("S5_WHITESPACE", State.ES8);          fsa_table.put("S5_LETTER", State.ES7);
        fsa_table.put("S5_DIGIT", State.ES7);               fsa_table.put("S5_OPERATOR", State.ES7);
        fsa_table.put("S5_DOLLAR", State.ES7);              fsa_table.put("S5_DECIMAL", State.ES7);
        fsa_table.put("S5_BRACKET", State.ES7);             fsa_table.put("S5_HASHTAG", State.commentTok);
        fsa_table.put("S5_COMMA", State.ES7);               fsa_table.put("S5_SEMICOLON", State.ES7);
        fsa_table.put("S5_OTHER", State.ES9);               fsa_table.put("S5_UNDERSCORE", State.ES7);

        //State 6
        fsa_table.put("S6_WHITESPACE", State.decimalTok);   fsa_table.put("S6_LETTER", State.decimalTok);
        fsa_table.put("S6_DIGIT", State.S3);                fsa_table.put("S6_OPERATOR", State.decimalTok);
        fsa_table.put("S6_DOLLAR", State.decimalTok);       fsa_table.put("S6_DECIMAL", State.S7);
        fsa_table.put("S6_BRACKET", State.decimalTok);      fsa_table.put("S6_HASHTAG", State.decimalTok);
        fsa_table.put("S6_COMMA", State.decimalTok);        fsa_table.put("S6_SEMICOLON", State.decimalTok);
        fsa_table.put("S6_OTHER", State.ES9);               fsa_table.put("S6_UNDERSCORE", State.decimalTok);

        //State 7
        fsa_table.put("S7_WHITESPACE", State.ES12);         fsa_table.put("S7_LETTER", State.ES12);
        fsa_table.put("S7_DIGIT", State.ES12);              fsa_table.put("S7_OPERATOR", State.ES12);
        fsa_table.put("S7_DOLLAR", State.ES12);             fsa_table.put("S7_DECIMAL", State.S9);
        fsa_table.put("S7_BRACKET", State.ES12);            fsa_table.put("S7_HASHTAG", State.ES12);
        fsa_table.put("S7_COMMA", State.ES12);              fsa_table.put("S7_SEMICOLON", State.ES12);
        fsa_table.put("S7_OTHER", State.ES9);               fsa_table.put("S7_UNDERSCORE", State.ES12);

        //State 8
        fsa_table.put("S8_WHITESPACE", State.opTok);        fsa_table.put("S8_LETTER", State.opTok);
        fsa_table.put("S8_DIGIT", State.opTok);             fsa_table.put("S8_OPERATOR", State.ES13);
        fsa_table.put("S8_DOLLAR", State.opTok);            fsa_table.put("S8_DECIMAL", State.opTok);
        fsa_table.put("S8_BRACKET", State.opTok);           fsa_table.put("S8_HASHTAG", State.opTok);
        fsa_table.put("S8_COMMA", State.opTok);             fsa_table.put("S8_SEMICOLON", State.opTok);
        fsa_table.put("S8_OTHER", State.ES9);               fsa_table.put("S8_UNDERSCORE", State.opTok);

        //State 9
        fsa_table.put("S9_WHITESPACE", State.ellipsTok);    fsa_table.put("S9_LETTER", State.ellipsTok);
        fsa_table.put("S9_DIGIT", State.ellipsTok);         fsa_table.put("S9_OPERATOR", State.ellipsTok);
        fsa_table.put("S9_DOLLAR", State.ellipsTok);        fsa_table.put("S9_DECIMAL", State.ellipsTok);
        fsa_table.put("S9_BRACKET", State.ellipsTok);       fsa_table.put("S9_HASHTAG", State.ellipsTok);
        fsa_table.put("S9_COMMA", State.ellipsTok);         fsa_table.put("S9_SEMICOLON", State.ellipsTok);
        fsa_table.put("S9_OTHER", State.ES9);               fsa_table.put("S9_UNDERSCORE", State.ellipsTok);

        //--- INITIALIZING KEYWORDS ---
        reservedKeywords.add("begin"); reservedKeywords.add("end");
        reservedKeywords.add("while"); reservedKeywords.add("until");
        reservedKeywords.add("done"); reservedKeywords.add("gateway");
        reservedKeywords.add("exit"); reservedKeywords.add("func");
        reservedKeywords.add("cin"); reservedKeywords.add("cout");
        reservedKeywords.add("tape"); reservedKeywords.add("portal");
        reservedKeywords.add("if"); reservedKeywords.add("then");
        reservedKeywords.add("else"); reservedKeywords.add("identifier");
        reservedKeywords.add("set");

        //--- INITIALIZING TRANSITION STATES SET ---
        TransitionStates.add(State.S0); TransitionStates.add(State.S1);
        TransitionStates.add(State.S2); TransitionStates.add(State.S3);
        TransitionStates.add(State.S4); TransitionStates.add(State.S5);
        TransitionStates.add(State.S6); TransitionStates.add(State.S7);
        TransitionStates.add(State.S8); TransitionStates.add(State.S9);

        //--- INITIALIZING ERROR STATES SET ---
        ErrorStates.add(State.ES1); ErrorStates.add(State.ES2);
        ErrorStates.add(State.ES3); ErrorStates.add(State.ES4);
        ErrorStates.add(State.ES5); ErrorStates.add(State.ES6);
        ErrorStates.add(State.ES7); ErrorStates.add(State.ES8);
        ErrorStates.add(State.ES9); ErrorStates.add(State.ES10);
        ErrorStates.add(State.ES11); ErrorStates.add(State.ES12);
        ErrorStates.add(State.ES0); ErrorStates.add(State.ES13);
        ErrorStates.add(State.ES14);

        //--- INITIALIZING FINAL STATES SET ---
        FinalStates.add(State.idTok); FinalStates.add(State.intTok);
        FinalStates.add(State.floatTok); FinalStates.add(State.opTok);
        FinalStates.add(State.bracketTok); FinalStates.add(State.commentTok);
        FinalStates.add(State.ellipsTok); FinalStates.add(State.commaTok);
        FinalStates.add(State.semiTok); FinalStates.add(State.decimalTok);
    }

    /**
     * Determines the next state to move to based on the current state and the input type from the FSA table
     * @param currentState The current state that the scanner is in within the FSA table
     * @param inputType The input type of the next input
     * @return The next state to move to in the table
     */
    public static State getState(State currentState, String inputType) {
        return fsa_table.getOrDefault(currentState + "_" + inputType,State.ES0);
    }

    public static boolean isKeyword(String str) {
        return reservedKeywords.contains(str);
    }

    /**
     * Determines if the input State is a transition state
     * @param state the state to evaluate
     * @return Boolean value
     */
    public static boolean isTransition(State state) {
        return TransitionStates.contains(state);
    }

    /**
     * Determines if the input State is an error state
     * @param state the state to evaluate
     * @return Boolean value
     */
    public static boolean isError(State state) {
        return ErrorStates.contains(state);
    }

    public static boolean isFinal(State state) {
        return FinalStates.contains(state);
    }

    /**
     * Handles case where token is at the end of the line and does not contain a white space to transition to
     * a final state
     * @param state the current state
     * @return returns the correct final state for the corresponding state, if none fitting, null
     */
    public static State getFinal(State state) {
        if(state == State.S1) return State.idTok;       // identifier tokens
        if(state == State.S2) return State.intTok;      // integer tokens
        if(state == State.S3) return State.floatTok;    // float tokens
        if(state == State.S4) return State.opTok;       // operator tokens of length 1
        if(state == State.S5) return State.opTok;       // operator tokens of length 2
        if(state == State.S6) return State.commentTok;  // comment Token
        if(state == State.S7) return State.commentTok;  // comment Token
        if(state == State.S8) return State.opTok;  // comment Token
        if(state == State.S9) return State.ellipsTok;  // comment Token

        return null;
    }
}

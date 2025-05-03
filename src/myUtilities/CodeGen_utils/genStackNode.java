package myUtilities.CodeGen_utils;

import java.util.ArrayList;

public class genStackNode {
    public String instance;
    public String tempVar;
    public int depthInitialized;
    public String value;
    public int ALStackPos;

    public genStackNode(String instance, String tempVar, int depth, String value, int ALStackPos) {
        this.instance = instance;
        this.tempVar = tempVar;
        this.depthInitialized = depth;
        this.value = value;
        this.ALStackPos = ALStackPos;
    }
}

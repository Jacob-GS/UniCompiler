package myUtilities.SemChecker_utils;

public class stackNode {
    private String instance; //the variable name
    public String value;
    private int depth; //the relative depth of the scope the instance is in

    public stackNode(String instance, int depth) {
        this.instance = instance;
        this.depth = depth;
    }

    public stackNode(String instance, int depth, String value) {
        this.instance = instance;
        this.depth = depth;
        this.value = value;
    }

    public void setValue(int val) { this.value = String.valueOf(val);}
    public void setValue(String val) { this.value = val;}
    public stackNode(String instnace) {
        this.instance = instnace;
    }

    public String getInstance() { return this.instance; }
    public int getDepth() { return (int)this.depth; }

    public String getValue() { return value; }
    public void setInstance(String instance) { this.instance= instance;}
}

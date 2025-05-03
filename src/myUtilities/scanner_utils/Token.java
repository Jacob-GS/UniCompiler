package myUtilities.scanner_utils;

public class Token <ID,Instance,Position> {
    private ID id; //the token id
    private Instance instance; //the actual word of the token
    private Position position; //where the word was located in the file

    public Token(ID id, Instance instance, Position pos) {
        this.id = id;
        this.instance = instance;
        this.position = pos;
    }

    public Token(Token token) {
        this.id = (ID) token.getID();
        this.instance = (Instance) token.getInstance();
        this.position = (Position) token.getPosition();
    }

    public Token(String tokenAsString) {
        //getting rid of "<" and ">"
        tokenAsString = tokenAsString.replace("<","").replace(">","");
        //splitting based on the commas
        String[] tokenSegments = tokenAsString.split(",");
        this.id = (ID) tokenSegments[0];
        this.instance = (Instance) tokenSegments[1];
        this.position = (Position) tokenSegments[2];
    }

    public ID getID () { return this.id; }
    public void setID (ID newID) { this.id = newID; }
    public Instance getInstance() { return this.instance; }
    public Position getPosition() { return this.position; }

    @Override
    public String toString(){
        return "<" + id + "," + instance + "," + position + ">";
    }

}

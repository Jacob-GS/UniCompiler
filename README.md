# The Project
This is a compiler for a self defined language written in java. It translates from the self defined language into a simplified assembly code. The assembly code produced can be ran [here](https://comp.umsl.edu/assembler/interpreter)

## Running The Project
The project can be ran either with a text document input, keyborad input, or indirection with the following commands:
file input: java -jar project4.jar <filename>
Keyboard input: java -jar project4.jar
indirection: java -jar project4.jar > <filename>

## Defining The Language
- The entire code must be preceeded with a "begin" and succeeded with a "end"
- Code must be wrapped in curly brackets "{}", this also defines local scopes
- Global variables can be placed between the begin and first {
- Lines must end with a ";"
- Defining variables: identifier var_name := expr ;
- Assignment: set var_name = expr ;
- Reading input from user: cin expr ;
- Processing outputs to user: cout expr ;
- Arithmetic Operators: +, -, *, /
- Comparison Operators: <, <=, >, >=, : (not)
- Supports parenthesis for order of operations
- While, If, and If/else blocks must end with a ;
    
**Example code:**
```
begin
identifier x := 5;
{
    while[ x < 10 ] {
        set x = x + 1;
    } ;
    cout x;
}
end
```
```
begin
identifier x := 5;
{
    if[ 0 < x ] then {
        cout x;
    }
    else {
        cout : x;
    } ;
}
end
```
More example inputs are within the Tests folder and some example outputs are in test_x.asm files within the project.

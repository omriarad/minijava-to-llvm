// PA2 pass AST reflects operator precedence
class A {
    public static void main(String [] args) {
        // these two stmts should have the same expr AST
        // b = false + true * 2 < 3 - 4 + 5 && !!!false;
        b = false < ((true * (2 < ((0-3) - (4 *5))))) && (!(!false));
    }
}

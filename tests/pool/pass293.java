// PA2 unary and binop precedence
class A {
    public static void main(String [] args) {
        // these two stmts should have the same expr AST
        b = false + true * 2 < 3 - 4 + 5 && !!!false;
        // b = false < ((true * (2 < ((0-3) - (4 *5))))) && (!(!false));
    }
}

class Unprecedented {
    public int p(){
        int x ;
        x = 0-1 + 2 * 3 * 4 < 5 < 6 < 7 && 8 - 9 && !!false + true + new Unprecedented();
        return x;
    }
}


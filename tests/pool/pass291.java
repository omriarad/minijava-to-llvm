// PA2 ref ASTs pass
class A {
    public static void main(String [] args) {
        // these two stmts should have the same expr AST
        b = false + true * 2 < 3 - 4 + 5 && !!!false;
        // b = false < ((true * (2 < ((0-3) - (4 *5))))) && (!(!false));
    }
}

class Refs {

    public bool p() {
        int[] af;
        boolean x;
        a = 1+2+3;
        x = a.b(4);
        x = a.b(5).c(5);
        // this = that;
        that = 6;
        x = a[7];
    	a[8] = c;
        a = c.p(9);
        x = a.b().c().d();
        af = new int[10];
        that = new Test();
        return that;
    }
}

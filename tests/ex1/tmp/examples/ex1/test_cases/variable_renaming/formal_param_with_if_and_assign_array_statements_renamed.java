class Main {
    public static void main(String[] args) {
        System.out.println(1);
    }
}

class A { }

class B extends A {
    int theVar;

    public int foo() {
        return theVar;
    }
}

class C extends A {
    int theVar;

    public int foo() {
        return theVar;
    }
}

class D extends C {
    public int bar(int renamedVar, int anotherVar) {
        int[] max;

        max = new int[renamedVar * anotherVar]

        if (anotherVar < renamedVar) {
            max[anotherVar] = renamedVar;
        } else {
            max[renamedVar] = anotherVar;
        }

        return max[renamedVar * anotherVar];
    }
}

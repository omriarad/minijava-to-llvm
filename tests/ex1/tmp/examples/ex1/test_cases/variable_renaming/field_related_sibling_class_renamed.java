class Main {
    public static void main(String[] args) {
        System.out.println(1);
    }
}

class Shared {
    int renamedVar;

    public int foo() {
        return renamedVar;
    }
}

class A extends Shared { }

class B extends A {
    public int foo() {
        return renamedVar;
    }
}

class C extends A {
    public int foo() {
        return renamedVar;
    }
}

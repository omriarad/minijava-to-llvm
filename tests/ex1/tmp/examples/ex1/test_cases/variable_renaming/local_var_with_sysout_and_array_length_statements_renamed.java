class Main {
    public static void main(String[] args) {
        System.out.println(1);
    }
}

class A { }

class B extends A {
    int[] theVar;

    public int foo() {
        return theVar.length;
    }
}

class C extends A {
    int[] theVar;

    public int foo() {
        return theVar.length;
    }
}

class D extends C {
    public int bar(int anotherVar) {
        int renamedVar;

        System.out.println(renamedVar[renamedVar.length] - anotherVar);
        return renamedVar.length - anotherVar;
    }

}

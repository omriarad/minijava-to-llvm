class Main {
    public static void main(String[] args) {
        System.out.println(1);
    }
}

class A {}

class B extends A {
    public int theMethod() {
        return 1;
    }
}

class C extends A {
    public int theMethod() {
        return 1;
    }
}

class D extends C {}

class E {
    D d;

    public int anotherMethod(B b) {
        d = new D();
        return b.theMethod() + d.theMethod()
    }
}

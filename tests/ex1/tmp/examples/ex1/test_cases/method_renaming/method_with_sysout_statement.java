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

class D extends C {
    public int anotherMethod(B b) {
        System.out.println(this.theMethod() - b.theMethod());
        return this.theMethod() - b.theMethod();
    }
}

class Main {
    public static void main(String[] args) {
        System.out.println(new C().run());
    }
} 

class A {
    int x;

    public int run(){
        x = 1;
        return x * 5;
    }
}

class B extends A{
    int y;

    @Override
    public int run(){
        A a;
        int res;
        y = 5;
        a = new A();
        res = a.run();
        return res * y;
    }
}

class D{
    public int run(){
        return 8;
    }
}


class E extends D{
    @Override
    public int run(){
        return 24;
    }
}

class C extends B {

    public int run(){
        B b;
        E e;
        int resB;
        int resE;
        b = new B();
        e = new E();
        resB = b.run();
        resE = e.run();
        return resB * resE;
    }
}


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
    int z;

    public int run(){
        B b;
        int res;
        z = 5;
        b = new B();
        res = b.run();
        return res * z;
    }
}



class F extends B{
    public int other(){
        return 32;
    }
}

class G extends C{
    int a;
    @Override
    public int run(){
        a = 19;
        return a;
    }
}

class H extends C{
    @Override
    public int other(){
        A a;
        B b;
        D d;
        int resA;
        int resB;
        int resD;
        int resMiddle;
        a = new A();
        b = new B();
        d = new D();
        resA = a.run();
        resB = b.run();
        resD = d.run();
        resMiddle = resA + resB;
        return resMiddle + resD;
    }
}
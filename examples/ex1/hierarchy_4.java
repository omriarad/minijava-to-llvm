class Main {
    public static void main(String[] args) {
        System.out.println(new C().run());
    }
} 

class A{
    int x;

    public int run(){
        x = 10;
        return x * x;
    }
}

class B extends A{
    @Override
    public int run(){
        A a;
        a = new A();
        return a.run();
    }

    public int other(){
        int res;
        res = new A().run();
        return res;
    }
}

class C{
    public int run(){
        return 55;
    }
    
    public int other(){
        int resA;
        int resB;
        int resC;
        resA = new A().run();
        resB = new B().run();
        resC = this.run();
        return resA + resB + resC;
    }
}
class Main {
    public static void main(String[] args) {
        System.out.println(new C().run());
    }
} 

class A {
    int x;

    public int run(int a){
        x = 1;
        return x * a;
    }
}

class B extends A{
    int y;

    @Override
    public int run(int a){
        int b;
        b = 7;
        x = 9;
        y = x * b;
        return y - a;
    }
}

class D{
    int x;

    public int run(int c){
        return c;
    }
}


class E extends D{
    @Override
    public int run(int a){
        B b;
        b = new B();
        return b.run(24) + a;
    }
}

class C extends B {
    int z;
    public int run(int z){
        x = 5;
        y = z + x;
        return y - z;
    }
}


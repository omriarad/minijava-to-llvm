class Main {
    public static void main(String[] args) {
        System.out.println(new C().run());
    }
}

class A {
    int x;
    int[] arr;
    public int run(int a){
        x = 1;
        arr = new int[10];
        arr[x] = x;
        return x * a;
    }
}

class B extends A{
    int y;

    @Override
    public int run(int a){
        int b;
        boolean bool;
        bool = true;
        b = 7;
        x = 9;
        y = x * b;
        arr = new int[30];
        while(bool){
            arr[b] = x;
        }
        return y - a;
    }
}

class D{
    int x;
    int[] arr;
    public int run(int c){
        return c;
    }
}


class E extends D{
    @Override
    public int run(int a){
        B b;
        int[] arr;
        arr = new int[30];
        arr[0] = x;
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


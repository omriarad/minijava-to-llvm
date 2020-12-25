class Main {
    public static void main(String[] args) {
        System.out.println(new Example().run());
    }
}

class Example {
    int x;

    public int run() {
        x = 0;
        return x + x;
    }

    public Example classRet(Example m, int k) {
        int x;
        x = 1;
        return m;
    }
}

class B extends Example{

    public Example classRet(C m, int k) {
        return m;
    }

    public void test() {
        Example k;
        int h;
        h=4;
        k = new Example();
        k = classRet(k, h)
        x = 4;
        return x+x;
    }

    public int run() {
        x = 2;
        return x + x;
    }

}

class C extends B{

    public int run() {
        x = 2;
        return x + x;
    }

    public void C_Test() {
        x = 7;
        return x+x;
    }
}

class D extends C{
    public void D_Test() {
        x = 23;
        return x+x;
    }
}

class E {
    int x;
    public void other() {
        x = 7;
        return x+x;
    }
}

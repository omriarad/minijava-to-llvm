class Main{
    public static void main(String[] args){
        System.out.println(new A().test());
    }
}

class A {
    boolean dup;
    int test(){
        return 5;
    }

    int test(int a){
        return a;
    }
}

class B extends A{
    int test(){
        return 3;
    }
}
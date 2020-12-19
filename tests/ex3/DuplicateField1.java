class Main{
    public static void main(String[] args){
        System.out.println(new A().test());
    }
}

class A {
    boolean dup;
    boolean dup;
    int test(){
        return 5;
    }
}

class B extends A{
    int test(){
        return 3;
    }
}
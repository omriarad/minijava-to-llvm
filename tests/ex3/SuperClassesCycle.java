class Main{
    public static void main(String[] args){
        System.out.println(new A().test());
    }
}

class A extends B {
    int test(){
        return 5;
    }
}

class B{
    int test(){
        return 3;
    }
}
class Main{
    public static void main(String[] args){
        System.out.println(new A().test());
    }
}

class A extends Main {
    int test(){
        return 5;
    }
}
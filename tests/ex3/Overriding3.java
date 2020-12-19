class Main{
    public static void main(String[] args){
        System.out.println(new A().ret());
    }
}

class A {
    A test(int a, boolean b){
        return this;
    }
    
    int ret(){
        A a;
        a = this.test(4,true);
        return 4;
    }
}

class B extends A{
    B test(int a, boolean b,int[] arr){
        return this;
    }
}
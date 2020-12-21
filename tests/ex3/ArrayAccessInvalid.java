class Main{
    public static void main(String[] args){
        System.out.println(new A().test(4));
    }
}

class A {
    boolean bool;
    int test(int num){
        int[] arr;
        int res;
        arr = new int[1];
        res = arr[2+5];
        res = arr[bool];
        return 5;
    }
}
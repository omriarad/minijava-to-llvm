public class Main {
    public static void main(String[] args){
        System.out.println(new Simple().test(-7));
    }
}

public class Simple {
    boolean cond;

    int number(int num){
        return num*2;
    }

    int test(int d){
        int a;
        int b;
        int c;
        Simple s;
        cond = false;
        a = 1;
        b = 2;
        c = 3;
        s = new Simple();
        if(!cond && a < b && b < c && d < c && !(s.number(b) < b)){
            System.out.println(1);
        } else {
            System.out.println(0);
        }
        return 5;
    }
}

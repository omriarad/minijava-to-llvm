public class Main {
    public static void main(String[] args){
        System.out.println(new Test().test());
    }
}

class Test {
    int test(){
        A a;
        B b;
        C c;
        D d;
        a = new A();
        b = new B();
        c = new C();
        d = new D();
        System.out.println(a.init());
        System.out.println(b.init());
        System.out.println(c.init());
        System.out.println(d.init());
        return 0;
    }
}

class A {
    int intField;
    boolean boolField;

    int init(){
        intField = 1;
        boolField = true;
        return intField;
    }
}

class B extends A {
    int[] arrField;

    int init(){
        intField = 2;
        boolField = true;
        arrField = new int[2];
        arrField[0] = 3;
        arrField[1] = 4;
        return intField + arrField[0] + arrField[1];
    }

}

class C extends B {
    D objField;

    int init(){
        int dNum;
        intField = 5;
        boolField = true;
        arrField = new int[2];
        arrField[0] = 6;
        arrField[1] = 7;
        objField = new D();
        dNum = objField.getNumber(boolField);
        return intField + arrField[0] + arrField[1] + dNum;
    }

    int getNumber(boolean bool){
        int res;
        if(bool){
            res = 13;
        } else {
            res = 14;
        }
        return res;
    }

}

class D extends B {
    C objField;

    int init(){
        int cNum;
        intField = 10;
        boolField = false;
        arrField = new int[2];
        arrField[0] = 11;
        arrField[1] = 12;
        objField = new C();
        cNum = objField.getNumber(boolField);
        return intField + arrField[0] + arrField[1] + cNum;
    }

    int getNumber(boolean bool){
        int res;
        if(bool){
            res = 8;
        } else {
            res = 9;
        }
        return res;
    }

}
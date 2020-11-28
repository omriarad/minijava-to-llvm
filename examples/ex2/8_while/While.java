class Main {
	public static void main(String[] a) {
	    System.out.println(new Simple().bar());
	}
}

class Simple {
	public int bar() {
	    boolean b;
	    boolean c;
	    int x;
	    
        b = false;
        c = true;

        while (b && c)
            x = 0;


	    System.out.println(x);

	    return 0;
	}
}

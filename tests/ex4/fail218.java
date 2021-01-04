// PA2 octal constant fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class Octal {
    int x;

    public int set() {
    	x =
    		0377; // disallow octal values
    	return x;
    }
}

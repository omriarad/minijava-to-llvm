// PA1 parse local decl fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class id {
    public int foo() {
        Nonesuch x[2] = 3;
    }
}



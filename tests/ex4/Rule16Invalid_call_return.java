class Main {
	public static void main(String[] args) {
		System.out.println(1);
	}
}

class A {
	int x;

	boolean b;

	public int foo() {
		x = 1;
		b = (this).foo();
		return 1;
	}

}

class B {
	public int run(int sz) {
		return 1;
	}

}


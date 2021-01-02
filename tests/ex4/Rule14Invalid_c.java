class Main {
	public static void main(String[] args) {
		System.out.println(1);
	}
}

class A {
	int x;

	B b;

	public int foo() {
		b = new B();
		x = (b).run(y);
		return 1;
	}

}

class B {
	public int run(int sz) {
		return 1;
	}

}


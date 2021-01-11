class Main {
	public static void main(String[] a) {
		System.out.println((new A()).test(new A()));
	}
}

class A {
	B b;

	public int test(A a) {
		return 5;
	}

}

class B extends A {
	A a;

	public int test(B b) {
		int res;
		res = (new Fake()).test();
		return 3;
	}

}


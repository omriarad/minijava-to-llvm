class Main {
	public static void main(String[] a) {
		System.out.println((new A()).ret());
	}
}

class A {
	A test(int a, boolean b) {
		return this;
	}

	int ret() {
		A a;
		a = (this).test(4, true, true);
		return 4;
	}

}

class B extends A {
	C test(int a, boolean b) {
		return new C();
	}

}

class C extends B {
}


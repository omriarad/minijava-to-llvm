class Main {
	public static void main(String[] a) {
		System.out.println((new A()).ret());
	}
}

class A {
	C test(int a, boolean b) {
		return new C();
	}

	int ret() {
		A a;
		a = (this).test(4, true);
		return 4;
	}

}

class B extends A {
	C test(int a, boolean b) {
		return new A();
	}

}

class C extends B {
}


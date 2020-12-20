class Main {
	public static void main(String[] a) {
		System.out.println((new A()).ret());
	}
}

class A {
	C test(int a, boolean b) {
		return new WhatAmI();
	}

	int ret() {
		WhoAmI a;
		a = (this).test(4, true);
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


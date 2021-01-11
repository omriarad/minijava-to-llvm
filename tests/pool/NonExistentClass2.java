class Main {
	public static void main(String[] a) {
		System.out.println((new A()).ret());
	}
}

class A {
	public C test(int a, boolean b) {
		return new WhatAmI();
	}

	public int ret() {
		WhoAmI a;
		a = (this).test(4, true);
		return 4;
	}

}

class B extends A {
	public C test(int a, boolean b) {
		return new C();
	}

}

class C extends B {
}


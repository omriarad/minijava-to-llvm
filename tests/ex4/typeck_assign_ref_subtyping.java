class Main {
	public static void main(String[] a) {
		System.out.println(3);
	}
}

class A {
	public int fun() {
		A x;
		x = new B();
		x = new C();
		x = new D();
		return 0;
	}

}

class B extends A {
}

class C extends B {
}

class D extends C {
}


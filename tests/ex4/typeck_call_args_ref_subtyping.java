class Main {
	public static void main(String[] a) {
		System.out.println(3);
	}
}

class A {
	public boolean fun() {
		boolean x;
		x = (this).fun2(true, new B());
		x = (this).fun2(true, new C());
		x = (this).fun2(true, new D());
		return (this).fun2(false, this);
	}

	public boolean fun2(boolean x, A y) {
		return x;
	}

}

class B extends A {
}

class C extends B {
}

class D extends C {
}


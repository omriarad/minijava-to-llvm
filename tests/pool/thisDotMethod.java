class Main {
	public static void main(String[] args) {
		System.out.println((new F()).run());
	}
}

class E {
	public int run() {
		return 0;
	}

}

class F {
	public int run() {
		int f;
		f = (this).run();
		return 0;
	}

}

class A {
	public int run() {
		return 0;
	}

}

class B extends A {
}

class C extends B {
	public int run() {
		return 0;
	}

}

class D extends A {
	public int run() {
		int d;
		d = (this).run();
		return 0;
	}

}


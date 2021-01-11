class Main {
	public static void main(String[] a) {
		System.out.println((new A()).test(new A(), new A()));
	}
}

class A {
	public int test(A formal1, A formal2) {
		return 5;
	}

}

class B extends A {
	public int test(B formal1, B formal2) {
		A a;
		int res;
		res = (a).test(new B());
		return 3;
	}

}

class C {
}


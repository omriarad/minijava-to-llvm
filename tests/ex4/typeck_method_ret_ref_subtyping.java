class Main {
	public static void main(String[] a) {
		System.out.println(3);
	}
}

class A {
	public A fun(boolean x) {
		return new B();
	}

}

class B extends A {
}


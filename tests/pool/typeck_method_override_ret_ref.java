class Main {
	public static void main(String[] a) {
		System.out.println(3);
	}
}

class A {
	public A fun(int x) {
		return this;
	}

}

class B extends A {
	public A fun(int x) {
		return this;
	}

}


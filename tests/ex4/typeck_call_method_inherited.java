class Main {
	public static void main(String[] a) {
		System.out.println(3);
	}
}

class A {
	public boolean fun() {
		int x;
		return (new B()).fun2(false, this);
	}

	public boolean fun2(boolean x, A y) {
		return x;
	}

}

class B extends A {
}


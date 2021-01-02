class Main {
	public static void main(String[] a) {
		System.out.println((new A()).test());
	}
}

class A {
	boolean dup;

	public int test() {
		return 5;
	}

	public int test(int a) {
		return a;
	}

}

class B extends A {
	public int test() {
		return 3;
	}

}


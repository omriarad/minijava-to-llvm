class Main {
	public static void main(String[] a) {
		System.out.println((new A()).test());
	}
}

class A extends B {
	public int test() {
		return 5;
	}

}

class B {
	public int test() {
		return 3;
	}

}


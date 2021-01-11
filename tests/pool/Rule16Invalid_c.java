class Main {
	public static void main(String[] args) {
		System.out.println(1);
	}
}

class A {
	public int test() {
		return 1;
	}

}

class C {
	public int test() {
		return 1;
	}

}

class B extends A {
	B theVar;

	public int foo() {
		theVar = new C();
		return 1;
	}

}


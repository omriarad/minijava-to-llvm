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

class B extends A {
	A theVar;

	public int foo() {
		theVar = new A();
		return ((theVar).test()).length;
	}

}


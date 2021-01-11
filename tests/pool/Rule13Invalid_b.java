class Main {
	public static void main(String[] args) {
		System.out.println(1);
	}
}

class A {
}

class B extends A {
	A theVar;

	public int foo() {
		return (theVar).length;
	}

}


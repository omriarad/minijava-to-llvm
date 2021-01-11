class Main {
	public static void main(String[] args) {
		System.out.println(1);
	}
}

class A {
	public int[] test() {
		int[] x;
		x = new int[2];
		x[0] = 1;
		return x;
	}

}

class B extends A {
	A theVar;

	public int foo() {
		theVar = new A();
		return ((theVar).test()).length;
	}

}


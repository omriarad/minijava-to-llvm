class Main {
	public static void main(String[] args) {
		System.out.println((new Example()).run());
	}
}

class Example {
	int x;

	public int run() {
		x = 0;
		return (x) + (x);
	}

	public int other() {
		int x;
		x = 1;
		return (x) - (1);
	}

}

class B extends D {
	public int test() {
		x = 4;
		return (x) + (x);
	}

}

class C extends B {
	public int C_Test() {
		x = 7;
		return (x) + (x);
	}

}

class D extends B {
	public int D_Test() {
		x = 23;
		return (x) + (x);
	}

}

class E {
	int x;

	public int E_Test() {
		x = 7;
		return (x) + (x);
	}

}


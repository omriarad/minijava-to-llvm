class Main {
	public static void main(String[] args) {
		System.out.println(new B().method());
	}
}

class C {
	public int c_method() {
		return 5;
	}
}

class A {
	C somefield;
	public int method() {
		return 0;
	}
}

class B extends A{
	public int method() {
		int x;
		something = New C();
		x = something.c_method();
		return x;
	}
}

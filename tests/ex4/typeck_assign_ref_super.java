class Main {
	public static void main(String[] a) {
		System.out.println(3);
	}
}

class A {
	public int fun() {
		B x;
		x = this;
		return 0;
	}

}

class B extends A {
}


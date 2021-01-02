class Main {
	public static void main(String[] a) {
		System.out.println((new Simple()).bar());
	}
}

class Simple {
	public int bar() {
		boolean b;
		boolean c;
		int x;
		b = false;
		c = true;
		while ((new Simple()).bar())
			x = 0;

		return 0;
	}

}


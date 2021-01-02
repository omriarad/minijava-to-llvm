class Main {
	public static void main(String[] a) {
		System.out.println((new Simple()).bar());
	}
}

class Simple {
	public int bar() {
		int[] x;
		int y;
		x = new int[2];
		y = (new int[2])[1];
		x[0] = 1;
		x[1] = 2;
		System.out.println(((x)[0]) + ((x)[1]));
		System.out.println(y);
		return 0;
	}

}


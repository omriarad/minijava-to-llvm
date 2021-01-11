class Main {
	public static void main(String[] args) {
		System.out.println((new Example()).run());
	}
}

class Example {
	int field;

	int x;

	public int mask() {
		x = 0;
		return (x) + (x);
	}

}


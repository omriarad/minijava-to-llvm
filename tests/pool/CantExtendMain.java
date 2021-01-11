class Main {
	public static void main(String[] a) {
		System.out.println((new A()).test());
	}
}

class A extends Main {
	public int test() {
		return 5;
	}

}


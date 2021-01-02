class Main {
	public static void main(String[] a) {
		System.out.println((new A()).test(4));
	}
}

class A {
	boolean bool;

	public int test(int num) {
		int[] arr;
		boolean res;
		res = (num).method();
		return 5;
	}

	public boolean method() {
		return true;
	}

}


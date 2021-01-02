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
		arr = new int[1];
		res = (arr)[(2) + (5)];
		res = (arr)[bool];
		return 5;
	}

}


class Main {
	public static void main(String[] a) {
		System.out.println(3);
	}
}

class A {
	int[] arr;

	public int fun() {
		arr[0] = this;
		return 0;
	}

}


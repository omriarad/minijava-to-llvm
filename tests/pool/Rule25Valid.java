class Main {
	public static void main(String[] args) {
		System.out.println((new Arr()).iota(false));
	}
}

class Arr {
	public int iota(boolean size) {
		int[] arr;
		int i;
		arr = new int[(new Arr()).foo()];
		return (arr)[0];
	}

	public int foo() {
		return 1;
	}

}


class Main {
	public static void main(String[] args) {
		System.out.println((new Arr()).access());
	}
}

class Arr {
	public int access() {
		int[] arr;
		arr = new int[5];
		return (arr)[(arr)[1]];
	}

}


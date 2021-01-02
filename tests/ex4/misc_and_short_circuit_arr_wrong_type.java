class Main {
	public static void main(String[] args) {
		System.out.println((new Checker()).check());
	}
}

class Checker {
	public int check() {
		int[] arr;
		boolean i;
		arr = new int[5];
		i = 20;
		if (((i) < ((arr).length)) && ((5) < ((arr)[i])))
			System.out.println(0);
		else
			System.out.println(1);
		return 0;
	}

}


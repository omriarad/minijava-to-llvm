class Main {
	public static void main(String[] args) {
		System.out.println((new Arr()).iota(10));
	}
}

class Arr {
	public int iota(int size) {
		int[] arr;
		int i;
		arr = new int[size];
		i = 0;
		while ((i) < ((arr).length))
			{
				arr[i] = (i) + (1);

				i = (i) + (1);

			}

		return (arr)[4];
	}

}


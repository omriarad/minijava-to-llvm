class Main {
	public static void main(String[] args) {
		System.out.println((new Arr()).printIota(10));
	}
}

class Arr {
	public int printIota(int size) {
		int[] arr;
		int i;
		arr = (this).iota(size);
		i = 0;
		while ((i) < ((arr).length))
			{
				System.out.println((arr)[i]);

				i = (i) + (1);

			}

		return 0;
	}

	public int[] iota(int size) {
		int[] arr;
		int i;
		arr = new int[size];
		i = 0;
		while ((i) < ((arr).length))
			{
				arr[i] = (i) + (1);

				i = (i) + (1);

			}

		return arr;
	}

}


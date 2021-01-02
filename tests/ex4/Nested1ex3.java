class Main {
	public static void main(String[] a) {
		System.out.println((new Simple()).bar());
	}
}

class Simple {
	public int bar() {
		boolean a;
		boolean b;
		boolean c;
		int i;
		{
			a = true;

			b = true;

			c = true;

			x = 0;

			while ((c) && (true))
				{
					i = 0;

					while ((i) < (10))
						{
							if ((((a) && (b)) && ((c) && (c))) && (true))
								x = (x) + (1);
							else
								if (b)
									x = (x) + (100);
								else
									x = (x) + (10000);

							i = (i) + (1);

						}


					if ((a) && (b))
						a = false;
					else
						if ((b) && (c))
							b = false;
						else
							c = false;

				}


			System.out.println(x);

		}
		return 0;
	}

}


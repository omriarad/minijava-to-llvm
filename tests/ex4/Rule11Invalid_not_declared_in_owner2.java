class Main {
	public static void main(String[] args) {
		System.out.println((new Classes()).run());
	}
}

class Classes {
	public int run() {
		Base b;
		Derived d;
		b = new Base();
		d = (b).notExist();
		return 0;
	}

}

class Base {
	int data;

	public int set(int x) {
		data = x;
		return data;
	}

	public int get() {
		return data;
	}

}

class Derived extends Base {
	public int set(int x) {
		data = (x) * (2);
		return data;
	}

}


class Main {
	public static void main(String[] args) {
		System.out.println((new Classes()).run());
	}
}

class Classes {
	public int run() {
		Base b;
		Derived d;
		DerivedFromDerived e;
		e = new DerivedFromDerived();
		b = new Base();
		d = new Derived();
		System.out.println((e).poop());
		System.out.println((d).poop());
		System.out.println((e).set(1));
		System.out.println((b).set(1));
		System.out.println((b).get());
		b = d;
		System.out.println((b).get());
		System.out.println((b).set(3));
		System.out.println((b).get());
		System.out.println((d).get());
		b = e;
		System.out.println((b).get());
		System.out.println((b).set(3));
		System.out.println((b).get());
		System.out.println((d).get());
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
	public int poop() {
		System.out.println(7);
		return data;
	}

	public int set(int x) {
		data = (x) * (2);
		return data;
	}

}

class DerivedFromDerived extends Derived {
	int something;

	public int get() {
		data = (data) * (4);
		something = 1;
		something = (something) + (data);
		return something;
	}

}


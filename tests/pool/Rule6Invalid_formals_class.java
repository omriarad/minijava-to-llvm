class Main {
	public static void main(String[] args) {
		System.out.println(0);
	}
}

class Parent {
	public int run(Parent x, int y) {
		return 0;
	}

}

class Child extends Parent {
	public int run(Child x, int y) {
		return 0;
	}

}


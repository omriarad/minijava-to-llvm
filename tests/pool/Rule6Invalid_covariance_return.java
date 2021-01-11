class Main {
	public static void main(String[] args) {
		System.out.println(0);
	}
}

class Base {
}

class Parent {
	public Parent run(int x, int y) {
		return 0;
	}

}

class Child extends Parent {
	public Base run(int x, int y) {
		return 0;
	}

}


class Main {
	public static void main(String[] args) {
		System.out.println(0);
	}
}

class Parent {
	public int run() {
		return 0;
	}

}

class Child extends Parent {
	public boolean run() {
		return false;
	}

}


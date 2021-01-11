class Main {
	public static void main(String[] args) {
		System.out.println((new Tester()).test(new A()));
	}
}

class Tester {
	public int test(A a) {
		System.out.println(1);
		return 0;
	}

}

class A {
	int a;

}


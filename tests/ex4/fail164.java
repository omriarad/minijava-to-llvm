// PA1 parse assign fail
// PA1 parse decl fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class Test {

    public int b(int a) {
        Test [ ]  = a * 3;
        return 1;
    }
}


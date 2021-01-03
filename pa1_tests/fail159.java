// PA1 parse method param type fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class Illegal {
    public int foo (void x) {
	x = 1;
   }
}

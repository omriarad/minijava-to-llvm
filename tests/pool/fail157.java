// PA1 parse unop fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class IllegalExpressions {
   public int foo() {
      z = a!b;
      return 1;
   }
}

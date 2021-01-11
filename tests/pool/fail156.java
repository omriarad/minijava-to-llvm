// PA1 parse expr fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class IllegalExpressions {
   public int foo (int a) {
      if (a = a) {
         a = a;
      }
      return 10;
   }
}

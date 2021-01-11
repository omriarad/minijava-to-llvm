// PA1 parse local decl fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class LValueFail {
   public int foo () {
      true = false;
      return 7;
   }
}

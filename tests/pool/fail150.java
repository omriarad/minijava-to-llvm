// PA1 parse local decl fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class idfail {
   public int foo () {
      int[] x[3];
      return 10;
   }
}

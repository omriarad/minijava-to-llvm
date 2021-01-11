// PA1 parse ref fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class IllegalExpressions {
    public boolean foo () {
    	//just in case
      a [b] [c] = d;   // not ok
      return false;
   }
}

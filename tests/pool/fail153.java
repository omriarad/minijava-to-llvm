// PA1 parse ref expr fail
class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class Test {
   public int s () {
       this.id = this.id;
       return 7;
   }
}

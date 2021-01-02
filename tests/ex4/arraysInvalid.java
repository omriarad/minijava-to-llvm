class Main {
	public static void main(String[] args) {
		System.out.println(1);
	}
}

class A {
}

class B extends A {
	int[] theVar;

	public int foo() {
		return (theVar).length;
	}

}

class C extends A {
	public int foo() {
		int[] theVar;
		theVar[0] = 5;
		theVar[(0) + (1)] = (2) + (2);
		return (theVar)[0];
	}

}

class D extends C {
	public int bar(int anotherVar) {
		System.out.println(((theVar)[(theVar).length]) - (anotherVar));
		return ((theVar).length) - (anotherVar);
	}

}


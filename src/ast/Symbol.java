package ast;

public class Symbol {
	private String name;
	private String type;
	private AstNode declRef;

	public Symbol(String name, String type, AstNode declRef) {
		this.name = name;
		this.declRef = declRef;
		if(type.equals("method") || type.equals("variable")) {
			this.type = type;
		} else {
			this.type = "N/A";
		}
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public AstNode getDeclRef() {
		return this.declRef;
	}

	@Override
	public String toString() {
		return "Symbol [name=" + name + ", type=" + type + ", declRef=" + declRef + "]";
	}
}

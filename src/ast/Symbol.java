package ast;

public class Symbol {
	private String name;
	private String kind;
	private AstNode declRef;
	
	public Symbol(String name,String kind,AstNode declRef) {
		this.name = name;
		this.declRef = declRef;
		if(kind.equals("method") || kind.equals("variable")) {
			this.kind = kind;
		} else {
			this.kind = "N/A";
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getKind() {
		return this.kind;
	}
	
	public AstNode getDeclRef() {
		return this.declRef;
	}
	
	@Override
	public String toString() {
		return "Symbol [name=" + name + ", kind=" + kind + ", declRef=" + declRef + "]";
	}
}

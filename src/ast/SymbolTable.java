package ast;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

	private Map<String,Symbol> entries;
	private SymbolTable parentTable;
	// debugging variable
	private String scopeName;

	public SymbolTable(){
		this.entries = new HashMap<String,Symbol>();
		this.parentTable = null;
		this.scopeName = "N/A";
	}

	public SymbolTable(String scopeName){
		this.entries = new HashMap<String,Symbol>();
		this.parentTable = null;
		this.scopeName = scopeName;
	}

	public void setParentTable(SymbolTable parent) {
		this.parentTable = parent;
	}

	public Map<String,Symbol> getEntries(){
		return this.entries;
	}

	public SymbolTable getParentSymbolTable() {
		return this.parentTable;
	}

	public String getScopeName() {
		return this.scopeName;
	}

	public void addEntry(String scopeName,Symbol symbol) {
		this.entries.put(scopeName,symbol);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Scope Name: "+this.scopeName + "\n");
		if(this.parentTable != null) {
			sb.append("Parent Scope: "+this.parentTable.getScopeName()+ "\n");
		} else {
			sb.append("Parent Scope:  N/A \n");
		}
		for(Symbol s : entries.values()) {
			sb.append(s.toString() + "\n");
		}
		return sb.toString();
	}

}

package ast;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

	private Map<String,SymbolTableEntry> variableEntries;
	private Map<String,SymbolTableEntry> methodEntries;
	private SymbolTable parentTable;
	private boolean isClassScope;
	// debugging variable
	private String scopeName;

	
	public SymbolTable(String scopeName,boolean isClassScope){
		this.variableEntries = new LinkedHashMap<String,SymbolTableEntry>();
		this.methodEntries = new LinkedHashMap<String,SymbolTableEntry>();
		this.parentTable = null;
		this.scopeName = scopeName;
		this.isClassScope = isClassScope;
	}

	public SymbolTable(String scopeName){
		this(scopeName,false);
	}

	public SymbolTable(){
		this("N/A",false);
	}


	public void setParentTable(SymbolTable parent) {
		this.parentTable = parent;
	}

	public Map<String,SymbolTableEntry> getMethodEntries(){
		return this.methodEntries;
	}
	
	public Map<String,SymbolTableEntry> getVarEntries(){
		return this.variableEntries;
	}

	public SymbolTable getParentSymbolTable() {
		return this.parentTable;
	}

	public String getScopeName() {
		return this.scopeName;
	}

	public boolean isClassScope(){
		return this.isClassScope;
	}

	public void addEntry(String scopeName, SymbolTableEntry symbol) {
		if(symbol.getType() == "method"){
			this.methodEntries.put(scopeName,symbol);
		} else { // symbol.getType() == "variable"
			this.variableEntries.put(scopeName,symbol);
		}
		// no error handling on mis-typed symbol
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
		for(SymbolTableEntry s : methodEntries.values()) {
			sb.append(s.toString() + "\n");
		}
		for(SymbolTableEntry s : variableEntries.values()) {
			sb.append(s.toString() + "\n");
		}
		return sb.toString();
	}

}

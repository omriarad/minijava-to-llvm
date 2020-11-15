package ast;

import java.util.*;
import java.util.Map;

public class SymbolTableLookup {
	private Map<String, Map<String,SymbolTable>> classToScopes;
	SymbolTableLookup(Map<String, Map<String,SymbolTable>> classToScopes){
		this.classToScopes = classToScopes;
	}

	SymbolTable lookup(String curClass, String curMethod, String type, String name) {
		Map<String,SymbolTable> scopeToSymbolTable = this.classToScopes.get(curClass);
		SymbolTable curSymbolTable;
		if(curMethod != null){
			curSymbolTable = scopeToSymbolTable.get(curMethod);
		}
		else{
			curSymbolTable = scopeToSymbolTable.get(curClass);
		}
		Map<String,SymbolTableEntry> curSymbolTableEntries;
		if(type.equals("variable")){
			curSymbolTableEntries = curSymbolTable.getVarEntries();
		}
		else{
			curSymbolTableEntries = curSymbolTable.getMethodEntries();
		}
		while(!curSymbolTableEntries.containsKey(name) || !curSymbolTableEntries.get(name).getType().equals(type)){
			curSymbolTable = curSymbolTable.getParentSymbolTable();
			if (curSymbolTable == null) {
				break;
			}
			if(type.equals("variable")) {
				curSymbolTableEntries = curSymbolTable.getVarEntries();
			} else {
				curSymbolTableEntries = curSymbolTable.getMethodEntries();
			}
		}
		return curSymbolTable;
	}

	boolean isAncestorSus(String curClass, String methodName, Set<String> susClasses, String susClass) {
		Map<String,SymbolTable> scopeToSymbolTable = this.classToScopes.get(curClass);
		if (scopeToSymbolTable == null) {
			return false;
		}

		SymbolTable curSymbolTable = scopeToSymbolTable.get(curClass);

		while (curSymbolTable != null) {
			if (susClasses.contains(curSymbolTable.getScopeName())) {
				SymbolTable result = this.lookup(
					curSymbolTable.getScopeName(),
					null,
					"method",
					methodName);
				if (result != null) {
					return true;
				}

				break;
			}

			curSymbolTable = curSymbolTable.getParentSymbolTable();
		}

		return false;
	}
}

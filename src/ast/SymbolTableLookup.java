package ast;

import java.util.*;
import java.util.Map;


public class SymbolTableLookup {
	private Map<String, Map<String,SymbolTable>> classToScopes;

	// Method vtables, fieldOffests & instanceSizes
	private Map<String, VTable> classToVTable;
	private Map<String, FieldOffsetsList> classToFieldOffsets;

	SymbolTableLookup(Map<String, Map<String,SymbolTable>> classToScopes){
		this.classToScopes = classToScopes;
		this.classToVTable = new HashMap<String,VTable>();
		this.classToFieldOffsets = new HashMap<String,FieldOffsetsList>();
		initFieldToOffsets();
		initVTable();
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

	boolean isField(String curClass, String curMethod, String name){
		SymbolTable lvSymbolTable = this.lookup(curClass, curMethod, "variable", name);
		return lvSymbolTable.isClassScope();
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


	AstType lookupVariableType(String curClass, String curMethod, String name) {
		SymbolTable symbolTable = this.lookup(curClass, curMethod, "variable", name);
		SymbolTableEntry symbol = symbolTable.getVarEntries().get(name);

		return ((VariableIntroduction)symbol.getDeclRef()).type();
	}

	private void initFieldToOffsets() {
		for(Map.Entry<String,Map<String,SymbolTable>> entry : classToScopes.entrySet()){
			String className = entry.getKey();
			SymbolTable classSymbolTable = classToScopes.get(className).get(className);
			FieldOffsetsList list;
			// Check if this class inherit from a parent class
			if(classSymbolTable.getParentSymbolTable() != null){
				// Build fieldOffsets from parent fieldOffsets
				FieldOffsetsList parentList = classToFieldOffsets.get(classSymbolTable.getParentSymbolTable().getScopeName());
				list = new FieldOffsetsList(parentList);
			} else {
				// Build FieldOffsets from scratch
				list = new FieldOffsetsList();
			}
			// Add new fields to the list
			Map<String,SymbolTableEntry> fields = classSymbolTable.getVarEntries();
			for(Map.Entry<String,SymbolTableEntry> field : fields.entrySet()){
				String fieldName = field.getKey();
				SymbolTableEntry symbol = field.getValue();
				AstType fieldType = ((VariableIntroduction)symbol.getDeclRef()).type();
				int fieldSize = calculateFieldSize(fieldType);
				list.addEntry(fieldName, fieldSize);
			}

			classToFieldOffsets.put(className,list);
		}
	}

	private int calculateFieldSize(AstType fieldType) {
		if(fieldType instanceof IntAstType){
			return 4;
		} else if (fieldType instanceof BoolAstType){
			return 1;
		} else {
			return 8;
		}
	}

	private void initVTable() {
		for(Map.Entry<String,Map<String,SymbolTable>> entry : classToScopes.entrySet()){
			String className = entry.getKey();
			SymbolTable classSymbolTable = classToScopes.get(className).get(className);
			VTable table;
			if(classSymbolTable.getParentSymbolTable() != null){
				// Build VTable from parent fieldOffsets
				VTable parentList = classToVTable.get(classSymbolTable.getParentSymbolTable().getScopeName());
				table = new VTable(className,parentList);
			} else {
				// Build VTable from scratch
				table = new VTable(className);
			}
			// Add new (and override) methods to the VTable
			Map<String,SymbolTableEntry> methods = classSymbolTable.getMethodEntries();
			for(Map.Entry<String,SymbolTableEntry> method : methods.entrySet()){
				String methodName = method.getKey();
				SymbolTableEntry symbol = method.getValue();
				table.addEntry(className,methodName,symbol);
			}
			
			classToVTable.put(className,table);
		}
	}



	VTable getClassVTable(String className) {
		return classToVTable.get(className);
	}

	int getFieldOffset(String className,String fieldName){
		FieldOffsetsList list = this.classToFieldOffsets.get(className);
		// Right now, error handling is as such : if found - returns offset, if not found returns -1
		return list.getOffset(fieldName);
	}

	int getInstanceSize(String className){
		FieldOffsetsList list = this.classToFieldOffsets.get(className);
		return list.getListSize();
	}
}

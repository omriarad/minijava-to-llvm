package ast;

import java.util.Map;

public class SymbolTableLookup {
    private Map<String, Map<String,SymbolTable>> classToScopes;
    SymbolTableLookup(Map<String, Map<String,SymbolTable>> classToScopes){
        this.classToScopes = classToScopes;
    }

    SymbolTable lookup(String curClass, String curMethod, String type, String name){ //curMethod == Null
        Map<String,SymbolTable> scopeToSymbolTable = this.classToScopes.get(curClass);
        SymbolTable curSymbolTable;
        if(curMethod != null){
            curSymbolTable = scopeToSymbolTable.get(curMethod);
        }
        else{
            curSymbolTable = scopeToSymbolTable.get(curClass);
        }
        Map<String,SymbolTableEntry> curSymbolTableEntries = curSymbolTable.getEntries();
        while(!curSymbolTableEntries.containsKey(name) || !curSymbolTableEntries.get(name).getType().equals(type)){
            curSymbolTable = curSymbolTable.getParentSymbolTable();
            curSymbolTableEntries = curSymbolTable.getEntries();
        }
        return curSymbolTable;
    }
}

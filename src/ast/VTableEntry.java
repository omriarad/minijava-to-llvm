package ast;

import java.util.ArrayList;
import java.util.List;

public class VTableEntry {
    private String returnType;
    private List<String> formalList;
    private String className;
    private String methodName;

    VTableEntry(String className,String methodName,AstNode method){
        this.className = className;
        this.methodName = methodName;
        AstType returnType = ((MethodDecl)method).returnType();
        this.returnType = getType(returnType);
        this.formalList = getFormalList(method);
    }

    String getFullName(){
        return "@"+className+"."+methodName;
    }

    String getMethodName(){
        return this.methodName;
    }

    String getReturnType(){
        return this.returnType;
    }
    
    List<String> getFormals(){
        return this.formalList;
    }

    void setClassName(String currClassName) {
        this.className = currClassName;
	}

    List<String> getFormalList(AstNode method) {
        List<String> results = new ArrayList<>();
        //add this as formal for all methods
        results.add("i8* %this");

        List<FormalArg> formals = ((MethodDecl)method).formals();
        for(FormalArg formal : formals){
            AstType formalType = ((VariableIntroduction)formal).type();
            String formalName =  ((VariableIntroduction)formal).name();
            results.add(getType(formalType) + " " + "."+formalName);
        }
        return results;
    }

    public List<String> getFormalsTypesList() {
        List<String> res = new ArrayList<>();
        for(String formal : this.formalList){
            res.add(formal.split(" ")[0]);
        }
        return res;
	}

    private String getType(AstType type) {
        if(type instanceof IntAstType){
            return "i32";
        } else if (type instanceof BoolAstType){
            return "i1";
        } else if (type instanceof IntArrayAstType){
            return "i32*";
        } else {
            //refType - I need to return a simple byte pointer, right ?
            return "i8*";
        }
    }

}

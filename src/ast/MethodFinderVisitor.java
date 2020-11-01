package ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodFinderVisitor implements Visitor {
    
    private Program program;
    private String searchTerm;
    private Integer searchLineNumber;
    private String currentClass;
    private String foundClass;
    private Set<String> susClasses;
    private boolean found = false;
    
    public MethodFinderVisitor(Program program,String searchTerm,Integer searchLineNumber){
    	this.program = program;
    	this.searchTerm = searchTerm;
    	this.searchLineNumber = searchLineNumber;
    	this.currentClass = "Main";
    	this.susClasses = new HashSet<String>();
    }
    
    private void generateSusSet() {
		// TODO Auto-generated method stub
    	List<ClassDecl> classes = program.classDecls();
    	Map<String,ClassDecl> potSupers = new HashMap<>();
    	int len = classes.size();
    	
    	Set<String> supers = new HashSet<>();
    	Set<String> children = new HashSet<>();
    	
    	int foundIndex = 0;
    	for(ClassDecl c : classes) {
    		if(c.name().equals(this.foundClass)) {
    			potSupers.put(c.name(), c);
    			break;
    		}
    		potSupers.put(c.name(), c);
    		foundIndex++;
    	}
    	// looking for super classes
    	// comapre
    	for(int i=foundIndex-1;i>=0;i--) {
    		ClassDecl cd = classes.get(i);
    		String foundSuper = potSupers.get(this.foundClass).superName();
    		if(foundSuper != null && cd.name().compareTo(foundSuper) == 0) {
    			supers.add(cd.name());
    			continue;
    		}
    		for(String c : supers) {
    			String currSuper = potSupers.get(c).superName();
    			if(currSuper != null && cd.name().compareTo(currSuper) == 0) {
        			supers.add(cd.name());
        			continue;
    			}
    		}
    	}
    	// looking for sub classes
    	for(int i=foundIndex+1;i<len;i++) {
    		ClassDecl cd = classes.get(i);
    		if(cd.superName() != null && cd.superName().compareTo(this.foundClass) == 0
    				|| children.contains(cd.superName())){
    			children.add(cd.name());
    		}
    	}
    	this.susClasses.addAll(supers);
    	this.susClasses.addAll(children);
    	this.susClasses.add(this.foundClass);
	}
    
    public Set<String> getSusClasses(){
    	if(found) 	return this.susClasses;
    	else return null;
    }
    
    public String getFoundClass(){
    	if(found) return this.foundClass;
    	else return null;
    }


    private void visitBinaryExpr(BinaryExpr e, String infixSymbol) {
        e.e1().accept(this);
        e.e2().accept(this);
    }


    @Override
    public void visit(Program program) {
    	this.currentClass = program.mainClass().name();
        program.mainClass().accept(this);
        for (ClassDecl classdecl : program.classDecls()) {
        	this.currentClass = classdecl.name();
            classdecl.accept(this);
        }
    }

    @Override
    public void visit(ClassDecl classDecl) {
        for (var fieldDecl : classDecl.fields()) {
            fieldDecl.accept(this);
        }
        for (var methodDecl : classDecl.methoddecls()) {
            methodDecl.accept(this);
        }
    }

    @Override
    public void visit(MainClass mainClass) {
        mainClass.mainStatement().accept(this);
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        methodDecl.returnType().accept(this);
        if(methodDecl.name().compareTo(this.searchTerm) == 0
        		&& methodDecl.lineNumber == this.searchLineNumber 
        		&& !this.found) {
        	this.found = true;
        	this.foundClass = this.currentClass;
        	this.generateSusSet();
        }

        for (var formal : methodDecl.formals()) {
            formal.accept(this);
        }

        for (var varDecl : methodDecl.vardecls()) {
            varDecl.accept(this);
        }
        for (var stmt : methodDecl.body()) {
            stmt.accept(this);
        }

        methodDecl.ret().accept(this);
    }


	@Override
    public void visit(FormalArg formalArg) {
        formalArg.type().accept(this);
    }

    @Override
    public void visit(VarDecl varDecl) {
        varDecl.type().accept(this);
    }

    @Override
    public void visit(BlockStatement blockStatement) {
        for (var s : blockStatement.statements()) {
            s.accept(this);
        }
    }

    @Override
    public void visit(IfStatement ifStatement) {
        ifStatement.cond().accept(this);
        ifStatement.thencase().accept(this);
        ifStatement.elsecase().accept(this);
    }

    @Override
    public void visit(WhileStatement whileStatement) {
        whileStatement.cond().accept(this);
        whileStatement.body().accept(this);
    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {
        sysoutStatement.arg().accept(this);
    }

    @Override
    public void visit(AssignStatement assignStatement) {
        assignStatement.rv().accept(this);
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        assignArrayStatement.index().accept(this);
        assignArrayStatement.rv().accept(this);
    }

    @Override
    public void visit(AndExpr e) {
        visitBinaryExpr(e, "&&");
    }

    @Override
    public void visit(LtExpr e) {
        visitBinaryExpr(e, "<");;
    }

    @Override
    public void visit(AddExpr e) {
        visitBinaryExpr(e, "+");;
    }

    @Override
    public void visit(SubtractExpr e) {
        visitBinaryExpr(e, "-");
    }

    @Override
    public void visit(MultExpr e) {
        visitBinaryExpr(e, "*");
    }

    @Override
    public void visit(ArrayAccessExpr e) {
        e.arrayExpr().accept(this);
        e.indexExpr().accept(this);
    }

    @Override
    public void visit(ArrayLengthExpr e) {
        e.arrayExpr().accept(this);
    }

    @Override
    public void visit(MethodCallExpr e) {
        e.ownerExpr().accept(this);

        for (Expr arg : e.actuals()) {
            arg.accept(this);
        }
        
    }

    @Override
    public void visit(IntegerLiteralExpr e) {
        //builder.append(e.num());
    }

    @Override
    public void visit(TrueExpr e) {
        //builder.append("true");
    }

    @Override
    public void visit(FalseExpr e) {
        //builder.append("false");
    }

    @Override
    public void visit(IdentifierExpr e) {
        //builder.append(e.id());
    }

    public void visit(ThisExpr e) {
        //builder.append("this");
    }

    @Override
    public void visit(NewIntArrayExpr e) {
        //builder.append("new int[");
        e.lengthExpr().accept(this);
        //builder.append("]");
    }

    @Override
    public void visit(NewObjectExpr e) {
//        builder.append("new ");
//        builder.append(e.classId());
//        builder.append("()");
    }

    @Override
    public void visit(NotExpr e) {
//        builder.append("!(");
        e.e().accept(this);
//        builder.append(")");
    }

    @Override
    public void visit(IntAstType t) {
//        builder.append("int");
    }

    @Override
    public void visit(BoolAstType t) {
//        builder.append("boolean");
    }

    @Override
    public void visit(IntArrayAstType t) {
//        builder.append("int[]");
    }

    @Override
    public void visit(RefType t) {
//        builder.append(t.id());
    }
}

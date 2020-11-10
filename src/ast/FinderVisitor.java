package ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FinderVisitor implements Visitor {

	// General fields
	private Program program;
	private String searchTerm;
	private Integer searchLineNumber;

	// Context fields
	private String currentClass;
	private boolean found = false;
	private SymbolTable currentSymbolTable;

	// For method finding
	private Set<String> susClasses;
	private String foundClass;

	// For variable finding
	private SymbolTable foundSymbolTable;

	// Symbol tables
	private Map<String, Map<String, SymbolTable>> classToScopes;

	public FinderVisitor(Program program, String searchTerm, Integer searchLineNumber) {
		this.program = program;
		this.searchTerm = searchTerm;
		this.searchLineNumber = searchLineNumber;
		this.currentClass = "Main";
		this.susClasses = new HashSet<String>();
		this.classToScopes = new HashMap<String,Map<String,SymbolTable>>();
		// currentSymbolTable will be initialized when calling mainClass.accept()
		this.currentSymbolTable = null;
	}

	private void generateSusSet() {
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
		if (found)
			return this.susClasses;
		else
			return null;
	}

	public String getFoundClass(){
		if (found)
			return this.foundClass;
		else
			return null;
	}

	public SymbolTable getFoundSymbolTable() {
		return this.foundSymbolTable;
	}

	public Map<String,Map<String,SymbolTable>> getClassToScopes() {
		return this.classToScopes;
	}

	private void visitBinaryExpr(BinaryExpr e, String infixSymbol) {
		e.e1().accept(this);
		e.e2().accept(this);
	}

	public void addClassScope(ClassDecl classdecl) {
		Map<String,SymbolTable> classScopes =  new HashMap<String,SymbolTable>();
		SymbolTable classSymbolTable = new SymbolTable(classdecl.name());
		String className = classdecl.name();
		String superName = classdecl.superName();

		/*
		 * set new class symbol table's parent pointer to the super class' symbol table.
		 * if no super class exists then set to null
		 * if one exists, minJava rules that it's class scope was previously defined in classToScopes,
		 * hence we can fetch it.
		 */
		if(superName == null) {
			classSymbolTable.setParentTable(null);
		} else {
			SymbolTable parentSymbolTable = this.classToScopes.get(superName).get(superName);
			classSymbolTable.setParentTable(parentSymbolTable);
		}
		// add the new class SymbolTable to it's scopes
		classScopes.put(className, classSymbolTable);
		// add class scopes to the general map.
		this.classToScopes.put(className,classScopes);
	}

	private void addMethodScope(Map<String, SymbolTable> classScopes, MethodDecl methodDecl,SymbolTable parentScope) {
		SymbolTable methodSymbolTable = new SymbolTable(methodDecl.name());
		methodSymbolTable.setParentTable(parentScope);
		classScopes.put(methodDecl.name(),methodSymbolTable);
	}

	@Override
	public void visit(Program program) {
		this.currentClass = program.mainClass().name();
		// add main Scope (little different than a regular class)
		Map<String,SymbolTable> mainScopes =  new HashMap<String,SymbolTable>();
		mainScopes.put(program.mainClass().name(),new SymbolTable(program.mainClass().name()));
		this.classToScopes.put(program.mainClass().name(),mainScopes);
		this.currentSymbolTable = this.classToScopes.get(this.currentClass).get(this.currentClass);
		program.mainClass().accept(this);

		for (ClassDecl classdecl : program.classDecls()) {
			this.currentClass = classdecl.name();
			this.addClassScope(classdecl);
			this.currentSymbolTable = this.classToScopes.get(this.currentClass).get(this.currentClass);
			classdecl.accept(this);
		}
	}

	@Override
	public void visit(ClassDecl classDecl) {
		Map<String,SymbolTable> classScopes = this.classToScopes.get(classDecl.name());
		/*
		 * currClassScope equals this.currentSymbolTable up until we're done with fieldDecls,
		 * but is kept to pass as parent when changing currentSymbolTable when dealing with methods.
		 */
		SymbolTable currClassScope = classScopes.get(classDecl.name());

		for (var fieldDecl : classDecl.fields()) {
			fieldDecl.accept(this);
		}

		for (var methodDecl : classDecl.methoddecls()) {
			addMethodScope(classScopes, methodDecl, currClassScope);
			this.currentSymbolTable = classScopes.get(methodDecl.name());
			methodDecl.accept(this);
		}
	}

	@Override
	public void visit(MainClass mainClass) {
		mainClass.mainStatement().accept(this);
	}

	private SymbolTable getCurrentClassTable() {
		Map<String,SymbolTable> classScopes = this.classToScopes.get(this.currentClass);
		SymbolTable currClassScope = classScopes.get(this.currentClass);

		return currClassScope;
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		Symbol symbol = new Symbol(methodDecl.name(), "method", methodDecl);

		this.getCurrentClassTable().addEntry(symbol.getName(), symbol);
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

	private void checkSusVariable(VariableIntroduction variable) {
		if(variable.name().compareTo(this.searchTerm) == 0
				&& variable.lineNumber == this.searchLineNumber) {
			this.foundSymbolTable = this.currentSymbolTable;
		}
	}

	@Override
	public void visit(FormalArg formalArg) {
		Symbol symbol = new Symbol(formalArg.name(), "variable", formalArg);
		this.currentSymbolTable.addEntry(symbol.getName(), symbol);
		this.checkSusVariable(formalArg);
		formalArg.type().accept(this);
	}

	@Override
	public void visit(VarDecl varDecl) {
		Symbol symbol = new Symbol(varDecl.name(), "variable", varDecl);
		this.currentSymbolTable.addEntry(symbol.getName(), symbol);
		this.checkSusVariable(varDecl);
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
	public void visit(IntegerLiteralExpr e) {}

	@Override
	public void visit(TrueExpr e) {}

	@Override
	public void visit(FalseExpr e) {}

	@Override
	public void visit(IdentifierExpr e) {}

	@Override
	public void visit(ThisExpr e) {}

	@Override
	public void visit(NewIntArrayExpr e) {
		e.lengthExpr().accept(this);
	}

	@Override
	public void visit(NewObjectExpr e) {}

	@Override
	public void visit(NotExpr e) {
		e.e().accept(this);
	}

	@Override
	public void visit(IntAstType t) {}

	@Override
	public void visit(BoolAstType t) {}

	@Override
	public void visit(IntArrayAstType t) {}

	@Override
	public void visit(RefType t) {}
}

package ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FinderVisitor implements Visitor {

	// General fields
	private Program program;
	private String searchTerm;
	private Integer searchLineNumber;
	private String mainClassName;

	// Context fields
	private String currentClass;
	private String currentMethod;
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
		this.classToScopes = new LinkedHashMap<String,Map<String,SymbolTable>>();
		// currentSymbolTable will be initialized when calling mainClass.accept()
		this.currentSymbolTable = null;
	}

	private void throwCompilationError(String errorMessage) {
        throw new AssertionError("ERROR: "+ errorMessage);
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

	public String getMainClassName(){
		return this.mainClassName;
	}

	private void visitBinaryExpr(BinaryExpr e, String infixSymbol) {
		e.e1().accept(this);
		e.e2().accept(this);
	}

	public void addClassScope(ClassDecl classdecl) {
		Map<String,SymbolTable> classScopes =  new HashMap<String,SymbolTable>();
		SymbolTable classSymbolTable = new SymbolTable(classdecl.name(),true);
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
		this.mainClassName = program.mainClass().name();
		// add main Scope (little different than a regular class)
		Map<String,SymbolTable> mainScopes =  new HashMap<String,SymbolTable>();
		mainScopes.put(program.mainClass().name(),new SymbolTable(program.mainClass().name(),true));
		this.classToScopes.put(program.mainClass().name(),mainScopes);
		this.currentSymbolTable = this.classToScopes.get(this.currentClass).get(this.currentClass);
		program.mainClass().accept(this);

		for (ClassDecl classdecl : program.classDecls()) {
			this.currentClass = classdecl.name();
			// checkDuplicateClass name has to be here -> before we create a scope
			// checkSuperClassDefined may be moved to the ClassDecl visit if needed
			if(checkDuplicateClassName(classdecl) || !checkSuperClassDefined(classdecl)){
				throwCompilationError("Error on class definition of "+classdecl.name());
			}
			this.addClassScope(classdecl);
			this.currentSymbolTable = this.classToScopes.get(this.currentClass).get(this.currentClass);
			classdecl.accept(this);
		}
	}

	// Check 3
	// Returns TRUE if class name is a duplicate
	private boolean checkDuplicateClassName(ClassDecl classdecl) {
		return this.classToScopes.get(classdecl.name()) != null;
	}

	// Checks 1 , 2
	// Returns TRUE if a super class was defined properly, or it was not set
	private boolean checkSuperClassDefined(ClassDecl classdecl) {
		String superClass = classdecl.superName();
		return superClass == null || (superClass != null && this.classToScopes.get(superClass) != null && !superClass.equals(this.mainClassName));
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
			if(checkDuplicateField(fieldDecl)){
				throwCompilationError("Duplicate field name :"+fieldDecl.name());
			}
			fieldDecl.accept(this);
		}

		for (var methodDecl : classDecl.methoddecls()) {
			this.currentMethod = methodDecl.name();
			if(checkOverloadedMethod(methodDecl)){
				throwCompilationError("Overloaded method "+methodDecl.name());
			}
			addMethodScope(classScopes, methodDecl, currClassScope);
			this.currentSymbolTable = classScopes.get(methodDecl.name());
			methodDecl.accept(this);
		}
	}

	// Check 4
	// Return TRUE if field is a duplicate
	private boolean checkDuplicateField(VarDecl fieldDecl) {
		SymbolTableLookup stl = new SymbolTableLookup(classToScopes);
		SymbolTable st = stl.lookup(this.currentClass,null,"variable",fieldDecl.name());
		return !(st == null);
	}

	// Check 5
	// Returns TRUE if method is an overload
	private boolean checkOverloadedMethod(MethodDecl methodDecl) {
		SymbolTableLookup stl = new SymbolTableLookup(classToScopes);
		SymbolTable st = stl.lookup(this.currentClass, null, "method", methodDecl.name());
		// if we found the a method with the same name in this current class then it must be an overload.
		// if we found it in any upper class - we check at InvalidMethodOverride
		// if null (i.e. not in this class or any upper class) then we return true.
		if (st == null) return false;
		return (st.getScopeName().equals(this.currentClass));
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
		SymbolTableEntry symbol = new SymbolTableEntry(methodDecl.name(), "method", methodDecl);

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
			if(checkDuplicateFormalDeclaration(formal)){
				throwCompilationError("Formal is of name already declared - "+formal.name());
			}
			formal.accept(this);
		}

		for (var varDecl : methodDecl.vardecls()) {
			if(checkDuplicateLocalVariableDeclaration(varDecl)){
				throwCompilationError("Local variable is of name already declared - "+varDecl.name());
			}
			varDecl.accept(this);
		}

		for (var stmt : methodDecl.body()) {
			stmt.accept(this);
		}

		methodDecl.ret().accept(this);
	}

	// Check 24a
	// Return TRUE if local variable is a duplicate
	private boolean checkDuplicateLocalVariableDeclaration(VarDecl varDecl) {
		return this.currentSymbolTable.getVarEntries().get(varDecl.name()) != null;
	}

	// Check 24b
	private boolean checkDuplicateFormalDeclaration(FormalArg formal) {
		return this.currentSymbolTable.getVarEntries().get(formal.name()) != null;
	}

	private void checkSusVariable(VariableIntroduction variable) {
		if(variable.name().compareTo(this.searchTerm) == 0
				&& variable.lineNumber == this.searchLineNumber) {
			this.foundSymbolTable = this.currentSymbolTable;
		}
	}

	@Override
	public void visit(FormalArg formalArg) {
		SymbolTableEntry symbol = new SymbolTableEntry(formalArg.name(), "variable", formalArg);
		this.currentSymbolTable.addEntry(symbol.getName(), symbol);
		this.checkSusVariable(formalArg);
		formalArg.type().accept(this);
	}

	@Override
	public void visit(VarDecl varDecl) {
		SymbolTableEntry symbol = new SymbolTableEntry(varDecl.name(), "variable", varDecl);
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
		// Check 13
		// arrayExpr must be newArray or identifer
		if(!(e.arrayExpr() instanceof NewIntArrayExpr) && !(e.arrayExpr() instanceof IdentifierExpr)){
			throwCompilationError(".length was used on a variable which is not new int[] or identifier - "+e.arrayExpr().toString());
		}
		e.arrayExpr().accept(this);
		// if it's an identiferExpr we check if it's defined at Check 14 - which happens on visit
		// if it's an identifer - make sure it's of type IntArray
		if(e.arrayExpr() instanceof IdentifierExpr){
			AstType type = new SymbolTableLookup(classToScopes).lookupVariableType(this.currentClass, this.currentMethod, ((IdentifierExpr)e.arrayExpr()).id());

			// if this isn't a RefType we throw compilation error (it's a primitive type : int, bool, int[])
			if (!(type instanceof IntArrayAstType)) {
				throwCompilationError("Type of indentifer in array.length is not int[] - "+type.toString());
			}
		}
	}

	@Override
	public void visit(MethodCallExpr e) {
		// Check 12 - owner must be this/new ()/identifier
		if(!(e.ownerExpr() instanceof ThisExpr) && !(e.ownerExpr() instanceof IdentifierExpr) && !(e.ownerExpr() instanceof NewObjectExpr)){
			throwCompilationError("OwnerExpr is not this / new () / identifier - "+e.ownerExpr().toString());
		}

		e.ownerExpr().accept(this);
		// if it's an identiferExpr we check if it's defined at Check 14 - which happens on visit
		// Check 10 - will only work if check 14 passed
		if(e.ownerExpr() instanceof IdentifierExpr){
			AstType type = new SymbolTableLookup(classToScopes).lookupVariableType(this.currentClass, this.currentMethod, ((IdentifierExpr)e.ownerExpr()).id());

			// if this isn't a RefType we throw compilation error (it's a primitive type : int, bool, int[])
			if (!RefType.class.isInstance(type)) {
				throwCompilationError("OwnerExpr is and identifer which is not a RefType - "+type.toString());
			}
		}

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
	public void visit(IdentifierExpr e) {
		if(!checkIdentifierDefined(e)){
			throwCompilationError("Identifier "+e.id()+" was not previously defined");
		}
	}

	// Check 14
	// Returns TRUE if identifier was properly defined
	private boolean checkIdentifierDefined(IdentifierExpr e) {
		SymbolTableLookup stl = new SymbolTableLookup(classToScopes);
		SymbolTable st = stl.lookup(this.currentClass, this.currentMethod, "variable", e.id());
		return st != null;
	}

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

package ast;
import java.util.*;

public class MethodRenamingVisitor implements Visitor {
	private String currentClass;
	private String currentMethod;
	private Set<String> susClasses;
	private String staticClassReference;

	private String originalName;
	private String newName;

	private SymbolTableLookup symbolTables;

	public MethodRenamingVisitor(String originalName, String newName, Set<String> susClasses, Map<String, Map<String, SymbolTable>> symbolTables) {
		this.originalName = originalName;
		this.newName = newName;
		this.susClasses = susClasses;
		this.symbolTables = new SymbolTableLookup(symbolTables);
	}

	private void visitBinaryExpr(BinaryExpr e, String infixSymbol) {
		e.e1().accept(this);
		e.e2().accept(this);
	}

	@Override
	public void visit(Program program) {
		program.mainClass().accept(this);
		for (ClassDecl classdecl : program.classDecls()) {
				classdecl.accept(this);
		}
	}

	@Override
	public void visit(ClassDecl classDecl) {
		this.currentClass = classDecl.name();
		for (var fieldDecl : classDecl.fields()) {
				fieldDecl.accept(this);
		}

		for (var methodDecl : classDecl.methoddecls()) {
				methodDecl.accept(this);
		}
	}

	@Override
	public void visit(MainClass mainClass) {
		this.currentClass = mainClass.name();
		mainClass.mainStatement().accept(this);
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		// Note: must be set before renaming, symbol table name isn't changed!
		this.currentMethod = methodDecl.name();
		methodDecl.returnType().accept(this);
		if(methodDecl.name().equals(this.originalName) && this.susClasses.contains(this.currentClass)){
				methodDecl.setName(this.newName);
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
		visitBinaryExpr(e, "<");
	}

	@Override
	public void visit(AddExpr e) {
		visitBinaryExpr(e, "+");
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

		if(e.methodId().equals(this.originalName)){
			if(this.susClasses.contains(this.staticClassReference)) {
				e.setMethodId(this.newName);
			}
		}
	}

	@Override
	public void visit(IntegerLiteralExpr e) {
	}

	@Override
	public void visit(TrueExpr e) {
	}

	@Override
	public void visit(FalseExpr e) {
	}

	@Override
	public void visit(IdentifierExpr e) {
		SymbolTable symbolTable = symbolTables.lookup(this.currentClass, this.currentMethod, "variable", e.id());
		SymbolTableEntry symbol = symbolTable.getVarEntries().get(e.id());
		VariableIntroduction variable = (VariableIntroduction)symbol.getDeclRef();
		AstType type = variable.type();

		// if this isn't a RefType we don't need to change it's method calls
		if (!RefType.class.isInstance(type)) {
			this.staticClassReference = "";
			return;
		}

		this.staticClassReference = ((RefType)type).id();
	}

	public void visit(ThisExpr e) {
		this.staticClassReference = this.currentClass;
	}

	@Override
	public void visit(NewIntArrayExpr e) {
		e.lengthExpr().accept(this);
	}

	@Override
	public void visit(NewObjectExpr e) {
		this.staticClassReference = e.classId();
	}

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

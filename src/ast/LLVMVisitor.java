package ast;
import java.util.*;

public class LLVMVisitor implements Visitor {

	private int registerCount;
	private String LLVMType;
	private SymbolTableLookup symbolTables;
	private boolean isField;
	private StringBuilder builder = new StringBuilder();

	public LLVMVisitor(Map<String, Map<String, SymbolTable>> symbolTables) {
		this.symbolTables = new SymbolTableLookup(symbolTables);
	}

	public String getCode() {
		return builder.toString();
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
		this.isField = true;
		for (var fieldDecl : classDecl.fields()) {
				fieldDecl.accept(this);
		}

		this.isField = false;
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
		this.registerCount = 0;
		methodDecl.returnType().accept(this);
		builder.append("\ndefine " + this.LLVMType + " @" + methodDecl.name() + "(");
		for (var formal : methodDecl.formals()) {
				formal.accept(this);
		}

		builder.append(") {\n");
		for (var varDecl : methodDecl.vardecls()) {
				varDecl.accept(this);
		}

		for (var stmt : methodDecl.body()) {
				stmt.accept(this);
		}

		builder.append("}\n");
		methodDecl.ret().accept(this);
	}

	@Override
	public void visit(FormalArg formalArg) {
		// TODO: handle formals
		formalArg.type().accept(this);
	}

	@Override
	public void visit(VarDecl varDecl) {
		// TODO
		if (this.isField) {
			return;
		}

		varDecl.type().accept(this);
		builder.append("\t%" + varDecl.name() + " = alloca " + this.LLVMType + "\n");
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
		String variable = assignStatement.lv();
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
	}

	@Override
	public void visit(IntegerLiteralExpr e) {
		this.LLVMType = Integer.toString(e.num());
	}

	@Override
	public void visit(TrueExpr e) {
		this.LLVMType = "1";
	}

	@Override
	public void visit(FalseExpr e) {
		this.LLVMType = "0";
	}

	@Override
	public void visit(IdentifierExpr e) {
	}

	public void visit(ThisExpr e) {
	}

	@Override
	public void visit(NewIntArrayExpr e) {
		e.lengthExpr().accept(this);
	}

	@Override
	public void visit(NewObjectExpr e) {
	}

	@Override
	public void visit(NotExpr e) {}

	@Override
	public void visit(IntAstType t) {
		this.LLVMType = "i32";
	}

	@Override
	public void visit(BoolAstType t) {
		this.LLVMType = "i1";
	}

	@Override
	public void visit(IntArrayAstType t) {
		this.LLVMType = "?";
		throw new java.lang.UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void visit(RefType t) {
		this.LLVMType = "?";
		throw new java.lang.UnsupportedOperationException("Not supported yet.");
	}
}

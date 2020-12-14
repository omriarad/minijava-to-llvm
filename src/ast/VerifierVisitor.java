package ast;
import java.util.*;

public class VerifierVisitor implements Visitor {

	private String type;
	private String currentClass;
	private String currentMethod;
	private SymbolTableLookup symbolTables;
	private boolean isThis;

	public VerifierVisitor(Map<String, Map<String, SymbolTable>> symbolTables) {
		this.symbolTables = new SymbolTableLookup(symbolTables);
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
		mainClass.mainStatement().accept(this);
	}

	@Override
	public void visit(MethodDecl methodDecl) {
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
		String variable = assignArrayStatement.lv();

		assignArrayStatement.index().accept(this);
		assignArrayStatement.rv().accept(this);
	}

	@Override
	public void visit(AndExpr e) {
		e.e1().accept(this);
		e.e2().accept(this);
	}

	private void visitBinaryMathExpr(BinaryExpr e, String command) {
		e.e1().accept(this);
		e.e2().accept(this);
	}

	@Override
	public void visit(LtExpr e) {
		visitBinaryMathExpr(e, "icmp slt");
		this.type = "boolean";
	}

	@Override
	public void visit(AddExpr e) {
		visitBinaryMathExpr(e, "add");
	}

	@Override
	public void visit(SubtractExpr e) {
		visitBinaryMathExpr(e, "sub");
	}

	@Override
	public void visit(MultExpr e) {
		visitBinaryMathExpr(e, "mul");
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
		this.type = "int";
	}

	@Override
	public void visit(TrueExpr e) {
		this.type = "boolean";
	}

	@Override
	public void visit(FalseExpr e) {
		this.type = "boolean";
	}


	@Override
	public void visit(IdentifierExpr e) {
		AstType type = this.symbolTables.lookupVariableType(this.currentClass, this.currentMethod, e.id());
		type.accept(this);
	}

	public void visit(ThisExpr e) {
		this.isThis = true;
		this.type = this.currentClass;
	}

	@Override
	public void visit(NewIntArrayExpr e) {
		e.lengthExpr().accept(this);
	}

	@Override
	public void visit(NewObjectExpr e) {
	}

	@Override
	public void visit(NotExpr e) {
		e.e().accept(this);
	}

	@Override
	public void visit(IntAstType t) {
		this.type = "int";
	}

	@Override
	public void visit(BoolAstType t) {
		this.type = "boolean";
	}

	@Override
	public void visit(IntArrayAstType t) {
		this.type = "int[]";
	}

	@Override
	public void visit(RefType t) {
		this.type = t.id();
	}
}

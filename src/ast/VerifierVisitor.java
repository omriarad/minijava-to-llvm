package ast;
import java.util.*;

public class VerifierVisitor implements Visitor {

	private String type;
	private String currentClass;
	private String currentMethod;
	private SymbolTableLookup symbolTables;
	private boolean isThis;

	class VerificationError extends AssertionError {
		public VerificationError(String error) {
			super(String.format("<%s:%s> %s",
								VerifierVisitor.this.currentClass,
								VerifierVisitor.this.currentMethod,
								error));
		}
	}

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

	private String getType(AstType type) {
		type.accept(this);
		return this.type;
	}

	private void compareType(String type1, String type2) {
		if (type1.compareTo(type2) != 0) {
			throw new VerificationError(String.format("type mismatch, %s!=%s", type1, type2));
		}
	}

	private void verifyOverride(MethodDecl originalMethod, MethodDecl overridingMethod) {
		int originalArgs = originalMethod.formals().size();
		int overridingArgs = overridingMethod.formals().size();

		if (originalArgs != overridingArgs) {
			throw new VerificationError(
				String.format(
					"number of arguments mismatch found! original: %d, overriding: %d",
					originalArgs,
					overridingArgs)
				);
		}

		for (int i = 0; i < originalArgs; i++) {
			var originalType = this.getType(originalMethod.formals().get(i).type());
			var overridingType = this.getType(overridingMethod.formals().get(i).type());

			this.compareType(originalType, this.type);
		}

		var originalReturnType = this.getType(originalMethod.returnType());
		var overridingReturnType = this.getType(overridingMethod.returnType());
		System.out.println(overridingReturnType + " " + originalReturnType);
		if (originalReturnType.compareTo(overridingReturnType) != 0 &&
			!this.symbolTables.isSubclass(overridingReturnType, originalReturnType)) {
			throw new VerificationError(
				String.format(
					"overriding return type is invalid, original=%s new=%s",
					originalReturnType,
					overridingReturnType)
				);
		}
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		MethodDecl overriddenMethod =
			this.symbolTables.getOverriddenMethod(this.currentClass, methodDecl.name());

		this.currentMethod = methodDecl.name();
		for (var formal : methodDecl.formals()) {
			formal.accept(this);
		}

		for (var varDecl : methodDecl.vardecls()) {
			varDecl.accept(this);
		}

		for (var stmt : methodDecl.body()) {
			stmt.accept(this);
		}

		var returnType = this.getType(methodDecl.returnType());
		methodDecl.ret().accept(this);
		if (this.type.compareTo(returnType) != 0 &&
			!this.symbolTables.isSubclass(this.type, returnType)) {
			throw new VerificationError(
				String.format(
					"invalid return type, static=%s got=%s",
					returnType,
					this.type)
				);
		}

		if (overriddenMethod != null) {
			this.verifyOverride(overriddenMethod, methodDecl);
		}
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
		if (this.type.compareTo("boolean") != 0) {
			throw new VerificationError(
				String.format(
					"cond in if must be boolean and not %s",
					this.type)
				);
		}
	}

	@Override
	public void visit(WhileStatement whileStatement) {
		whileStatement.cond().accept(this);
		if (this.type.compareTo("boolean") != 0) {
			throw new VerificationError(
				String.format(
					"cond in while must be boolean and not %s",
					this.type)
				);
		}

		whileStatement.body().accept(this);
	}

	@Override
	public void visit(SysoutStatement sysoutStatement) {
		sysoutStatement.arg().accept(this);
		this.compareType("int", this.type);
	}

	@Override
	public void visit(AssignStatement assignStatement) {
		String variable = assignStatement.lv();
		String rType = this.getType(this.symbolTables.lookupVariableType(this.currentClass, this.currentMethod, variable));

		assignStatement.rv().accept(this);
		var lType = this.type;
		if (lType.compareTo(rType) != 0 && !this.symbolTables.isSubclass(lType, rType)) {
			throw new VerificationError(
				String.format(
					"type mismatch in assignment, rType=%s lType=%s",
					rType,
					lType)
				);
		}
	}

	@Override
	public void visit(AssignArrayStatement assignArrayStatement) {
		String variable = assignArrayStatement.lv();
		var type = this.symbolTables.lookupVariableType(this.currentClass, this.currentMethod, variable);

		type.accept(this);
		this.compareType("int[]", this.type);
		assignArrayStatement.index().accept(this);
		this.compareType("int", this.type);
		assignArrayStatement.rv().accept(this);
		this.compareType("int", this.type);
	}

	@Override
	public void visit(AndExpr e) {
		e.e1().accept(this);
		this.compareType("boolean", this.type);
		e.e2().accept(this);
		this.compareType("boolean", this.type);
	}

	private void visitBinaryMathExpr(BinaryExpr e, String command) {
		e.e1().accept(this);
		this.compareType("int", this.type);
		e.e2().accept(this);
		this.compareType("int", this.type);
	}

	@Override
	public void visit(LtExpr e) {
		visitBinaryMathExpr(e, "lt");
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
		this.compareType("int[]", this.type);
		e.indexExpr().accept(this);
		this.compareType("int", this.type);
	}

	@Override
	public void visit(ArrayLengthExpr e) {
		e.arrayExpr().accept(this);
		this.type = "int";
	}

	@Override
	public void visit(MethodCallExpr e) {
		e.ownerExpr().accept(this);
		var method = this.symbolTables.getMethod(this.type, e.methodId());
		if (method == null) {
			throw new VerificationError(
				String.format(
					"method %s not found for class %s",
					e.methodId(),
					this.type)
				);
		}

		int methodArgCount = method.formals().size();
		int calledArgCount = e.actuals().size();

		if (methodArgCount != calledArgCount) {
			throw new VerificationError(
				String.format(
					"number of arguments mismatch found! method: %d, method call: %d",
					methodArgCount,
					calledArgCount)
				);
		}

		for (int i = 0; i < methodArgCount; i++) {
			var methodType =this.getType(method.formals().get(i).type());
			Expr arg = e.actuals().get(i);
			arg.accept(this);

			if (this.type.compareTo(methodType) != 0 &&
				!this.symbolTables.isSubclass(this.type, methodType)) {
				throw new VerificationError(
					String.format(
						"type of formal %d mismatch for method %s",
						i,
						e.methodId())
					);
			}
		}

		this.type = this.getType(method.returnType());
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
		this.compareType("int", this.type);
		this.type = "int[]";
	}

	@Override
	public void visit(NewObjectExpr e) {
		if (!this.symbolTables.isDefined(e.classId())) {
			throw new VerificationError(
				String.format(
					"refType %s doesn't exists in symbol tables",
					e.classId())
				);
		}

		this.type = e.classId();
	}

	@Override
	public void visit(NotExpr e) {
		e.e().accept(this);
		this.compareType("boolean", this.type);
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
		if (!this.symbolTables.isDefined(t.id())) {
			throw new VerificationError(
				String.format(
					"refType %s doesn't exists in symbol tables",
					t.id())
				);
		}

		this.type = t.id();
	}
}

package ast;
import java.util.*;

public class LLVMVisitor implements Visitor {

	private int registerCount;
	private int labelCount;
	private int lRegister;
	private int rRegister;
	private String LLVMType;
	private String currentClass;
	private String currentMethod;
	private SymbolTableLookup symbolTables;
	private boolean isField;
	private StringBuilder builder = new StringBuilder();


	public LLVMVisitor(Map<String, Map<String, SymbolTable>> symbolTables) {
		this.symbolTables = new SymbolTableLookup(symbolTables);
		this.registerCount = -1;
		this.labelCount = 0;
		// TODO: fix this ugliness better
		this.builder.append("declare i8* @calloc(i32, i32)\ndeclare i32 @printf(i8*, ...)\ndeclare void @exit(i32)\n\n@_cint = constant [4 x i8] c\"%d\\0a\\00\"\n@_cOOB = constant [15 x i8] c\"Out of bounds\\0a\\00\"\ndefine void @print_int(i32 %i) {\n\t%_str = bitcast [4 x i8]* @_cint to i8*\n\tcall i32 (i8*, ...) @printf(i8* %_str, i32 %i)\n\tret void\n}\n\ndefine void @throw_oob() {\n\t%_str = bitcast [15 x i8]* @_cOOB to i8*\n\tcall i32 (i8*, ...) @printf(i8* %_str)\n\tcall void @exit(i32 1)\n\tret void\n}\n");
	}

	public String getCode() {
		return builder.toString();
	}

	private boolean isLiteral() {
		try {
			int literalValue = Integer.parseInt(this.LLVMType);
			return true;
		} catch (NumberFormatException nfe) {}

		return false;
	}

	@Override
	public void visit(Program program) {
		// Handle this
		//program.mainClass().accept(this);
		for (ClassDecl classdecl : program.classDecls()) {
				classdecl.accept(this);
		}
	}

	@Override
	public void visit(ClassDecl classDecl) {
		this.isField = true;
		this.currentClass = classDecl.name();
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
		String retType;
		this.registerCount = -1;
		this.currentMethod = methodDecl.name();
		methodDecl.returnType().accept(this);
		retType = this.LLVMType;
		builder.append("\ndefine " + retType + " @" + methodDecl.name() + "(");
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

		methodDecl.ret().accept(this);
		if (this.isLiteral()) {
			this.builder.append("\tret " + retType + " " + this.LLVMType);
		} else {
			this.builder.append("\tret " + retType + " %_" + this.registerCount);
		}
		builder.append("\n}\n");
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
		int startLabelNo = this.labelCount;
		this.labelCount += 3;
		builder.append("\tbr i1 %_" +  this.registerCount + ", label %if" + startLabelNo + ", label %if" + (startLabelNo + 1) + "\n");
		builder.append("if" + startLabelNo + ":\n");
		ifStatement.thencase().accept(this);
		builder.append("\tbr label %if" + (startLabelNo + 2) + "\n");
		builder.append("if" + (startLabelNo + 1) + ":\n");
		ifStatement.elsecase().accept(this);
		builder.append("\tbr label %if" + (startLabelNo + 2) + "\n");
		builder.append("if" + (startLabelNo + 2) + ":\n");
	}

	@Override
	public void visit(WhileStatement whileStatement) {
		whileStatement.cond().accept(this);
		whileStatement.body().accept(this);
	}

	@Override
	public void visit(SysoutStatement sysoutStatement) {
		sysoutStatement.arg().accept(this);
		this.builder.append("\tcall void (i32) @print_int(i32 ");
		if (this.isLiteral()) {
			this.builder.append(this.LLVMType);
		} else {
			this.builder.append("%_" + registerCount);
		}
		this.builder.append(")\n");
	}

	@Override
	public void visit(AssignStatement assignStatement) {
		int literalValue = 0;
		boolean isLiteral = false;
		String variable = assignStatement.lv();
		AstType type = this.symbolTables.lookupVariableType(this.currentClass, this.currentMethod, variable);

		assignStatement.rv().accept(this);
		if (this.isLiteral()) {
			literalValue = Integer.parseInt(this.LLVMType);
			isLiteral = true;
		}

		type.accept(this);

		this.builder.append("\t");
		if (isLiteral) {
			this.builder.append("store " + this.LLVMType + " " + literalValue + ", " + this. LLVMType + "* %" + variable);
		} else {
			int lastRegister = this.registerCount;
			this.registerCount++;
			this.builder.append("store " + this.LLVMType + " " + lastRegister + ", " + this. LLVMType + "* %" + variable);
		}

		this.builder.append("\n");
	}

	@Override
	public void visit(AssignArrayStatement assignArrayStatement) {
		assignArrayStatement.index().accept(this);
		assignArrayStatement.rv().accept(this);
	}

	@Override
	public void visit(AndExpr e) {
		int startLabelNo = -1;
		e.e1().accept(this);
		int registerForPhi = -1;
		startLabelNo = this.labelCount;
		this.labelCount += 4;
		builder.append("\tbr label %andcond" + startLabelNo + "\n");
		builder.append("andcond" + startLabelNo + ":\n");
		builder.append("\tbr i1 %_" +  this.registerCount + ", label %andcond" + (startLabelNo + 1) + ", label %andcond" + (startLabelNo + 3) + "\n");
		builder.append("andcond" + (startLabelNo + 1) + ":\n");
		e.e2().accept(this);
		registerForPhi = this.registerCount;
		builder.append("\tbr label %andcond" + (startLabelNo + 2) + "\n");
		builder.append("andcond" + (startLabelNo + 2) + ":\n");
		builder.append("\tbr label %andcond" + (startLabelNo + 3) + "\n");
		builder.append("andcond" + (startLabelNo + 3) + ":\n");
		this.registerCount++;
		builder.append("\t%_" +  this.registerCount + " = phi i1 [0, %andcond" + (startLabelNo) + "], [%_" + registerForPhi + ", %andcond" + (startLabelNo + 2) + "]\n");
	}

	@Override
	public void visit(LtExpr e) {}

	private void visitBinaryMathExpr(BinaryExpr e, String command) {
		String lvalue, rvalue, type = "";

		e.e1().accept(this);
		if (this.isLiteral()) {
			lvalue = this.LLVMType;
		} else {
			type = this.LLVMType;
			lvalue = "%_" + this.registerCount;
		}
		e.e2().accept(this);
		if (this.isLiteral()) {
			rvalue = this.LLVMType;
		} else {
			type = this.LLVMType;
			rvalue = "%_" + this.registerCount;
		}

		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = " + command + " " + type + " " + lvalue +", " + rvalue + "\n");
		this.LLVMType = type;
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
		AstType type = this.symbolTables.lookupVariableType(this.currentClass, this.currentMethod, e.id());
		type.accept(this);
		this.registerCount++;
		this.builder.append("\t");
		this.builder.append("%_" + this.registerCount + " = load " + this.LLVMType + ", " + this.LLVMType + "* %" + e.id());
		this.builder.append("\n");
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

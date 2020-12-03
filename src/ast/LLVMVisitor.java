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
	private String refTypeClass;
	private boolean newObjectOwner;


	public LLVMVisitor(Map<String, Map<String, SymbolTable>> symbolTables) {
		this.symbolTables = new SymbolTableLookup(symbolTables);
		this.registerCount = -1;
		this.labelCount = 0;
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


	private void buildVTables(Program program) {
		// We do not build a vtable for main class
		for(ClassDecl classdecl : program.classDecls()){
			printVTable(symbolTables.getClassVTable(classdecl.name()));
			this.builder.append("\n");
		}
	}

	private void printVTable(VTable vtable) {
		// Header
		builder.append("@."+vtable.getClassName()+"_vtable = global ["+vtable.getSize()+" x i8*] [");
		// fuction declarations
		String funcSig;
		List<VTableEntry> entries = vtable.getVTableEntries();
		if(entries.size() >= 2){
			for(int i=0;i<entries.size()-1;i++){
				funcSig = vtable.getMethodFullSignature(entries.get(i).getMethodName());
				builder.append("i8* bitcast ("+funcSig+" to i8*), ");
			}
		}
		funcSig = vtable.getMethodFullSignature(entries.get(entries.size()-1).getMethodName());
		builder.append("i8* bitcast ("+funcSig+" to i8*)");
		// Footer
		builder.append("]\n");
	}

	@Override
	public void visit(Program program) {
		buildVTables(program);
		this.builder.append("declare i8* @calloc(i32, i32)\ndeclare i32 @printf(i8*, ...)\ndeclare void @exit(i32)\n\n@_cint = constant [4 x i8] c\"%d\\0a\\00\"\n@_cOOB = constant [15 x i8] c\"Out of bounds\\0a\\00\"\ndefine void @print_int(i32 %i) {\n\t%_str = bitcast [4 x i8]* @_cint to i8*\n\tcall i32 (i8*, ...) @printf(i8* %_str, i32 %i)\n\tret void\n}\n\ndefine void @throw_oob() {\n\t%_str = bitcast [15 x i8]* @_cOOB to i8*\n\tcall i32 (i8*, ...) @printf(i8* %_str)\n\tcall void @exit(i32 1)\n\tret void\n}\n");
		program.mainClass().accept(this);
		for (ClassDecl classdecl : program.classDecls()) {
				classdecl.accept(this);
		}

		this.builder.append("\n");
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
		builder.append("\n\ndefine i32 @main() {\n");
		mainClass.mainStatement().accept(this);
		builder.append("\tret i32 0\n}\n");
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		String retType;
		this.registerCount = -1;
		this.labelCount = 0;
		this.currentMethod = methodDecl.name();
		methodDecl.returnType().accept(this);
		retType = this.LLVMType;
		builder.append("\ndefine " + retType + " @" + this.currentClass + "." + methodDecl.name() + "(i8* %this");
		for (var formal : methodDecl.formals()) {
			formal.type().accept(this);
			builder.append(", " + this.LLVMType + " %." + formal.name());
		}

		builder.append(") {\n");
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
		if (this.isLiteral()) {
			this.builder.append("\tret " + retType + " " + this.LLVMType);
		} else {
			this.builder.append("\tret " + retType + " %_" + this.registerCount);
		}
		builder.append("\n}\n");
	}

	@Override
	public void visit(FormalArg formalArg) {
		formalArg.type().accept(this);
		builder.append("\t%" + formalArg.name() + " = alloca " + this.LLVMType + "\n");
		builder.append("\tstore " + this.LLVMType + " %." + formalArg.name() + ", " + this. LLVMType + "* %" + formalArg.name() + "\n");
	}

	@Override
	public void visit(VarDecl varDecl) {
		varDecl.type().accept(this);
		if (this.isField) {
			return;
		}

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
		if(this.isLiteral()){
			builder.append("\tbr i1 " +  this.LLVMType + ", label %if" + startLabelNo + ", label %if" + (startLabelNo + 1) + "\n");
		}
		else{
			builder.append("\tbr i1 %_" +  this.registerCount + ", label %if" + startLabelNo + ", label %if" + (startLabelNo + 1) + "\n");
		}
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
		int startLabelNo = this.labelCount;
		this.labelCount += 3;
        builder.append("branch " + startLabelNo + ":\n");
		builder.append("while" + startLabelNo + ":\n");
		whileStatement.cond().accept(this);
		if(this.isLiteral()){
			builder.append("\tbr i1 " +  this.LLVMType + ", label %while" + (startLabelNo + 1) + ", label %if" + (startLabelNo + 2) + "\n");
		}
		else{
			builder.append("\tbr i1 %_" +  this.registerCount + ", label %while" + (startLabelNo + 1) + ", label %while" + (startLabelNo + 2) + "\n");
		}
		builder.append("while" + (startLabelNo + 1) + ":\n");
		whileStatement.body().accept(this);
		builder.append("\tbr label %while" + startLabelNo + "\n");
		builder.append("while" + (startLabelNo + 2) + ":\n");
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
		String src = null;
		String variable = assignStatement.lv();
		AstType type = this.symbolTables.lookupVariableType(this.currentClass, this.currentMethod, variable);
		boolean lvIsField = this.symbolTables.isField(this.currentClass, this.currentMethod, variable);
		assignStatement.rv().accept(this);
		if (this.isLiteral()) {
			src = this.LLVMType;
		} else if (this.newObjectOwner) {
			src = "%_" + (this.registerCount - 2);
		} else {
			src = "%_" + this.registerCount;
		}

		type.accept(this);
		if(lvIsField){
			this.registerCount++;
			this.builder.append("\t%_" + this.registerCount + " = getelementptr i8, i8* %this, i32 " + this.symbolTables.getFieldOffset(this.currentClass, variable) + "\n");
			this.registerCount++;
			this.builder.append("\t%_" + this.registerCount + " = bitcast i8* %_" + (this.registerCount - 1) + " to " + this.LLVMType + "*\n");
			this.builder.append("\tstore " + this.LLVMType + " " + src + ", " + this.LLVMType + "* %_" + this.registerCount + "\n");
		}
		else{
			this.builder.append("\tstore " + this.LLVMType + " " + src + ", " + this.LLVMType + "* %" + variable + "\n");
		}
		this.newObjectOwner = false;
	}

	private String arrayAccessSetup(String variable, String index) {
		String arrayPtr;
		int startLabelNo = this.labelCount;

		this.labelCount += 4;
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = load i32*, i32** %" + variable + "\n");
		arrayPtr = "%_" + this.registerCount;
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = icmp slt i32 " + index + ", 0\n");
		this.builder.append("\tbr i1 %_" + this.registerCount + ", label %arr_alloc" + startLabelNo);
		this.builder.append(", label %arr_alloc" + (startLabelNo + 1) + "\n");
		this.builder.append("arr_alloc" + startLabelNo + ":\n");
		this.builder.append("\tcall void @throw_oob()\n");
		this.builder.append("\tbr label %arr_alloc" + (startLabelNo + 1) + "\n");
		this.builder.append("arr_alloc" + (startLabelNo + 1) + ":\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = getelementptr i32, i32* " + arrayPtr + ", i32 0\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = load i32, i32* %_" + (this.registerCount - 1) + "\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = icmp sle i32 %_" + (this.registerCount - 1) + ", " + index + "\n");
		this.builder.append("\tbr i1 %_" + this.registerCount + ", label %arr_alloc" + (startLabelNo + 2));
		this.builder.append(", label %arr_alloc" + (startLabelNo + 3) + "\n");
		this.builder.append("arr_alloc" + (startLabelNo + 2) + ":\n");
		this.builder.append("\tcall void @throw_oob()\n");
		this.builder.append("\tbr label %arr_alloc" + (startLabelNo + 3) + "\n");
		this.builder.append("arr_alloc" + (startLabelNo + 3) + ":\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = add i32 " + index + ", 1\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = getelementptr i32, i32* " + arrayPtr + ", i32 %_" + (this.registerCount - 1) + "\n");
		return "%_" + this.registerCount;
	}

	@Override
	public void visit(AssignArrayStatement assignArrayStatement) {
		String index;
		String indexPtr;
		String variable = assignArrayStatement.lv();

		assignArrayStatement.index().accept(this);
		if (this.isLiteral()) {
			index = this.LLVMType;
		} else {
			index = "%_" + this.registerCount;
		}

		indexPtr = this.arrayAccessSetup(assignArrayStatement.lv(), index);
		assignArrayStatement.rv().accept(this);
		if (this.isLiteral()) {
			this.builder.append("\tstore i32 " + this.LLVMType + ", i32* " + indexPtr + "\n");
		} else {
			this.builder.append("\tstore i32 %_" + this.registerCount + ", i32* " + indexPtr + "\n");
		}
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
		if(this.isLiteral()){
			builder.append("\tbr i1 " +  this.LLVMType + ", label %andcond" + (startLabelNo + 1) + ", label %andcond" + (startLabelNo + 3) + "\n");
		}
		else{
			builder.append("\tbr i1 %_" +  this.registerCount + ", label %andcond" + (startLabelNo + 1) + ", label %andcond" + (startLabelNo + 3) + "\n");
		}
		builder.append("andcond" + (startLabelNo + 1) + ":\n");
		e.e2().accept(this);
		registerForPhi = this.registerCount;
		builder.append("\tbr label %andcond" + (startLabelNo + 2) + "\n");
		builder.append("andcond" + (startLabelNo + 2) + ":\n");
		builder.append("\tbr label %andcond" + (startLabelNo + 3) + "\n");
		builder.append("andcond" + (startLabelNo + 3) + ":\n");
		this.registerCount++;
		if(this.isLiteral()){
			builder.append("\t%_" +  this.registerCount + " = phi i1 [0, %andcond" + (startLabelNo) + "], [" + this.LLVMType + ", %andcond" + (startLabelNo + 2) + "]\n");
		}
		else {
			builder.append("\t%_" +  this.registerCount + " = phi i1 [0, %andcond" + (startLabelNo) + "], [%_" + registerForPhi + ", %andcond" + (startLabelNo + 2) + "]\n");
		}
		this.LLVMType = "i1";
	}

	@Override
	public void visit(LtExpr e) {
		visitBinaryMathExpr(e, "icmp slt");
		this.LLVMType = "i1";
	}

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
		String index;
		String indexPtr;
		IdentifierExpr variable = (IdentifierExpr)e.arrayExpr();

		e.indexExpr().accept(this);
		if (this.isLiteral()) {
			index = this.LLVMType;
		} else {
			index = "%_" + this.registerCount;
		}

		indexPtr = this.arrayAccessSetup(variable.id(), index);
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = load i32, i32* " + indexPtr + "\n");
		this.LLVMType = "i32";
	}

	@Override
	public void visit(ArrayLengthExpr e) {
		e.arrayExpr().accept(this);
	}

	@Override
	public void visit(MethodCallExpr e) {
		e.ownerExpr().accept(this);
		String thisRegister;
		if(this.newObjectOwner){
			thisRegister  = "%_" + String.valueOf(this.registerCount - 2);
		}
		else{
			thisRegister  = "%_" + String.valueOf(this.registerCount);
		}
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = bitcast i8* " + thisRegister + " to i8***\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = load i8**, i8*** %_" + (this.registerCount - 1) + "\n");
		this.registerCount++;
		VTable curVTable = this.symbolTables.getClassVTable(this.refTypeClass);
		this.builder.append("\t%_" + this.registerCount + " = getelementptr i8*, i8** %_" + (this.registerCount - 1) + ", i32 " + curVTable.getIndexOfMethod(e.methodId()) + "\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = load i8*, i8** %_" + (this.registerCount - 1) + "\n");
		this.registerCount++;
		List<String> formalTypes = curVTable.getMethodFormalTypesList(e.methodId());
		this.builder.append("\t%_" + this.registerCount + " = bitcast i8* %_" + (this.registerCount - 1) + " to " + curVTable.getMethodReturnType(e.methodId()) + " (");
		for(int i = 0; i < formalTypes.size(); i++){
			if(i != 0){
				this.builder.append(" ");
			}
			this.builder.append(formalTypes.get(i));
			if(i != formalTypes.size() - 1){
				this.builder.append(",");
			}
		}
		this.builder.append(")*" + "\n");
		List<String> registers = new ArrayList<>();
		registers.add(thisRegister);
		int methodCallRegister = this.registerCount;
		for (Expr arg : e.actuals()) {
				arg.accept(this);
				if(this.isLiteral()){
					registers.add(this.LLVMType);
				}
				else{
					registers.add("%_" + String.valueOf(this.registerCount));
				}
		}
		this.registerCount++;
		String methodReturnType = curVTable.getMethodReturnType(e.methodId());
		this.builder.append("\t%_" + this.registerCount + " = call " + methodReturnType + " %_" + methodCallRegister + "(");
		for(int i = 0; i < formalTypes.size(); i++){
			if(i != 0){
				this.builder.append(" ");
			}
			this.builder.append(formalTypes.get(i) + " " + registers.get(i));
			if(i != formalTypes.size() - 1){
				this.builder.append(",");
			}
		}
		this.builder.append(")\n");
		this.LLVMType = methodReturnType;
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
		boolean isField = this.symbolTables.isField(this.currentClass, this.currentMethod, e.id());
		type.accept(this);
		this.registerCount++;
		if(isField){
			this.builder.append("\t%_" + this.registerCount + " = getelementptr i8, i8* %this, i32 " + this.symbolTables.getFieldOffset(this.currentClass, e.id()) + "\n");
			this.registerCount++;
			this.builder.append("\t%_" + this.registerCount + " = bitcast i8* %_" + (this.registerCount - 1) + " to " + this.LLVMType + "*\n");
			this.registerCount++;
			this.builder.append("\t%_" + this.registerCount + " = load " + this.LLVMType + ", " + this.LLVMType + "* %_" + (this.registerCount - 1) + "\n");
		}
		else {
			this.builder.append("\t");
			this.builder.append("%_" + this.registerCount + " = load " + this.LLVMType + ", " + this.LLVMType + "* %" + e.id());
			this.builder.append("\n");
		}
	}

	public void visit(ThisExpr e) {
		this.refTypeClass = this.currentClass;
	}

	@Override
	public void visit(NewIntArrayExpr e) {
		String size;
		int startLabelNo;

		e.lengthExpr().accept(this);
		if (this.isLiteral()) {
			size = this.LLVMType;
		} else {
			size = "%_" + this.registerCount;
		}

		startLabelNo = this.labelCount;
		this.labelCount += 2;
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = icmp slt i32 " + size + ", 0\n");
		this.builder.append("\tbr i1 %_" + this.registerCount + ", label %arr_alloc" + startLabelNo);
		this.builder.append(", label %arr_alloc" + (startLabelNo + 1) + "\n");
		this.builder.append("arr_alloc" + startLabelNo + ":\n");
		this.builder.append("\tcall void @throw_oob()\n");
		this.builder.append("\tbr label %arr_alloc" + (startLabelNo + 1) + "\n");
		this.builder.append("arr_alloc" + (startLabelNo + 1) + ":\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = add i32 " + size + ", 1\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = call i8* @calloc(i32 %_" + (this.registerCount -1) + ", i32 4)\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = bitcast i8* %_" + (this.registerCount - 1) + " to i32*\n");
		this.builder.append("\tstore i32 " + size + ", i32* %_" + this.registerCount + "\n");
		this.LLVMType = "i32*";
	}

	@Override
	public void visit(NewObjectExpr e) {
		this.registerCount++;
		String newObjectClass = e.classId();
		int instanceSize = this.symbolTables.getInstanceSize(newObjectClass);
		this.builder.append("\t%_" + this.registerCount + " = call i8* @calloc(i32 1, i32 " + instanceSize + ")\n");
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = bitcast i8* %_" + (this.registerCount - 1) + " to i8***\n");
		this.registerCount++;
		VTable newObjectVtable = this.symbolTables.getClassVTable(e.classId());
		int numOfVTableEntrues = newObjectVtable.getVTableEntries().size();
		this.builder.append("\t%_" + this.registerCount + " = getelementptr [" + numOfVTableEntrues + " x i8*], [" + numOfVTableEntrues + " x i8*]* @." + e.classId() + "_vtable, i32 0, i32 0\n");
		this.builder.append("\tstore i8** %_" + this.registerCount + ", i8*** %_" + (this.registerCount - 1) + "\n");
		this.LLVMType = "i8*";
		this.refTypeClass = e.classId();
		this.newObjectOwner = true;
	}

	@Override
	public void visit(NotExpr e) {
		int originalRegister;

		e.e().accept(this);
		originalRegister = this.registerCount;
		this.registerCount++;
		this.builder.append("\t%_" + this.registerCount + " = sub i1 1, %_" + originalRegister + "\n");
	}

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
		this.LLVMType = "i32*";
	}

	@Override
	public void visit(RefType t) {
		this.LLVMType = "i8*";
        this.refTypeClass = t.id();

	}
}

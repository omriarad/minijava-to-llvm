package ast;
import java.util.*;

public class AstRenamingVisitor implements Visitor {
	private String currentClass;
	private Set<String> susClasses;
	private Map<String,String> fieldToType;
	private Map<String,String> scopeToType;
	private String staticClassReference;
	private Map<String,String> currentMap;
	private String currentTypeName;

	private String originalName;
	private String newName;

	public AstRenamingVisitor(String originalName, String newName, Set<String> susClasses) {
		this.originalName = originalName;
		this.newName = newName;
		this.susClasses = susClasses;
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
		this.fieldToType = new HashMap<>();
		this.currentMap = this.fieldToType;
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
		this.fieldToType = null;
		this.scopeToType = null;
		mainClass.mainStatement().accept(this);
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		this.scopeToType = new HashMap<>();
		this.currentMap = this.scopeToType;
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
		this.currentMap.put(formalArg.name(), this.currentTypeName);
	}

	@Override
	public void visit(VarDecl varDecl) {
		varDecl.type().accept(this);
		this.currentMap.put(varDecl.name(), this.currentTypeName);
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
		System.out.println(e.methodId());
		System.out.println(this.staticClassReference);
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
	    String identifiersID = e.id();
        String identifiersClass;
        if(this.scopeToType.containsKey(identifiersID)){
            	identifiersClass = this.scopeToType.get(identifiersID);
        }
	    else{
            	identifiersClass = this.fieldToType.get(identifiersID);
        }
	    this.staticClassReference = identifiersClass;
	    //
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
	public void visit(IntAstType t) {
		this.currentTypeName = "int";
	}

	@Override
	public void visit(BoolAstType t) {
		this.currentTypeName = "boolean";
	}

	@Override
	public void visit(IntArrayAstType t) {
		this.currentTypeName = "int[]";
	}

	@Override
	public void visit(RefType t) {
		this.currentTypeName = t.id();
	}
}

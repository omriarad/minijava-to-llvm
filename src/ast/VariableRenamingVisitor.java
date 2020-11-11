package ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VariableRenamingVisitor implements Visitor {
    private Map<String, Map<String,SymbolTable>> classToScopes;
    private String currentClass;
    private String currentMethod;
    private String originalName;
    private String newName;
    private SymbolTableLookup symbolTableLookup;
    private SymbolTable foundSymbolTable;

    public VariableRenamingVisitor(String originalName, String newName, Map<String, Map<String,SymbolTable>> classToScopes, SymbolTable foundSymbolTable) {
        this.originalName = originalName;
        this.newName = newName;
        this.classToScopes = classToScopes;
        this.foundSymbolTable = foundSymbolTable;
        this.symbolTableLookup = new SymbolTableLookup(classToScopes);
        this.currentClass = "Main";
    }

    private boolean rename(String curName){
        if(curName.equals(this.originalName)) {
            SymbolTable curSymbolTable = this.symbolTableLookup.lookup(this.currentClass, this.currentMethod, "variable", curName);
            if (curSymbolTable == this.foundSymbolTable) {
                return true;
            }
        }
        return false;
    }

    private void visitBinaryExpr(BinaryExpr e, String infixSymbol) { //not interesting
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(Program program) {
        program.mainClass().accept(this);
        for (ClassDecl classdecl : program.classDecls()) {
            this.currentClass = classdecl.name();
            classdecl.accept(this);
        }
    }

    @Override
    public void visit(ClassDecl classDecl) {
        this.currentClass = classDecl.name();
        this.currentMethod = null;
        for (var fieldDecl : classDecl.fields()) {
            fieldDecl.accept(this);
        }

        for (var methodDecl : classDecl.methoddecls()) {
            this.currentMethod = methodDecl.name();
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
        if(this.rename(formalArg.name())){
            formalArg.setName(this.newName);
        }
        formalArg.type().accept(this);
    }

    @Override
    public void visit(VarDecl varDecl) {
        if(this.rename(varDecl.name())){
            varDecl.setName(this.newName);
        }
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
        if(this.rename(assignStatement.lv())){
            assignStatement.setLv(this.newName);
        }
        assignStatement.rv().accept(this);
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        if(this.rename(assignArrayStatement.lv())){
            assignArrayStatement.setLv(this.newName);
        }
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
    }

    @Override
    public void visit(TrueExpr e) {
    }

    @Override
    public void visit(FalseExpr e) {
    }

    @Override
    public void visit(IdentifierExpr e) {
        if(this.rename(e.id())){
            e.setId(newName);
        }
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
    public void visit(NotExpr e) {
        e.e().accept(this);
    }

    @Override
    public void visit(IntAstType t) {

    }

    @Override
    public void visit(BoolAstType t) {

    }

    @Override
    public void visit(IntArrayAstType t) {

    }

    @Override
    public void visit(RefType t) {

    }
}



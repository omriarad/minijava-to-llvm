package ast;
import java.util.*;

public class IsInitVisitor implements Visitor{
    private String currentClass;
    private String currentMethod;
    private SymbolTableLookup symbolTables;
    private boolean isField;
    private HashMap<String, String> curLatticeMap;
    private boolean isPrimitive;

    public IsInitVisitor(Map<String, Map<String, SymbolTable>> symbolTables) {
        this.symbolTables = new SymbolTableLookup(symbolTables);
    }


    private HashMap<String, String> duplicateLatticeMap(HashMap<String, String> latticeMap){
        HashMap<String, String> duplicatedLatticeMap = new HashMap<>(latticeMap);
        return duplicatedLatticeMap;
    }

    private void throwCompilationError(String errorMessage) {
        throw new AssertionError("ERROR: "+ errorMessage);
    }

    private  HashMap<String, String> laticeMapUnion(HashMap<String, String> latticeMap1, HashMap<String, String> latticeMap2) {
        HashMap<String, String> unifiedLaticeMap = new HashMap<>();
        for(Map.Entry<String, String> entry : latticeMap1.entrySet()){
            if(entry.getValue().equals("T") || (!entry.getValue().equals(latticeMap2.get(entry.getKey())))){
                unifiedLaticeMap.put(entry.getKey(), "T");
            }
            else{
                unifiedLaticeMap.put(entry.getKey(), entry.getValue());
            }
        }
        return unifiedLaticeMap;
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
        HashMap<String, String> latticeMap = new HashMap<>();
        this.curLatticeMap = latticeMap;
        mainClass.mainStatement().accept(this);
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.currentMethod = methodDecl.name();
        methodDecl.returnType().accept(this);
        HashMap<String, String> latticeMap = new HashMap<>();
        for (FormalArg formal : methodDecl.formals()) {
            latticeMap.put(formal.name(), "tt");
            formal.type().accept(this);
        }

        for (VarDecl varDecl : methodDecl.vardecls()) {
            latticeMap.put(varDecl.name(), "ff");
            varDecl.accept(this);
        }
        List<String> fieldNames = this.symbolTables.getFieldsNames(this.currentClass);
        for(String name : fieldNames){
            if(!latticeMap.containsKey(name)){
                latticeMap.put(name, "tt");
            }
        }
        this.curLatticeMap = latticeMap;
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
        if (this.isField) {
            return;
        }
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
        HashMap<String, String> thenLaticeMap = this.duplicateLatticeMap(this.curLatticeMap);
        HashMap<String, String> elseLaticeMap = this.duplicateLatticeMap(this.curLatticeMap);

        this.curLatticeMap = thenLaticeMap;
        ifStatement.thencase().accept(this);
        thenLaticeMap = this.curLatticeMap;


        this.curLatticeMap = elseLaticeMap;
        ifStatement.elsecase().accept(this);

        this.curLatticeMap = this.laticeMapUnion(thenLaticeMap, this.curLatticeMap);
    }

    @Override
    public void visit(WhileStatement whileStatement) {
        whileStatement.cond().accept(this);
        HashMap<String, String> prevLaticeMap = this.curLatticeMap;
        HashMap<String, String> whileLaticeMap = this.duplicateLatticeMap(this.curLatticeMap);
        this.curLatticeMap = whileLaticeMap;
        whileStatement.body().accept(this);
        this.curLatticeMap = this.laticeMapUnion(prevLaticeMap, this.curLatticeMap);
    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {
        sysoutStatement.arg().accept(this);
    }

    @Override
    public void visit(AssignStatement assignStatement) {
        assignStatement.rv().accept(this);
        this.curLatticeMap.put(assignStatement.lv(), "tt");
    }


    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        if(!this.curLatticeMap.get(assignArrayStatement.lv()).equals("tt")){
            throwCompilationError(assignArrayStatement.lv() + " is used before initialized (class: " + this.currentClass + " method: " + this.currentMethod + ")");
        }
        assignArrayStatement.index().accept(this);
        assignArrayStatement.rv().accept(this);
    }

    @Override
    public void visit(AndExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(LtExpr e) {
        visitBinaryMathExpr(e, "icmp slt");
    }

    private void visitBinaryMathExpr(BinaryExpr e, String command) {
        e.e1().accept(this);
        e.e2().accept(this);
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

    }

    @Override
    public void visit(TrueExpr e) {

    }

    @Override
    public void visit(FalseExpr e) {

    }


    @Override
    public void visit(IdentifierExpr e) {
        if(!this.curLatticeMap.get(e.id()).equals("tt")){
            throwCompilationError(e.id() + " is used before initialized (class: " + this.currentClass + " method: " + this.currentMethod + ")");
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



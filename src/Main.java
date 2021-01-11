import ast.*;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class Main {
    private static Map<String,Map<String,SymbolTable>> verify(Program prog, PrintWriter outFile) {
        SymbolTableVisitor sv;
        boolean erroneous = false;

        try {
            sv = new SymbolTableVisitor(prog, "", 0);
            sv.visit(prog);
            var vv = new VerifierVisitor(sv.getSymbolTables());
            vv.visit(prog);
            var iv = new InitializationVisitor(sv.getSymbolTables());
            iv.visit(prog);
        } catch(AssertionError e) {
            erroneous = true;
            outFile.write("ERROR\n");
            throw e;
        }

        if (!erroneous) {
            outFile.write("OK\n");
        }

        return sv.getSymbolTables();
    }

    public static void main(String[] args) throws FileNotFoundException {
        var action = args[0];
        var filename = args[args.length - 2];
        var outfilename = args[args.length - 1];
        var outFile = new PrintWriter(outfilename);

        try {
            Program prog;
            Parser parser_obj = new Parser(new Lexer(new FileReader(filename)));
            var parse_tree = parser_obj.parse();
            prog = (Program)parse_tree.value;

            if (action.equals("--marshal")) {
                AstXMLSerializer xmlSerializer = new AstXMLSerializer();
                xmlSerializer.serialize(prog, outfilename);
                return;
            }

            var symbolTables = verify(prog, outFile);
            if (action.equals("--semantic")) {
                return;
            }

            outFile = new PrintWriter(outfilename);
            var compiler = new LLVMVisitor(symbolTables);
            compiler.visit(prog);
            outFile.write(compiler.getCode());
        } catch (AssertionError e) {
            System.out.println("Semantic error: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("General error: " + e);
            e.printStackTrace();
        } finally {
            outFile.flush();
            outFile.close();
        }
    }
}


import ast.*;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            var inputMethod = args[0];
            var action = args[1];
            var filename = args[args.length - 2];
            var outfilename = args[args.length - 1];

            Program prog;

            if (inputMethod.equals("parse")) {
                throw new UnsupportedOperationException("TODO - Ex. 4");
            } else if (inputMethod.equals("unmarshal")) {
                AstXMLSerializer xmlSerializer = new AstXMLSerializer();
                prog = xmlSerializer.deserialize(new File(filename));
            } else {
                throw new UnsupportedOperationException("unknown input method " + inputMethod);
            }

            var outFile = new PrintWriter(outfilename);
            try {

                if (action.equals("marshal")) {
                    AstXMLSerializer xmlSerializer = new AstXMLSerializer();
                    xmlSerializer.serialize(prog, outfilename);
                } else if (action.equals("print")) {
                    AstPrintVisitor astPrinter = new AstPrintVisitor();
                    astPrinter.visit(prog);
                    outFile.write(astPrinter.getString());

                } else if (action.equals("semantic")) {
                    throw new UnsupportedOperationException("TODO - Ex. 3");

                } else if (action.equals("compile")) {
                    var fv = new FinderVisitor(prog, "", 0);
                    fv.visit(prog);
                    var compiler = new LLVMVisitor(fv.getClassToScopes());
                    compiler.visit(prog);
                    System.out.println(compiler.getCode());
                } else if (action.equals("rename")) {
                    var type = args[2];
                    var originalName = args[3];
                    var originalLine = args[4];
                    var newName = args[5];

                    boolean isMethod;
                    if (type.equals("var")) {
                        isMethod = false;
                        FinderVisitor fv = new FinderVisitor(prog,originalName,Integer.valueOf(originalLine));
                        fv.visit(prog);
                        var classToScopes = fv.getClassToScopes();
                        // Checking FinderVisitor scope building skills
                        // for(var entry : classToScopes.entrySet()){
                        //     System.out.println("Scopes for class: "+entry.getKey());
                        //     for(var st : entry.getValue().values()){
                        //         System.out.println(st);
                        //     }
                        // }
                        //
                        var foundSymbolTable= fv.getFoundSymbolTable();
                        var visitor = new VariableRenamingVisitor(originalName, newName, classToScopes, foundSymbolTable);
                        visitor.visit(prog);

                    } else if (type.equals("method")) {
                        isMethod = true;
                        // Example of using MethodFinderVisitor
                        FinderVisitor fv = new FinderVisitor(prog,originalName,Integer.valueOf(originalLine));
                        fv.visit(prog);
                        // imported Set
                        Set<String> sus = fv.getSusClasses();
                        var classToScopes = fv.getClassToScopes();
                        var susClass = fv.getFoundClass();
                        var visitor = new MethodRenamingVisitor(originalName, newName, sus, susClass, classToScopes);
                        visitor.visit(prog);
                    } else {
                        throw new IllegalArgumentException("unknown rename type " + type);
                    }
                    AstXMLSerializer xmlSerializer = new AstXMLSerializer();
                    xmlSerializer.serialize(prog, outfilename);
                } else {
                    throw new IllegalArgumentException("unknown command line action " + action);
                }
            } finally {
                outFile.flush();
                outFile.close();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error reading file: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("General error: " + e);
            e.printStackTrace();
        }
    }
}


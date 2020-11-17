import ast.*;

import java.io.*;
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
                    throw new UnsupportedOperationException("TODO - Ex. 2");

                } else if (action.equals("rename")) {
                    var type = args[2];
                    var originalName = args[3];
                    var originalLine = args[4];
                    var newName = args[5];

                    boolean isMethod;
                    if (type.equals("var")) {
                        isMethod = false;
                    } else if (type.equals("method")) {
                        isMethod = true;
                        // Example of using MethodFinderVisitor
                        MethodFinderVisitor mfv = new MethodFinderVisitor(prog,originalName,Integer.valueOf(originalLine));
                        mfv.visit(prog);
                        // imported Set
                        Set<String> sus = mfv.getSusClasses();
                        if(sus != null) {
                        	System.out.println("Suspected Classes:");
                        	for(String s : sus) {
                        		System.out.println(s);
                        	}
                        }
                        if(mfv.getFoundClass() != null) {
                        	System.out.println("Method found in class: "+mfv.getFoundClass());
                        }
                        var visitor = new AstRenamingVisitor(originalName, newName, sus);
                        visitor.visit(prog);
                    } else {
                        throw new IllegalArgumentException("unknown rename type " + type);
                    }



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

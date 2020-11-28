package ast;

public class FieldOffsetsEntry {
    private String fieldName;
    private int offset;

    FieldOffsetsEntry(String fieldName, int offset){
        this.fieldName = fieldName;
        this.offset = offset;
    }

    String getName(){
        return this.fieldName;
    }

    int getOffset(){
        return this.offset;
    }
}

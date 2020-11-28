package ast;

import java.util.List;
import java.util.ArrayList;

public class FieldOffsetsList {
    private List<FieldOffsetsEntry> entries;
    private int currentOffset;

    FieldOffsetsList(){
        this.entries = new ArrayList<FieldOffsetsEntry>();
        this.currentOffset = 8;
    }

    FieldOffsetsList(FieldOffsetsList prevList){
        this.entries = new ArrayList<FieldOffsetsEntry>(prevList.getFieldOffsetsList());
        this.currentOffset = prevList.getListSize();
    }

    void addEntry(String fieldName,int fieldSize){
        FieldOffsetsEntry foe = new FieldOffsetsEntry(fieldName,currentOffset);
        this.currentOffset = currentOffset + fieldSize;
        this.entries.add(foe);
    }

    int getOffset(String fieldName){
        for(FieldOffsetsEntry entry : this.entries){
            if(entry.getName().equals(fieldName)){
                return entry.getOffset();
            }
        }
        return -1;
    }

    int getListSize(){
        return this.currentOffset;
    }

    List<FieldOffsetsEntry> getFieldOffsetsList(){
        return this.entries;
    }

    // List<FieldOffsetsEntry> getFieldOffsetsCopy(){
    //     // return a copy of the fieldOffsetsList as this will be the base for Appending classes
    //     return new ArrayList<FieldOffsetsEntry>(entries);
    // }
}

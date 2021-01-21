package ast;

import java.util.ArrayList;
import java.util.List;

public class VTable {
  String className;
  List<VTableEntry> entries;

  VTable(String className) {
    this.className = className;
    this.entries = new ArrayList<VTableEntry>();
  }

  VTable(String className, VTable prevTable) {
    this.className = className;
    ArrayList<VTableEntry> newEntries = new ArrayList<>();
    for (VTableEntry vte : prevTable.getVTableEntries()) {
      newEntries.add(new VTableEntry(vte));
    }
    this.entries = newEntries;
  }

  List<VTableEntry> getVTableEntries() {
    return this.entries;
  }

  void addEntry(String currClassName, String methodName, SymbolTableEntry symbol) {
    // check if this method is an override
    for (VTableEntry entry : this.entries) {
      if (entry.getMethodName().equals(methodName)) {
        // This is an override, change className of this method
        entry.setClassName(currClassName);
        return;
      }
    }
    // No override found, new entry !
    VTableEntry vte = new VTableEntry(currClassName, methodName, symbol.getDeclRef());
    this.entries.add(vte);
  }

  String getClassName() {
    return this.className;
  }

  /* VTable utility functions */
  /* Main exmaple of usage:
   * Imagine a class Base with method: int foo(int x,boolean isTrue)
   * class Base has no other methods.
   * will describe the expected return for each utility function
   */

  // With methodName , finds the index of that method in the class vtable
  // If methodName was not found returns -1
  // Good for method calling
  // Main Example ("foo") : 0
  int getIndexOfMethod(String methodName) {
    for (int i = 0; i < this.entries.size(); i++) {
      if (entries.get(i).getMethodName().equals(methodName)) {
        return i;
      }
    }
    return -1;
  }

  int getSize() {
    return this.entries.size();
  }

  // FOR ALL FUNCTIONS BELOW -- If methodName was not found returns null !!!!!

  // Get method Formal Types list (to iterate over)
  // Good for method calling
  // M.E. ("foo"): [i8* , i32 , i32]
  List<String> getMethodFormalTypesList(String methodName) {
    for (VTableEntry entry : entries) {
      if (entry.getMethodName().equals(methodName)) {
        return entry.getFormalsTypesList();
      }
    }
    return null;
  }

  // Get a formal type for a specific formal name in a method
  // Good for allocating stack space for a formal at method start
  // M.E. ("foo",".x"): i32 | Notice the dot on the formal name !
  String getMethodFormalType(String methodName, String formalName) {
    for (VTableEntry entry : entries) {
      if (entry.getMethodName().equals(methodName)) {
        List<String> formals = entry.getFormals();
        for (String formal : formals) {
          String currFormalType = formal.split(" ")[0];
          String currFormalName = formal.split(" ")[1];
          if (currFormalName.equals(formalName)) {
            return currFormalType;
          }
        }
      }
    }
    return null;
  }

  // Get the COMPLETE method formal list - includes types and names
  // Good for method declarations
  // M.E. ("foo") : (i8* %this, i32 %.x, i1 %.isTrue)
  String getMethodFormalsFullString(String methodName) {
    for (VTableEntry entry : entries) {
      if (entry.getMethodName().equals(methodName)) {
        String res = "(";
        List<String> formalList = entry.getFormals();
        if (formalList.size() >= 2) {
          for (int i = 0; i < formalList.size() - 1; i++) {
            res += formalList.get(i) + ", ";
          }
        }
        res += formalList.get(formalList.size() - 1) + ")";
        return res;
      }
    }
    return null;
  }

  // Get SHORTEND method formal list - includes only types
  // Good for vtable construction, bitcasting functions fetched from vtable
  // M.E. ("foo") : (i8*, i32, i1)*
  String getMethodFormalsShortString(String methodName) {
    for (VTableEntry entry : entries) {
      if (entry.getMethodName().equals(methodName)) {
        String res = "(";
        List<String> formalTypesList = entry.getFormalsTypesList();
        if (formalTypesList.size() >= 2) {
          for (int i = 0; i < formalTypesList.size() - 1; i++) {
            String formalType = formalTypesList.get(i);
            res += formalType + ", ";
          }
        }
        res += formalTypesList.get(formalTypesList.size() - 1) + ")*";
        return res;
      }
    }
    return null;
  }

  // Get method's return type as a String
  // Good for method calling , method fetching from vtable , bitcasting fetched functions from
  // vtable
  // M.E. ("foo") : i32
  String getMethodReturnType(String methodName) {
    for (VTableEntry entry : entries) {
      if (entry.getMethodName().equals(methodName)) {
        return entry.getReturnType();
      }
    }
    return null;
  }

  // Constructs the method full name
  // Can be used for method declartions
  // M.E. ("foo") : @Base.foo
  String getMethodFullName(String methodName) {
    for (VTableEntry entry : entries) {
      if (entry.getMethodName().equals(methodName)) {
        return entry.getFullName();
      }
    }
    return null;
  }

  // Helper function for vtable builiding
  // M.E. ("foo") : i32 (i8*, i32, i1)* @Base.foo
  String getMethodFullSignature(String methodName) {
    return getMethodReturnType(methodName)
        + " "
        + getMethodFormalsShortString(methodName)
        + " "
        + getMethodFullName(methodName);
  }

  @Override
  public String toString() {
    String res = "VTable [className=" + className + "]\n";
    for (VTableEntry entry : this.entries) {
      res += entry.getMethodName() + "\n";
      res += getMethodFullSignature(entry.getMethodName());
      res += "\n";
    }
    return res;
  }
}

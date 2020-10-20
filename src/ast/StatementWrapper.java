package ast;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class StatementWrapper {
    @XmlElements({
            @XmlElement(name="block", type=BlockStatement.class),
            @XmlElement(name="if", type=IfStatement.class),
            @XmlElement(name="while", type=WhileStatement.class),
            @XmlElement(name="sysout", type=SysoutStatement.class),
            @XmlElement(name="assign", type=AssignStatement.class),
            @XmlElement(name="assign-array", type=AssignArrayStatement.class)
    })
    public Statement s;

    // for derserialization only!
    public StatementWrapper() {
    }

    public StatementWrapper(Statement s) {
        this.s = s;
    }
}

package ast;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class AstTypeWrapper {
    @XmlElements({
            @XmlElement(name="int", type=IntAstType.class),
            @XmlElement(name="bool", type=BoolAstType.class),
            @XmlElement(name="int-array", type=IntArrayAstType.class),
            @XmlElement(name="ref", type= RefType.class)
    })
    public AstType t;

    // for deserialization only!
    public AstTypeWrapper() {
    }

    public AstTypeWrapper(AstType t) {
        this.t = t;
    }
}

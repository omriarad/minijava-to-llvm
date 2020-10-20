package ast;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

// wrapper for XML marshalling, generating an element according to the subclass
public class ExprWrapper {
    @XmlElements({@XmlElement(name="and", type=AndExpr.class),
            @XmlElement(name="lt", type=LtExpr.class),
            @XmlElement(name="add", type=AddExpr.class),
            @XmlElement(name="subtract", type=SubtractExpr.class),
            @XmlElement(name="mult", type=MultExpr.class),
            @XmlElement(name="array-access", type=ArrayAccessExpr.class),
            @XmlElement(name="array-length", type=ArrayLengthExpr.class),
            @XmlElement(name="call", type=MethodCallExpr.class),
            @XmlElement(name="int-literal", type=IntegerLiteralExpr.class),
            @XmlElement(name="true", type=TrueExpr.class),
            @XmlElement(name="false", type=FalseExpr.class),
            @XmlElement(name="ref-id", type=IdentifierExpr.class),
            @XmlElement(name="this", type=ThisExpr.class),
            @XmlElement(name="new-int-array", type=NewIntArrayExpr.class),
            @XmlElement(name="new-object", type=NewObjectExpr.class),
            @XmlElement(name="not", type=NotExpr.class)
    })
    public Expr e;

    // for deserialization only!
    public ExprWrapper() {
    }

    public ExprWrapper(Expr e) {
        this.e = e;
    }
}

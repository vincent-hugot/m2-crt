package parser;

/* Generated By:JJTree: Do not edit this line. ASTdomain.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTdomain extends SimpleNode {
  public ASTdomain(int id) {
    super(id);
  }

  public ASTdomain(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=fef7a3a1c9e04ec943e28a867b9c5b96 (do not edit this line) */

package parser;

/* Generated By:JJTree: Do not edit this line. ASTeq.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTeq extends SimpleNode {
  public ASTeq(int id) {
    super(id);
  }

  public ASTeq(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=4eee7a0c649e60480511cc3fdaee1fe7 (do not edit this line) */

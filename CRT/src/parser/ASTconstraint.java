package parser;

/* Generated By:JJTree: Do not edit this line. ASTconstraint.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTconstraint extends SimpleNode {
  public ASTconstraint(int id) {
    super(id);
  }

  public ASTconstraint(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=663d58a6a2ec139840f8cc6136dba727 (do not edit this line) */

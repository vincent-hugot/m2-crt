package parser;

/* Generated By:JJTree: Do not edit this line. ASTl.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTl extends SimpleNode {
  public ASTl(int id) {
    super(id);
  }

  public ASTl(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=66480611721aa481052c8b99c09d4aaa (do not edit this line) */

/* Generated By:JJTree: Do not edit this line. ASTvar.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
public class ASTvar extends SimpleNode {
  public ASTvar(int id) {
    super(id);
  }

  public ASTvar(Parser p, int id) {
    super(p, id);
  }

  public String name = null;

  public String toString()
  {
    return super.toString() + " (" + name + ")";
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=a8fd9b8e6406483bd8377de604fedc9c (do not edit this line) */
package parser;

/* Generated By:JavaCC: Do not edit this line. ParserVisitor.java Version 4.2 */
public interface ParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTconstraints node, Object data);
  public Object visit(ASTdomain node, Object data);
  public Object visit(ASTvar node, Object data);
  public Object visit(ASTinteger node, Object data);
  public Object visit(ASTeq node, Object data);
  public Object visit(ASTleq node, Object data);
  public Object visit(ASTgeq node, Object data);
  public Object visit(ASTneq node, Object data);
  public Object visit(ASTg node, Object data);
  public Object visit(ASTl node, Object data);
  public Object visit(ASTconstraint node, Object data);
  public Object visit(ASTmul node, Object data);
  public Object visit(ASTdiv node, Object data);
  public Object visit(ASTplus node, Object data);
  public Object visit(ASTminus node, Object data);
}
/* JavaCC - OriginalChecksum=399af72bdd38a79d5e7e7f6f4801c30d (do not edit this line) */

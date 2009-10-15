package translator;

import model.Model;
import parser.*;


public class TranslationVisitor implements ParserVisitor {
	
	private SimpleNode ast;
	private Model model;
	
	
	public TranslationVisitor(SimpleNode ast) {
		this.ast = ast;
		this.model = new Model();
	}
	
	
	public Model translate() {
		// Visiting...
		ast.jjtAccept(this, new Object());
		
		// Model is hopefuly done.
		return model;
	}
	
	
	
	
	
	public Object visit(SimpleNode node, Object data) {
		return null;
	}
	
	public Object visit(ASTconstraints node, Object data) {
		return null;
	}
	
	public Object visit(ASTdomain node, Object data) {
		return null;
	}
	
	public Object visit(ASTvar node, Object data) {
		return null;
	}
	
	public Object visit(ASTinteger node, Object data) {
		return null;
	}
	
	public Object visit(ASTeq node, Object data) {
		return null;
	}
	
	public Object visit(ASTleq node, Object data) {
		return null;
	}
	
	public Object visit(ASTgeq node, Object data) {
		return null;
	}
	
	public Object visit(ASTneq node, Object data) {
		return null;
	}
	
	public Object visit(ASTg node, Object data) {
		return null;
	}
	
	public Object visit(ASTl node, Object data) {
		return null;
	}
	
	public Object visit(ASTconstraint node, Object data) {
		return null;
	}
	
	public Object visit(ASTmul node, Object data) {
		return null;
	}
	
	public Object visit(ASTdiv node, Object data) {
		return null;
	}
	
	public Object visit(ASTplus node, Object data) {
		return null;
	}
	
	public Object visit(ASTminus node, Object data) {
		return null;
	}
	
}

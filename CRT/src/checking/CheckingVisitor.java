package checking;

import parser.*;
import translator.ConstraintsError;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


public class CheckingVisitor implements ParserVisitor {
	
	private SimpleNode ast;
	private ArrayList<ConstraintsError> errors;
	private String filename;
	
	/** Set of variables which we know have already been declared. */
	Set<String> declaredVars = new HashSet<String>();
	
	
	
	public CheckingVisitor(SimpleNode ast, String filename) {
		this.ast = ast;
		this.errors = new ArrayList<ConstraintsError>();
		this.filename = filename;
	}
	
	
	public void fail(String msg, int line) {
		errors.add(new ConstraintsError(filename, line, msg));
	}
	
	
	public void check() {
		ast.jjtAccept(this, new Object());
	}
	
	
	public ArrayList<ConstraintsError> getErrors() {
		return errors;
	}
	
	

	
	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	
	public Object visit(ASTconstraints node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTdomain node, Object data) {
		String varName = ((ASTvar)node.jjtGetChild(0)).name;
		if (declaredVars.contains(varName))
		{
			fail("Variable " + varName + " has more than one declaration",
					((SimpleNode) node.jjtGetChild(0)).getLine());
		}
		declaredVars.add(varName);
		int lower = ((ASTinteger)node.jjtGetChild(1)).value ;
		int higher = ((ASTinteger)node.jjtGetChild(2)).value ;
		if (lower > higher)
		{
			fail("Variable " + varName + " lives in the empty domain: [" +
					lower + ",...," + higher + "]", node.getLine());
		}
		
		return null;
	}

	
	public Object visit(ASTvar node, Object data) {
		
		String varName = ((ASTvar)node).name;
		if (!declaredVars.contains(varName))
		{
			fail("Variable " + varName + " has not been declared", node.getLine());
		}
		return null;
	}

	
	public Object visit(ASTinteger node, Object data) {
		return null;
	}

	
	public Object visit(ASTeq node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTleq node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTgeq node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTneq node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTg node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTl node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTconstraint node, Object data) {
		
		// the only son is an (in)equality...
		node.jjtGetChild(0).jjtAccept(this, null);
	    
		return null;
	}

	
	public Object visit(ASTmul node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTdiv node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTplus node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}

	
	public Object visit(ASTminus node, Object data) {
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
	    {
	      node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}
	
	

}

package checking;

import parser.*;
import translator.ConstraintsError;
import java.util.ArrayList;
import java.util.HashMap;
import model.Variable;



public class CheckingVisitor implements ParserVisitor {
	
	private SimpleNode ast;
	private ArrayList<ConstraintsError> errors;
	private String filename;
	
	/** Set of variables which we know have already been declared. */
	HashMap<String,Variable> declaredVars = new HashMap<String,Variable>();
	
	

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
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
		}
		return null;
	}
	
	
	public Object visit(ASTdomain node, Object data) {
		String varName = ((ASTvar) node.jjtGetChild(0)).name;
		if (declaredVars.containsKey(varName)) {
			fail("Variable " + varName + " has more than one declaration", ((SimpleNode) node
					.jjtGetChild(0)).getLine());
		}
		int lower = ((ASTinteger) node.jjtGetChild(1)).value;
		int higher = ((ASTinteger) node.jjtGetChild(2)).value;
		if (lower > higher) {
			fail("Variable " + varName + " lives in the empty domain: [" + lower + ",...," + higher
					+ "]", node.getLine());
		}
		
		declaredVars.put(varName, new Variable(varName, lower, higher));
		
		return null;
	}
	
	
	public Object visit(ASTvar node, Object data) {
		
		String varName = ((ASTvar) node).name;
		if (!declaredVars.containsKey(varName)) {
			fail("Variable " + varName + " has not been declared", node.getLine());
			return null;
		}
		
		Variable v = declaredVars.get(varName);
		if (v.minBound() == v.maxBound()) // Variable value known (for /0 check)
			return v.minBound();
		
		return null;
	}
	
	
	public Object visit(ASTinteger node, Object data) {
		return node.value; // To check /0
	}
	
	
	public Object visit(ASTeq node, Object data) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
		}
		return null;
	}
	
	
	public Object visit(ASTleq node, Object data) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
		}
		return null;
	}
	
	
	public Object visit(ASTgeq node, Object data) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
		}
		return null;
	}
	
	
	public Object visit(ASTneq node, Object data) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
		}
		return null;
	}
	
	
	public Object visit(ASTg node, Object data) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
		}
		return null;
	}
	
	
	public Object visit(ASTl node, Object data) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
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
		
		Integer op1 = (Integer) node.jjtGetChild(0).jjtAccept(this, null);
		Integer op2 = (Integer) node.jjtGetChild(1).jjtAccept(this, null);
		
		if (op1 != null && op2 != null)
			return op1 * op2; // To check /0 in ASTdiv
		return null;
		
		/*for (int i = 0 ; i < node.jjtGetNumChildren() ; i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;*/
	}
	
	public Object visit(ASTdiv node, Object data) {
		
		Integer op1 = (Integer) node.jjtGetChild(0).jjtAccept(this, null);
		Integer op2 = (Integer) node.jjtGetChild(1).jjtAccept(this, null);
		
		/* Quick note on 'divide by 0' detection strategy:
		 * - each integer node returns its value (we know it)
		 * - each [X..X] var node returns X
		 * - each [X..Y] var node returns null (we don't know its value)
		 * - each arithmetic node returns the calculated result of op1 & op2
		 *    IFF we recursively know each child's value
		 * - the div node checks if op2 is not null and zero, then fails
		 *    else it has the same behavior as other arithmetic nodes
		 */
		if (op2 != null && op2 == 0) {
			fail("Obvious division by 0", node.getLine());
		}
		else if (op1 != null & op2 != null)
			return op1 / op2;
		return null;
		
		/*for (int i = 0 ; i < node.jjtGetNumChildren() ; i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;*/
	}
	
	
	public Object visit(ASTplus node, Object data) {
		
		Integer op1 = (Integer) node.jjtGetChild(0).jjtAccept(this, null);
		Integer op2 = (Integer) node.jjtGetChild(1).jjtAccept(this, null);
		
		if (op1 != null && op2 != null)
			return op1 + op2; // To check /0 in ASTdiv
		return null;
		
		/*for (int i = 0 ; i < node.jjtGetNumChildren() ; i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;*/
	}
	
	
	public Object visit(ASTminus node, Object data) {
		
		Integer op1 = (Integer) node.jjtGetChild(0).jjtAccept(this, null);
		Integer op2 = (Integer) node.jjtGetChild(1).jjtAccept(this, null);
		
		if (op1 != null && op2 != null)
			return op1 - op2; // To check /0 in ASTdiv
		return null;
		
		/*for (int i = 0 ; i < node.jjtGetNumChildren() ; i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;*/
	}
	
}

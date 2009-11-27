package translator;

import java.util.HashMap;
import model.Constant;
import model.Model;
import model.Operator;
import model.Variable;
import parser.*;


/**
 * Visitor for AST to model translation.
 * 
 * General conversion strategy:
 * <br>- Domain definition = Variable creation (+ add in symbols table)
 * <br>- A #var node returns the Variable object from its name
 * <br>- An #integer node creates a Constant, then returns it
 * <br>- An expression node will create a substituteNbr Variable,
 * create a Substitution from whatever Variable/Constant their child may return,
 * then return the variable created
 * <br>- A constraint node creates the Constraint with whatever Variable/Constant
 * object their child may return
 * 
 * <br><br>The behavior of substitutions is fully recursive,
 * starting from #var/#integer, up to the constraint node.
 * This way, "A < B + C * 42":
 * <br>- A, B and C are returned according to their declaration (Variable object)
 * <br>- 42 creates a constant and returns it
 * <br>- "*"-node creates the S1 = C * 42 substitution (returns S1)
 * <br>- "+"-node creates the S2 = B + S1 substitution (returns S2)
 * <br>- "<"-node creates the A < S2 constraint
 * 
 * <br>A symbols table (declaredVars) and an iterator (substituteNbr naming) are used.
 * 
 * <br>Last note: this translator is confident enough for being used AFTER checking.
 * (ie: #var in constraint is considered already declared, no checking is done)
 * 
 * @author Mathias COQBLIN
 */
public class TranslationVisitor implements ParserVisitor {
	
	/** Substitute variable prefix, using an invalid char to avoid conflicts */
	public static final String SUBSTITUTE = "@";
	
	/** Iterator for substituteNbr naming */
	private int substituteNbr;
	
	private SimpleNode ast;
	private Model model;
	
	private HashMap<String,Variable> declaredVars;
	
	
	public TranslationVisitor(SimpleNode ast) {
		this.ast = ast;
		this.model = new Model();
		this.declaredVars = new HashMap<String,Variable>();
		this.substituteNbr = 1;
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
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++) {
			node.jjtGetChild(i).jjtAccept(this, null);
	    }
		return null;
	}
	
	
	public Object visit(ASTdomain node, Object data) {
		
		/* Adding a variable to the model */
		
		String name = ((ASTvar)node.jjtGetChild(0)).name;
		int lower = ((ASTinteger)node.jjtGetChild(1)).value;
		int higher = ((ASTinteger)node.jjtGetChild(2)).value;
		
		Variable v = model.newVariable(name, lower, higher);
		declaredVars.put(name, v);
		
		return null;
	}
	
	
	
	public Object visit(ASTvar node, Object data) {
		
		/* Getting the previously declared variable */
		return declaredVars.get(node.name);
	}
	
	
	
	public Object visit(ASTinteger node, Object data) {
		
		/* Creating a new constant, then returning it */
		Constant c = model.newConstant(node.value);
		
		return c;
	}
	
	
	
	public Object visit(ASTeq node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,Operator.Constraint.EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTleq node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,Operator.Constraint.LOWER_OR_EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTgeq node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,Operator.Constraint.GREATER_OR_EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTneq node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,Operator.Constraint.NOT_EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTg node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,Operator.Constraint.GREATER);
		
		return null;
	}
	
	
	public Object visit(ASTl node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,Operator.Constraint.LOWER);
		
		return null;
	}
	
	
	public Object visit(ASTconstraint node, Object data) {
		
		// Kind of useless node, only child is the actual constraint node
		node.jjtGetChild(0).jjtAccept(this, null);
		
		return null;
	}
	
	
	
	public Object visit(ASTmul node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		
		// Creating a substituteNbr from left and right, then an expression linking them
		String name = SUBSTITUTE + substituteNbr;
		substituteNbr++;
		
		model.Domain newDomain = 
			left.getDomain().arithmeticOperation(
					Operator.Arithmetic.MUL, right.getDomain());
		
			
		Variable equal = model.newVariable(name, newDomain, true);
		
		
		model.newSubstitution(left, right, Operator.Arithmetic.MUL, equal);
		
		return equal;
	}
	
	
	public Object visit(ASTdiv node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		
		// Creating a substituteNbr from left and right, then an expression linking them
		String name = SUBSTITUTE + substituteNbr;
		substituteNbr++;
		
		model.Domain newDomain = 
			left.getDomain().arithmeticOperation(
					Operator.Arithmetic.DIV, right.getDomain());
		
			
		Variable equal = model.newVariable(name, newDomain, true);
		
		
		model.newSubstitution(left, right, Operator.Arithmetic.DIV, equal);
		
		return equal;
	}
	
	
	public Object visit(ASTplus node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		
		// Creating a substituteNbr from left and right, then an expression linking them
		String name = SUBSTITUTE + substituteNbr;
		substituteNbr++;
		
		model.Domain newDomain = 
			left.getDomain().arithmeticOperation(
					Operator.Arithmetic.ADD, right.getDomain());
		
			
		Variable equal = model.newVariable(name, newDomain, true);
		
		
		model.newSubstitution(left, right, Operator.Arithmetic.ADD, equal);
		
		return equal;
	}
	
	
	public Object visit(ASTminus node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		
		// Creating a substituteNbr from left and right, then an expression linking them
		String name = SUBSTITUTE + substituteNbr;
		substituteNbr++;
		
		model.Domain newDomain = 
			left.getDomain().arithmeticOperation(
					Operator.Arithmetic.SUB, right.getDomain());
		
			
		Variable equal = model.newVariable(name, newDomain, true);
		
		
		model.newSubstitution(left, right, Operator.Arithmetic.SUB, equal);
		
		return equal;
	}
	
}

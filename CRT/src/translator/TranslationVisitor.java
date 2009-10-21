package translator;

import java.util.HashMap;
import model.Constant;
import model.Model;
import model.Variable;
import model.Global.OpType;
import parser.*;


/**
 * Visitor for AST to model translation.
 * 
 * General conversion strategy:
 * <br>- Domain definition = Variable creation (+ add in symbols table)
 * <br>- A #var node returns the Variable object from its name
 * <br>- An #integer node creates a Constant, then returns it
 * <br>- A constraint node creates the Constraint with whatever Variable/Constant
 * object their child may return
 * <br>- An expression node will create a substitute Variable,
 * create an Expression from whatever Variable/Constant their child may return.
 * 
 * The behavior of expressions is fully recursive, starting from #var/#integer,
 * to the constraint node.
 * This way, A < B + C * 42:
 * - creates the S1 = C * 42 expression (*node returns it)
 * - creates the S2 = B + S1 expression (+node returns it)
 * - creates the A < S2 constraint
 * 
 * <br>A symbols table (declaredVars) and an iterator (substitute naming) are used.
 * 
 * <br>Last note: this translator is confident enough for being used AFTER checking.
 * (ie: #var in constraint is considered already declared, no checking is done)
 * 
 * @author Mathias COQBLIN
 */
public class TranslationVisitor implements ParserVisitor {
	
	public static final String SUBSTITUTE = "S_";
	
	private SimpleNode ast;
	private Model model;
	
	private HashMap<String,Variable> declaredVars;
	private int substitute;
	
	
	public TranslationVisitor(SimpleNode ast) {
		this.ast = ast;
		this.model = new Model();
		this.declaredVars = new HashMap<String,Variable>();
		this.substitute = 0;
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
		
		model.newConstraint(left,right,OpType.EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTleq node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,OpType.LOWER_OR_EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTgeq node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,OpType.GREATER_OR_EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTneq node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,OpType.NOT_EQUAL);
		
		return null;
	}
	
	
	public Object visit(ASTg node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,OpType.GREATER);
		
		return null;
	}
	
	
	public Object visit(ASTl node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		model.newConstraint(left,right,OpType.LOWER);
		
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
		
		
		// Creating a substitute from left and right, then an expression linking them
		String name = SUBSTITUTE + substitute;
		substitute++;
		
		/* Boundary calculation for our substitute domain, the dumb way.
		 * That is, apply the operation between min/max of left/right
		 * (L.min+R.min, L.max+R.min, L.min+R.max, L.max+R.max)
		 * Min and max bound are amongst those resulsts
		 */
		int minmin = left.minBound() * right.minBound();
		int minmax = left.minBound() * right.maxBound();
		int maxmin = left.maxBound() * right.minBound();
		int maxmax = left.maxBound() * right.maxBound();
		
		int min = Math.min(Math.min(minmin,minmax), Math.min(maxmin,maxmax));
		int max = Math.max(Math.max(minmin,minmax), Math.max(maxmin,maxmax));
		
		Variable equal = model.newVariable(name, min, max, true);
		
		
		model.newExpression(left, right, OpType.MUL, equal);
		
		return equal;
	}
	
	
	public Object visit(ASTdiv node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		
		// Creating a substitute from left and right, then an expression linking them
		String name = SUBSTITUTE + substitute;
		substitute++;
		
		/* Boundary calculation for our substitute domain, the dumb way.
		 * That is, apply the operation between min/max of left/right
		 * (L.min+R.min, L.max+R.min, L.min+R.max, L.max+R.max)
		 * Min and max bound are amongst those resulsts
		 */
		
		/* Division by 0 case
		 * Right operand is studied by excluding 0 if it is a boundary:
		 * if min=0 && max>0, right.exclude(0) then 1 used as min
		 * if min<0 && max=0, right.exclude(0) then -1 used as max
		 * if min=0 && max=0, well, CheckingVisitor should have prevented it, since
		 *   no domain can be calculated.
		 */
		int minmin, minmax, maxmin, maxmax;
		
		if (right.minBound() == 0) {
			right.exclude(0);
			minmin = left.minBound() / 1;
			maxmin = left.maxBound() / 1;
		}
		else {
			minmin = left.minBound() / right.minBound();
			maxmin = left.maxBound() / right.minBound();
		}
		
		if (right.maxBound() == 0) {
			right.exclude(0);
			minmax = left.minBound() / -1;
			maxmax = left.maxBound() / -1;
		}
		else {
			minmax = left.minBound() / right.maxBound();
			maxmax = left.maxBound() / right.maxBound();
		}
		
		int min = Math.min(Math.min(minmin,minmax), Math.min(maxmin,maxmax));
		int max = Math.max(Math.max(minmin,minmax), Math.max(maxmin,maxmax));
		
		Variable equal = model.newVariable(name, min, max, true);
		
		
		model.newExpression(left, right, OpType.DIV, equal);
		
		return equal;
	}
	
	
	public Object visit(ASTplus node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		
		// Creating a substitute from left and right, then an expression linking them
		String name = SUBSTITUTE + substitute;
		substitute++;
		
		/* Boundary calculation for our substitute domain, the dumb way.
		 * That is, apply the operation between min/max of left/right
		 * (L.min+R.min, L.max+R.min, L.min+R.max, L.max+R.max)
		 * Min and max bound are amongst those resulsts
		 */
		int minmin = left.minBound() + right.minBound();
		int minmax = left.minBound() + right.maxBound();
		int maxmin = left.maxBound() + right.minBound();
		int maxmax = left.maxBound() + right.maxBound();
		
		int min = Math.min(Math.min(minmin,minmax), Math.min(maxmin,maxmax));
		int max = Math.max(Math.max(minmin,minmax), Math.max(maxmin,maxmax));
		
		Variable equal = model.newVariable(name, min, max, true);
		
		
		model.newExpression(left, right, OpType.ADD, equal);
		
		return equal;
	}
	
	
	public Object visit(ASTminus node, Object data) {
		
		// Assuming children will return Variable or Constant...
		Variable left = (Variable) node.jjtGetChild(0).jjtAccept(this,data);
		Variable right = (Variable) node.jjtGetChild(1).jjtAccept(this,data);
		
		
		// Creating a substitute from left and right, then an expression linking them
		String name = SUBSTITUTE + substitute;
		substitute++;
		
		/* Boundary calculation for our substitute domain, the dumb way.
		 * That is, apply the operation between min/max of left/right
		 * (L.min+R.min, L.max+R.min, L.min+R.max, L.max+R.max)
		 * Min and max bound are amongst those resulsts
		 */
		int minmin = left.minBound() - right.minBound();
		int minmax = left.minBound() - right.maxBound();
		int maxmin = left.maxBound() - right.minBound();
		int maxmax = left.maxBound() - right.maxBound();
		
		int min = Math.min(Math.min(minmin,minmax), Math.min(maxmin,maxmax));
		int max = Math.max(Math.max(minmin,minmax), Math.max(maxmin,maxmax));
		
		Variable equal = model.newVariable(name, min, max, true);
		
		
		model.newExpression(left, right, OpType.SUB, equal);
		
		return equal;
	}
	
}

package model;

import java.util.TreeSet;
import model.Global.OpType;


/**
 * Substitution handling class.
 * Whenever one operand of a constraint is not a single variable (ie A < B+C),
 * the resulting expression is converted into a special substitution constraint,
 * called Expression.
 * 
 * <br>The B+C part becomes S1 = B+C, while S1 is used in A<S1
 * 
 * <br>An expression is similar to a constraint, only it has another variable
 * ("equal = left [operator] right")
 * 
 * <br>Lastly, since expression is not binary anymore, its handling is slightly
 * different:
 * Whenever a variable's domain has changed in an algorithm, AND this variable
 * is concerned by an expression, then the whole expressions/substitutions list
 * is updated with the Expression.updateDomain.
 * Then the algorithm can procede.
 * 
 * <br><br>updateDomain consists in re-calculating the right side domain
 * (using Xi [operator] Xj on any Xi in "left" and Xj in "right")
 * The resulting domain become the new domain of the "equal" part.
 * 
 * <br>Since expressions are done in substitution order, applying updates in the
 * same order is enough to guarantee that every dependency is covered.
 * 
 * @author Mathias COQBLIN
 */
public class Expression extends Constraint {

	protected Variable				equal;
	protected Operator				expressionOperator;
	
	// See Variable for use
	/*protected ArrayList<Integer>	baseDomain;
	protected ArrayList<Integer>	domain;
	protected ArrayList<Integer>	excludedDomain;*/

	/**
	 * @param left
	 * @param op
	 * @param right
	 * @param equal
	 */
	public Expression(Variable left, OpType op, Variable right, Variable equal)/* throws ModelException */{
		super(left, Global.OpType.EQUAL, right);
		Operator o = Global.getOperator(op);
		
		/*
		 * if(!o.isArithmetical()){ throw new ModelException("The expression's operator must be arithmetical!"); }
		 */
		
		this.equal = equal;
		this.expressionOperator = o;
		
		// Base domain is the equal part domain (thanks to the translator)
		/*this.baseDomain = equal.getBaseDomain();
		this.domain = equal.getBaseDomain();
		this.excludedDomain = new ArrayList<Integer>();*/
	}

	public Variable getEqual() {
		return equal;
	}

	public boolean equals(Object obj) {
		return (obj instanceof Expression && this.left.equals(((Expression) obj).left)
				&& this.right.equals(((Expression) obj).right) && this.equal.equals(((Expression) obj).equal) && this.op
				.equals(((Expression) obj).op));
	}
	
	
	
	
	
	/*public ArrayList<Integer> getBaseDomain() {
		return baseDomain;
	}
	public ArrayList<Integer> getDomain() {
		return domain;
	}
	public ArrayList<Integer> getExcludedDomain() {
		return excludedDomain;
	}
	void resetDomain() {
		domain.clear();
		excludedDomain.clear();
	}*/
	
	
	
	/* Updating the EQUAL domain, according to LEFT x RIGHT */
	public void updateDomain() {
		
		// Reseting domain to baseDomain, and excludedDomain to {}
		equal.resetDomain();
		
		// Ensures no duplicates + auto-sorting (+ log(n) time operation)
		TreeSet<Integer> newDom = new TreeSet<Integer>();
		
		
		// Calculating remaining domain
		for (Integer xi : left.getDomain()) {
			for (Integer xj : right.getDomain()) {
				
				switch (op.getType()) {
					case ADD:
						newDom.add(new Integer(xi + xj));
						break;
					case SUB:
						newDom.add(new Integer(xi - xj));
						break;
					case MUL:
						newDom.add(new Integer(xi * xj));
						break;
					case DIV:
						if (xj != 0) {
							newDom.add(new Integer(xi / xj));
						}
						break;
					default: break;
				}
				
			}
		}
		
		
		// Re-excluding equal's domain from what we did not calculate
		for (Integer i : equal.getBaseDomain()) {
			if (!newDom.contains(i))
				equal.exclude(i);
		}
	}
}

package model;

import java.util.TreeSet;


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
public class Substitution extends Constraint {

	protected Variable				equal;
	protected Operator.Arithmetic	expressionOperator;
	
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
	public Substitution(Variable left, Operator.Arithmetic op, Variable right, Variable equal) {
		super(left, Operator.Constraint.EQUAL, right);
		
		this.equal = equal;
		this.expressionOperator = op;
	}

	public Variable getEqual() {
		return equal;
	}

	public boolean equals(Object obj) {
		return (obj instanceof Substitution && this.left.equals(((Substitution) obj).left)
				&& this.right.equals(((Substitution) obj).right) && this.equal.equals(((Substitution) obj).equal) && this.op
				.equals(((Substitution) obj).op));
	}
	
	
	
	
	/* Updating the EQUAL domain, according to LEFT x RIGHT */
	public void updateDomain() {
		
		// Reseting domain to baseDomain, and excludedDomain to {}
		equal.resetDomain();
		
		// Ensures no duplicates + auto-sorting (+ log(n) time operation)
		TreeSet<Integer> newDom = new TreeSet<Integer>();
		
		
		// Calculating remaining domain
		for (Integer xi : left.getDomain()) {
			for (Integer xj : right.getDomain()) {
				
				switch (expressionOperator) {
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
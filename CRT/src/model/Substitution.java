package model;


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

	protected Variable				substitutionVariable;
	protected Operator.Arithmetic	expressionOperator;

	/**
	 * @param left
	 * @param op
	 * @param right
	 * @param substitutionVariable
	 */
	public Substitution(Variable left, Operator.Arithmetic op, Variable right, Variable substitutionVariable) {
		super(left, Operator.Constraint.EQUAL, right);
		
		this.substitutionVariable = substitutionVariable;
		this.expressionOperator = op;
	}

	public Variable getEqual() {
		return substitutionVariable;
	}

	public boolean equals(Object obj) {
		return (obj instanceof Substitution && this.left.equals(((Substitution) obj).left)
				&& this.right.equals(((Substitution) obj).right) && this.substitutionVariable.equals(((Substitution) obj).substitutionVariable) && this.op
				.equals(((Substitution) obj).op));
	}
	
	
	/* Updating the EQUAL domain, according to LEFT x RIGHT */
	public void updateDomain() {
		Domain newDomain = left.domain.arithmetic_operation
				(
				expressionOperator, 
				right.domain
				);
		substitutionVariable.domain.restrict(newDomain);
	}
}

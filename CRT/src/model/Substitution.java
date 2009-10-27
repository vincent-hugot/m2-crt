package model;


/**
 * Substitution handling class. Whenever one operand of a constraint is not a
 * single variable (ie A < B+C), the resulting expression is converted into a
 * Substitunio object.
 * 
 * <br>
 * The B+C part becomes S1 = B+C, while S1 is used in A<S1
 * 
 * <br>
 * A substitution is similar to a constraint, only it has another variable
 * ("equal = left [operator] right")
 * 
 * <br>
 * Lastly, since expression is not binary anymore, its handling is slightly
 * different. See AC3 algorithm for details.
 */
public class Substitution extends Constraint {
	
	protected Variable substitutionVariable;
	protected Operator.Arithmetic substitutionOperator;
	
	/**
	 * @param left
	 * @param op
	 * @param right
	 * @param substitutionVariable
	 */
	public Substitution(Variable left, Operator.Arithmetic op, Variable right,
			Variable substitutionVariable) {
		super(left, Operator.Constraint.EQUAL, right);
		
		this.substitutionVariable = substitutionVariable;
		this.substitutionOperator = op;
	}
	
	public Variable getEqual() {
		return substitutionVariable;
	}
	
	
	public Operator.Arithmetic getSubstitutionOperator() {
		return substitutionOperator;
	}
	
	public Variable getSubstitutionVariable() {
		return substitutionVariable;
	}
	
	
	public boolean equals(Object obj) {
		return (obj instanceof Substitution && this.left.equals(((Substitution) obj).left)
				&& this.right.equals(((Substitution) obj).right)
				&& this.substitutionVariable.equals(((Substitution) obj).substitutionVariable) && this.op
				.equals(((Substitution) obj).op));
	}
	
	
	/**
	 * Variable identification doesn't matter here (see Constraint.areValidValues),
	 * since equal, left and right are (and must be) given in that exact order
	 * 
	 * @param equal
	 * @param left
	 * @param right
	 * @return
	 */
	public boolean areValidValues(int equal, int left, int right) {
		boolean res;
		res = (this.substitutionVariable.domain.contains(equal)
				&& this.right.domain.contains(right) && this.left.domain.contains(left));
		
		if (res) {
			switch (substitutionOperator) {
				
				case ADD:
					res = (equal == (left + right));
					break;
				case SUB:
					res = (equal == (left - right));
					break;
				case MUL:
					res = (equal == (left * right));
					break;
				case DIV:
					res = ((right != 0) && (equal == (left / right)));
					break;
			}
		}
		
		return res;
	}
	
	
	
	
	public String toString() {
		String str = substitutionVariable.getName() + " = " + left.getName();
		
		switch (substitutionOperator) {
			case ADD: str += " + ";
				break;
			case SUB: str += " - ";
				break;
			case MUL: str += " * ";
				break;
			case DIV: str += " / ";
				break;
		}
		
		str += right.getName();
		
		return str;
	}
}

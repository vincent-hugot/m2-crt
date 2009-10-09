package model;

public class Expression extends Constraint {

	
	private Variable equal;

	/**
	 * @param left
	 * @param op
	 * @param right
	 * @param equal
	 */
	public Expression(Variable left, Operator op, Variable right, Variable equal) {
		super(left, op, right);
		this.equal = equal;
	}

	public Variable getEqual() {
		return equal;
	}

	public void setEqual(Variable equal) {
		this.equal = equal;
	}
	
	

}

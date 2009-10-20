package model;

public class Expression extends Constraint {

	private Variable	equal;

	/**
	 * @param left
	 * @param op
	 * @param right
	 * @param equal
	 */
	public Expression(Variable left, Global.OpType op, Variable right, Variable equal) {

		super(left, op, right);
		this.equal = equal;
	}

	public Variable getEqual() {
		return equal;
	}

	public boolean equals(Object O) {
		return (O instanceof Expression && this.left.equals(((Expression) O).left)
				&& this.right.equals(((Expression) O).right) && this.equal.equals(((Expression) O).equal) && this.op.equals(((Expression) O).op));
	}
}

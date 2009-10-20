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

	public boolean equals(Object obj) {
		return (obj instanceof Expression && this.left.equals(((Expression) obj).left)
				&& this.right.equals(((Expression) obj).right) && this.equal.equals(((Expression) obj).equal) && this.op
				.equals(((Expression) obj).op));
	}
}

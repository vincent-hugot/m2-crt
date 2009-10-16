package model;

public class Constraint {

	private Variable	left;
	private Variable	right;

	private Operator	op;

	/**
	 * @param left
	 * @param op
	 * @param right
	 */
	public Constraint(Variable left, Operator op, Variable right) {

		this.left = left;
		this.op = op;
		this.right = right;
	}

	public Variable getLeft() {

		return left;
	}

	public Variable getRight() {

		return right;
	}

	public Operator getOp() {

		return op;
	}

}

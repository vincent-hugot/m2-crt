package model;

public class Constraint {
	private Variable left;
	private Variable right;
	
	private Operator op;

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

	public void setLeft(Variable left) {
		this.left = left;
	}

	public Variable getRight() {
		return right;
	}

	public void setRight(Variable right) {
		this.right = right;
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}
	
	
}

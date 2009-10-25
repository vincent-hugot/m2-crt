package model;


public class Constraint {

	protected Variable	left;
	protected Variable	right;

	protected Operator.Constraint	op;

	/**
	 * @param left
	 * @param op
	 * @param right
	 */
	public Constraint(Variable left, Operator.Constraint op, Variable right) {
		
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

	public Operator.Constraint getOp() {
		return op;
	}

	public boolean equals(Object obj) {
		return (obj instanceof Constraint && this.left.equals(((Constraint) obj).left)
				&& this.op == ((Constraint) obj).op && this.right.equals(((Constraint) obj).right));
	}
	
	public boolean areValidValues(int leftValue, int rightValue) {
		boolean res;
		res = (left.domain.contains(leftValue) && right.domain.contains(rightValue));
		
		if (res) {
			switch (op) {
				
				case EQUAL:
					res = (leftValue == rightValue);
					break;
				case NOT_EQUAL:
					res = (leftValue != rightValue);
					break;
				case GREATER:
					res = (leftValue > rightValue);
					break;
				case LOWER:
					res = (leftValue < rightValue);
					break;
				case GREATER_OR_EQUAL:
					res = (leftValue >= rightValue);
					break;
				case LOWER_OR_EQUAL:
					res = (leftValue <= rightValue);
					break;
			}
		}
		return res;
	}
}

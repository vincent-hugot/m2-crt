package model;

public class Constraint {

	protected Variable	left;
	protected Variable	right;

	protected Operator	op;

	/**
	 * @param left
	 * @param op
	 * @param right
	 */
	public Constraint(Variable left, Global.OpType op, Variable right)/*throws ModelException*/ {
		
		Operator oper = Global.getOperator(op);
		
		/*if(oper.isArithmetical()){
			throw new ModelException("The constraint operator must be not arithmetical");
		}*/
		
		this.left = left;
		this.op = oper;
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

	public boolean equals(Object obj) {
		return (obj instanceof Constraint && this.left.equals(((Constraint) obj).left)
				&& this.op.equals(((Constraint) obj).op) && this.right.equals(((Constraint) obj).right));
	}
}

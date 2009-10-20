package model;

public class Expression extends Constraint {

	protected Variable	equal;
	protected Operator expressionOperator;
	
	/**
	 * @param left
	 * @param op
	 * @param right
	 * @param equal
	 */
	public Expression(Variable left, Global.OpType op, Variable right, Variable equal)/*throws ModelException*/ {

		super(left, Global.OpType.EQUAL, right);
		Operator o = new Operator(op);
		
		/*if(!o.isArithmetical()){
			throw new ModelException("The expression's operator must be arithmetical!");
		}*/
		
		this.expressionOperator = o;
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

package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Expression extends Constraint {

	protected Variable				equal;
	protected Operator				expressionOperator;
	protected ArrayList<Integer>	domain;
	protected ArrayList<Integer>	excludedDomain;

	/**
	 * @param left
	 * @param op
	 * @param right
	 * @param equal
	 */
	public Expression(Variable left, Global.OpType op, Variable right, Variable equal)/* throws ModelException */{

		super(left, Global.OpType.EQUAL, right);
		Operator o = Global.getOperator(op);
		/*
		 * if(!o.isArithmetical()){ throw new ModelException("The expression's operator must be arithmetical!"); }
		 */

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

	public void updateDomain() {
		Iterator<Integer> itLeft, itRight, itDom;
		int l, r;
		Integer i;
		ArrayList<Integer> newDom = new ArrayList<Integer>();
		equal.resetExcludedDomain();

		itLeft = left.getRemainingDomain().iterator();

		// Creation of the new domain of the Expression
		while (itLeft.hasNext()) {
			l = itLeft.next().intValue();
			itRight = right.getRemainingDomain().iterator();

			while (itRight.hasNext()) {
				r = itRight.next();
				switch (op.getType()) {
				case ADD:
					newDom.add(new Integer(l + r));
					break;
				case SUB:
					newDom.add(new Integer(l - r));
					break;
				case MUL:
					newDom.add(new Integer(l * r));
					break;
				case DIV:
					if (r != 0) {
						newDom.add(new Integer(l / r));
					}
					break;
				default:
					;
				}
			}

		}

		// Updating the excludedDomain of the equal variable (Domain - newDom)
		itDom = equal.getDomain().iterator();
		while (itDom.hasNext()) {
			i = itDom.next();
			if(!newDom.contains(i))
			{
				equal.exclude(i.intValue());
			}
		}
	}
}

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
	
	
	
	/**
	 * True if this is a constraint between Xi and Xj, and Xi != Xj UNLESS left = right
	 * @param xi
	 * @param xj
	 * @return
	 */
	public boolean isCij(Variable xi, Variable xj) {
		if (xi == left && xj == right) return true;
		if (xi == right && xj == left) return true;
		return false;
	}
	
	
	/**
	 * Calculate if Ai and Aj are valid values of Xi and Xj, according to this constraint.
	 * Xi and Xj are used to determine what Variable is the left/right one.
	 * 
	 * If Xi is left and Xj is right, we treat Ai [op] Aj
	 * If Xi is right and Xj is left, we treat Aj [op] Ai
	 * (Anytime Ai is in Xi and Aj in Xj, only Xi/Xj and left/right matches matters)
	 * 
	 * @param xi might be left or right
	 * @param xj is the other
	 * @param ai MUST be the element of D(xi) studied (=> if Xi is right, Ai is in right)
	 * @param aj MUST be the element of D(xj) studied
	 * @return
	 */
	public boolean areValidValues(Variable xi, Variable xj, int ai, int aj) {	
		
		// Invalid variables (we must find Left and Right amongst Xi and Xj
		if (!((xi == left && xj == right) || (xi == right || xj == left))) return false;
		
		// Invalid values (considering Ai in Xi, Aj in Xj)
		if (!xi.domain.contains(ai) || !xj.domain.contains(aj)) return false;
		
		
		// left/right determination
		if (xi == left) { // Constraint is Xi [op] Xj (same order)
			
			switch (op) {
				case EQUAL: return (ai == aj);
				case NOT_EQUAL: return (ai != aj);
				case GREATER: return (ai > aj);
				case LOWER: return (ai < aj);
				case GREATER_OR_EQUAL: return (ai >= aj);
				case LOWER_OR_EQUAL: return (ai <= aj);
			}
		}
		
		else { // Constraint is Xj [op] Xi (reverse order)
			
			switch (op) {
				case EQUAL: return (aj == ai);
				case NOT_EQUAL: return (aj != ai);
				case GREATER: return (aj > ai);
				case LOWER: return (aj < ai);
				case GREATER_OR_EQUAL: return (aj >= ai);
				case LOWER_OR_EQUAL: return (aj <= ai);
			}
		}
		
		// If anything else fail.
		return false;
	}
	
	
	public String toString() {
		String str = left.getName();
		
		switch (op) {
			case EQUAL: str += " = ";
				break;
			case NOT_EQUAL: str += " != ";
				break;
			case GREATER: str += " > ";
				break;
			case LOWER: str += " < ";
				break;
			case GREATER_OR_EQUAL: str += " >= ";
				break;
			case LOWER_OR_EQUAL: str += " <= ";
				break;
		}
		
		str += right.getName();
		
		return str;
	}
}

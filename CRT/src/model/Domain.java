package model;

import java.util.TreeSet;


/**
 * Mutable domain with arithmetic facilities.
 * 
 * @author Vincent HUGOT
 *
 */
public class Domain extends TreeSet<Integer> {
	
	/**
	 * Eclipse WTF.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Build the empty set
	 */
	public Domain() {
		super();
	}
	
	/**
	 * build the interval [min,..,max]
	 * @param min
	 * @param max
	 */
	public Domain(int min, int max) {
		super();
		for (int i = min; i <= max ; i++) {
			this.add(i);
		}
	}
	

	
		
	public int greatest() {
		return last();
	}
	
	public int least() {
		return first();
	}
	
	
	/**
	 * Returns the least element in this set greater 
	 * than or equal to the given element, or null if 
	 * there is no such element.
	 * @param n
	 */
	public int next(int n) {
		return ceiling(n);
	}
	
	/**
	 * Remove from this domain the elements of another domain
	 * Corresponds to a mutable intersection
	 * 
	 * @param d another domain, unaffected by the operation
	 */
	public void restrict (Domain d) {
		this.removeAll(d);
	}
	
	/**
	 * Add to this domain all elements of another domain
	 * Corresponds to a mutable union
	 * 
	 * @param d another domain, unaffected by the operation
	 */
	public void extend(Domain d){
		addAll(d);
	}
	
	
	/**
	 * Returns true if n is in the domain
	 * @param n
	 * @return
	 */
	public boolean contains(int n) {
		return contains(n);
	}
	
	/**
	 * Returns a new domain, which is the "product" of this domain
	 * and of the domain D passed as an argument, with respect to an 
	 * arithmetic operator.
	 * 
	 * Both this domain and D remain unchanged. 
	 * 
	 * For instance this.arithmetic_operation(OP, D) is 
	 * { c | exists a in this and b in D, c = a OP b }
	 * @param op an arithmetic operator
	 * @param d another domain
	 * @return the set of c such that c = a OP b
	 */
	public Domain arithmeticOperation(Operator.Arithmetic op, Domain d) {
		Domain resultingDomain = new Domain();
		for (Integer a : this) {
			for (Integer b : d) {
				Integer c = null;
				switch (op) {
				case ADD:
					c = a+b;
					break;
				case SUB:
					c = a-b;
					break;
				case DIV:
					if (b != 0) c = a/b;
					break;
				case MUL:
					c = a*b;
					break;
				}
				if (c != null) resultingDomain.add(c);
			}
		}
		return resultingDomain;
	}

}

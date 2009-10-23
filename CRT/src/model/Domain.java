package model;

import java.util.TreeSet;


/**
 * Mutable domain with arithmetic facilities.
 * 
 * @author Vincent HUGOT
 *
 */
public class Domain {
	
	TreeSet<Integer> set = new TreeSet<Integer>();
	
	/**
	 * Build the empty set
	 */
	public Domain() {
		
	}
	
	/**
	 * build the interval [min,..,max]
	 * @param min
	 * @param max
	 */
	public Domain(int min, int max) {
		for (int i = min; i <= max ; i++) {
			set.add(i);
		}
	}
	
	/**
	 * add an element to the domain
	 * @param n
	 */
	public void add(int n) {
		set.add(n);
	}
	
	/**
	 * remove an element from the domain
	 * @param n
	 */
	public void remove(int n) {
		set.remove(n);
	}
	
	/**
	 * Returns the greatest element of the set
	 */
	public void last() {
		set.last();
	}
	
	/**
	 * Returns the least element in this set greater 
	 * than or equal to the given element, or null if 
	 * there is no such element.
	 * @param n
	 */
	public void next(int n) {
		set.ceiling(n);
	}
	
	/**
	 * Remove from this domain the elements of another domain
	 * Corresponds to a mutable intersection
	 * 
	 * @param d another domain, unaffected by the operation
	 */
	public void restrict (Domain d) {
		set.removeAll(d.set);
	}
	
	/**
	 * Add to this domain all elements of another domain
	 * Corresponds to a mutable union
	 * 
	 * @param d another domain, unaffected by the operation
	 */
	public void extend(Domain d){
		set.addAll(d.set);
	}
	
	
	/**
	 * Returns true if n is in the domain
	 * @param n
	 * @return
	 */
	public boolean contains(int n) {
		return set.contains(n);
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
	public Domain arithmetic_operation(Operator.Arithmetic op, Domain d) {
		Domain resultingDomain = new Domain();
		for (Integer a : set) {
			for (Integer b : d.set) {
				int c = 0;
				switch (op) {
				case ADD:
					c = a+b;
					break;
				case SUB:
					c = a-b;
					break;
				case DIV:
					c = a/b;
					break;
				case MUL:
					c = a*b;
					break;
				}
				resultingDomain.add(c);
			}
		}
		return resultingDomain;
	}

}

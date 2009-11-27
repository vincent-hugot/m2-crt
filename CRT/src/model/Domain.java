package model;

import java.util.TreeSet;


/**
 * Mutable domain with arithmetic facilities.
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
		return ceiling(n+1);
	}
	
	/**
	 * Remove from this domain the elements which are not also
	 * in another domain.
	 * 
	 * Corresponds to a mutable intersection
	 * 
	 * @param d another domain, unaffected by the operation
	 * 
	 * @return true if there was a restriction done
	 * 		(ie true if this TreeSet changed from removeAll call)
	 */
	public boolean restrict(Domain d) {
		return retainAll(d);
	}
	
	/**
	 * Add to this domain all elements of another domain
	 * Corresponds to a mutable union
	 * 
	 * @param d another domain, unaffected by the operation
	 * 
	 * @return true if there was an extension done
	 * 		(ie true if this TreeSet changed from addAll call)
	 */
	public boolean extend(Domain d) {
		return addAll(d);
	}
	
	
	
	public void remove(int n) {
		this.remove(new Integer(n));
	}
	
	/**
	 * Yields a compact LaTeX string representing 
	 * the domain
	 * @return a LaTeX string
	 */
	public String toLaTeX()
	{
		if (this.isEmpty())
		{
			return "$\\emptyset$";
		}
		
		StringBuilder res = new StringBuilder("$\\{");
		
		int head = last() +1;
		int tail = head;
		
		for (int i : this) {
			if (i < head)
			{//init
				head = tail = i;
			}
			else if (i == tail+1)
			{
				tail++;
			}
			else
			{
				if (head == tail)
				{
					res.append("," + head);
				}
				else if (tail == head + 1)
				{
					res.append(head + "," + tail);
				}
				else
				{
					res.append(head + ".." + tail);
				}
				head = tail = i;
			}
		}
		
		if (head == tail)
		{
			if (size() > 1)
				res.append(",");
			res.append(head);
		}
		else if (tail == head + 1)
		{
			res.append(head + "," + tail);
		}
		else
		{
			res.append(head + ".." + tail);
		}
		
		return res + "\\}$";
	}
	
	
	/**
	 * Returns a new domain, which is the "product" of this domain
	 * and of the domain D passed as an argument, with respect to an 
	 * arithmetic operator.
	 * 
	 * Both this domain and D remain unchanged. 
	 * 
	 * For instance this.arithmeticOperation(OP, D) is 
	 * { a OP b | a in this and b in D }
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

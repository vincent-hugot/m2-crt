package model;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Substitution class tests.
 * @author Mathias COQBLIN
 */
public class SubstitutionTest {
	
	/**
	 * Same test as in constraints.
	 * Less operators, added left operand, must considerate /0.
	 * 3 invalid domain, 2*4 operations types, div by 0 => 12 cases.
	 */
	@Test
	public void testValidValues() {
		Variable equal = new Variable("Z",1,3);
		Variable left = new Variable("A",1,3);
		Variable right = new Variable("B",1,3);
		
		Substitution s1 = new Substitution(left, Operator.Arithmetic.ADD, right, equal);
		Substitution s2 = new Substitution(left, Operator.Arithmetic.SUB, right, equal);
		Substitution s3 = new Substitution(left, Operator.Arithmetic.MUL, right, equal);
		Substitution s4 = new Substitution(left, Operator.Arithmetic.DIV, right, equal);
		
		// Out of domain
		assertFalse(s1.areValidValues(42,1,1));
		assertFalse(s1.areValidValues(1,42,1));
		assertFalse(s1.areValidValues(1,1,42));
		
		// ADD
		assertTrue(s1.areValidValues(3,1,2));
		assertFalse(s1.areValidValues(1,1,2));
		
		// SUB
		assertTrue(s2.areValidValues(2,3,1));
		assertFalse(s2.areValidValues(3,3,1));
		
		// MUL
		assertTrue(s3.areValidValues(2,1,2));
		assertFalse(s3.areValidValues(3,1,2));
		
		// DIV
		assertTrue(s4.areValidValues(2,2,1));
		assertFalse(s4.areValidValues(3,2,1));
		assertFalse(s4.areValidValues(2,2,0)); // div by 0
	}
	
}

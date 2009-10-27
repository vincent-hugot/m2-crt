package model;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Constraint class tests.
 * @author Mathias COQBLIN
 */
public class ConstraintTest {
	
	/**
	 * left/right not in domains, 6 constraints types, true/false study
	 * => 12+2 = 14 cases for good condition coverage.
	 * 
	 * UPDATE: Doubling number of tests to test cases with reversed values
	 */
	@Test
	public void testValidValues() {
		Variable v1 = new Variable("A",1,2);
		Variable v2 = new Variable("B",2,3);
		Variable v2bis = new Variable("BB",1,2);
		
		Constraint c1 = new Constraint(v1, Operator.Constraint.EQUAL, v2);
		Constraint c2 = new Constraint(v1, Operator.Constraint.GREATER, v2bis);
		Constraint c3 = new Constraint(v1, Operator.Constraint.GREATER_OR_EQUAL, v2);
		Constraint c4 = new Constraint(v1, Operator.Constraint.LOWER, v2);
		Constraint c5 = new Constraint(v1, Operator.Constraint.LOWER_OR_EQUAL, v2bis);
		Constraint c6 = new Constraint(v1, Operator.Constraint.NOT_EQUAL, v2);
		
		// Out of domain
		assertFalse(c1.areValidValues(v1,v2, 3,3));
		assertFalse(c1.areValidValues(v1,v2, 1,1));
		
		// Fail (stupid variables)
		assertFalse(c1.areValidValues(v1,v2bis, 1,1));
		assertFalse(c1.areValidValues(v2bis,v2, 1,1));
		
		
		// EQUAL
		assertTrue(c1.areValidValues(v1,v2, 2,2));
		assertFalse(c1.areValidValues(v1,v2, 2,3));
		
		assertTrue(c1.areValidValues(v2,v1, 2,2));
		assertFalse(c1.areValidValues(v2,v1, 3,2));
		
		
		// GREATER
		assertTrue(c2.areValidValues(v1,v2bis, 2,1));
		assertFalse(c2.areValidValues(v1,v2bis, 1,1));
		
		assertTrue(c2.areValidValues(v2bis,v1, 1,2));
		assertFalse(c2.areValidValues(v2bis,v1, 1,1));
		
		
		// GREATER_OR_EQUAL
		assertTrue(c3.areValidValues(v1,v2, 2,2));
		assertFalse(c3.areValidValues(v1,v2, 2,3));
		
		assertTrue(c3.areValidValues(v2,v1, 2,2));
		assertFalse(c3.areValidValues(v2,v1, 3,2));
		
		
		// LOWER
		assertTrue(c4.areValidValues(v1,v2, 1,2));
		assertFalse(c4.areValidValues(v1,v2, 2,2));
		
		assertTrue(c4.areValidValues(v2,v1, 2,1));
		assertFalse(c4.areValidValues(v2,v1, 2,2));
		
		
		// LOWER_OR_EQUAL
		assertTrue(c5.areValidValues(v1,v2bis, 2,2));
		assertFalse(c5.areValidValues(v1,v2bis, 2,1));
		
		assertTrue(c5.areValidValues(v2bis,v1, 2,2));
		assertFalse(c5.areValidValues(v2bis,v1, 1,2));
		
		
		// NOT_EQUAL
		assertTrue(c6.areValidValues(v1,v2, 1,2));
		assertFalse(c6.areValidValues(v1,v2, 2,2));
		
		assertTrue(c6.areValidValues(v2,v1, 2,1));
		assertFalse(c6.areValidValues(v2,v1, 2,2));
		
	}
	
}

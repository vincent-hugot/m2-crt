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
		assertFalse(c1.areValidValues(3,3));
		assertFalse(c1.areValidValues(1,1));
		
		// EQUAL
		assertTrue(c1.areValidValues(2,2));
		assertFalse(c1.areValidValues(2,3));
		
		// GREATER
		assertTrue(c2.areValidValues(2,1));
		assertFalse(c2.areValidValues(1,1));
		
		// GREATER_OR_EQUAL
		assertTrue(c3.areValidValues(2,2));
		assertFalse(c3.areValidValues(2,3));
		
		// LOWER
		assertTrue(c4.areValidValues(1,2));
		assertFalse(c4.areValidValues(2,2));
		
		// LOWER_OR_EQUAL
		assertTrue(c5.areValidValues(2,2));
		assertFalse(c5.areValidValues(2,1));
		
		// NOT_EQUAL
		assertTrue(c6.areValidValues(1,2));
		assertFalse(c6.areValidValues(2,2));
	}
	
}

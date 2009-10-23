package model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;


/**
 * Constraint class tests.
 * @author Mathias COQBLIN
 */
public class ConstraintTest {
	
	// TODO : Redo with formal coverage
	/**
	 * Depends on isValidValue on both sides
	 */
	@Test
	public void testAreValidValues() {
		
		Variable v1 = mock(Variable.class);
		Variable v2 = mock(Variable.class);
		
		when(v1.isValidValue(1)).thenReturn(true);
		when(v1.isValidValue(2)).thenReturn(false);
		when(v2.isValidValue(16)).thenReturn(true);
		when(v2.isValidValue(42)).thenReturn(false);
		
		Constraint c = new Constraint(v1, Operator.Constraint.NOT_EQUAL, v2);
		
		assertTrue(c.areValidValues(1,16));
		assertFalse(c.areValidValues(1,42));
		assertFalse(c.areValidValues(2,16));
		assertFalse(c.areValidValues(2,42));
	}
}

package model;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Constant class tests. Most is tested in VariableTest
 * @author Mathias COQBLIN
 */
public class ConstantTest {
	
	
	/**
	 * Constant = Variable with restricted and predefined information
	 */
	@Test
	public void testConstantIsARestrictedVariable() {
		Constant c = new Constant(42);
        Domain domain = new Domain(42,42);
		
		assertEquals(c.getDomain() ,domain);
		assertEquals(c.getName(), "42");
	}
	
	
	
	@Test
	public void testEquals() {
		assertEquals(new Constant(42), new Constant(42));
	}
}

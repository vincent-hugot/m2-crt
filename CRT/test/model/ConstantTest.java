package model;

import static org.junit.Assert.*;
import java.util.ArrayList;
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
		ArrayList<Integer> domain = new ArrayList<Integer>();
		domain.add(42);
		
		assertEquals(c.getBaseDomain(),domain);
		assertEquals(c.getDomain(),domain);
		assertEquals(c.getName(), "42");
	}
	
	
	@Test
	public void testValueDomain() {
		Constant c = new Constant(42);

		assertEquals(c.getBaseDomain().size(), 1);
		assertEquals(c.getBaseDomain().get(0).intValue(), c.getValue());
	}
	
	
	@Test
	public void testEquals() {
		assertEquals(new Constant(42), new Constant(42));
	}
}

package model;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Variable class tests.
 * Few critical checking is done (mostly "are my input values correct?")
 * 
 * @author Mathias COQBLIN
 */
public class VariableTest {
	
	
	/**
	 * Condition coverage.
	 * Domain comparison is the same for baseDomain, domain and excludedDomain
	 * (they are supposed to respect "base = domain \/ excluded" anytime)
	 */
	@Test
	public void testEquals() {
		Variable v = new Variable("X",0,5);
		
		String v1 = "X";
		Variable v2 = new Variable("Y",0,5);
		Variable v3 = new Variable("X",0,5,true);
		Variable v4 = new Variable("X",1,4);
		Variable v5 = new Variable("X",0,5);
		
		// v != Variable
		assertFalse(v.equals(v1));
		
		// v == Variable, v.name != this.name
		assertFalse(v.equals(v2));
		
		// v == Variable, v.name == this.name, v.artificial != this.artificial
		assertFalse(v.equals(v3));
		
		// v == Variable, v.name == this.name, v.artificial == this.artificial
		// v.domain != this. domain
		assertFalse(v.equals(v4));
		
		// all true
		assertTrue(v.equals(v5));
	}
	
	
	
	/**
	 * Constructors are equivalent
	 */
	@Test
	public void testNewVariable() {
		Domain domain123 = new Domain(1,3);
		
		Variable v1 = new Variable("X",domain123,false);
		Variable v2 = new Variable("X",domain123);
		Variable v3 = new Variable("X",1,3,false);
		Variable v4 = new Variable("X",1,3);
		
		assertEquals(v1,v2);
		assertEquals(v1,v3);
		assertEquals(v1,v4);
	}
	
	
	
	/**
	 * Various getters checking
	 */
	@Test
	public void testGetters() {
		Domain domain123 = new Domain(1,3);
		
		Variable v = new Variable("X",1,3);
		
		assertEquals(v.getName(),"X");
		assertFalse(v.isArtificial());
		assertEquals(v.getDomain(), domain123);
		
		// Node : Constraints and Substitutions lists are tested elsewhere
	}
	
		
	
	
	/**
	 * Checking an invariant on domains:
	 * baseDomain = domain \/ excludedDomain
	 * & domain /\ excludedDomain = {}
	 * 
	 * TODO : this will go in future Domain class.
	 */
	@Test
	public void testInvariantDomain() {
		
	}
}

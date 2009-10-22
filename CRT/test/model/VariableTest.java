package model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;


/**
 * Variable class tests.
 * @author Mathias COQBLIN
 */
public class VariableTest {
	
	
	@Test
	public void testEquals() {
		Variable v1 = new Variable("X",0,5);
		Variable v2 = new Variable("X",0,5);
		Variable v3 = new Variable("Y",0,5);
		Variable v4 = new Variable("X",1,4);

		assertEquals(v1,v1);
		assertEquals(v1,v2);
		assertNotSame(v1,v3);
		assertNotSame(v1,v4);
		
		assertEquals(v2,v2);
		assertNotSame(v2,v3);
		assertNotSame(v2,v4);

		assertEquals(v3,v3);
		assertNotSame(v3,v4);
		
		assertEquals(v4,v4);
	}
	
	
	/**
	 * Constructors are equivalent
	 */
	@Test
	public void testNewVariable() {
		ArrayList<Integer> domain123 = new ArrayList<Integer>();
		domain123.add(1); domain123.add(2); domain123.add(3);
		
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
		ArrayList<Integer> domain123 = new ArrayList<Integer>();
		domain123.add(1); domain123.add(2); domain123.add(3);
		
		Variable v = new Variable("X",1,3);
		
		assertEquals(v.getName(),"X");
		assertFalse(v.isArtificial());
		assertEquals(v.getBaseDomain(), domain123);
		assertEquals(v.getDomain(), domain123);
		assertEquals(v.getExcludedDomain(), new ArrayList<Integer>());
		assertEquals(v.minBound(),1);
		assertEquals(v.maxBound(),3);
		
		// Node : Constraints, Expressions, and Neighbors lists are tested elsewhere
	}
	
	
	@Test
	public void testValidValue() {
		Variable v = new Variable("X",1,3);
		
		assertFalse(v.isValidValue(0));
		assertTrue(v.isValidValue(1));
		assertTrue(v.isValidValue(2));
		assertTrue(v.isValidValue(3));
		assertFalse(v.isValidValue(4));
	}
	
	
	@Test
	public void testExclude() {
		Variable v = new Variable("X",1,3);
		
		ArrayList<Integer> base = new ArrayList<Integer>();
		base.add(1); base.add(2); base.add(3);
		ArrayList<Integer> domain = new ArrayList<Integer>();
		ArrayList<Integer> excluded = new ArrayList<Integer>();
			domain.add(1); domain.add(2); domain.add(3);
		
		assertEquals(v.getBaseDomain(), base);
		assertEquals(v.getDomain(), domain);
		assertEquals(v.getExcludedDomain(), excluded);
		
		
		// Trying after a first exclusion
		v.exclude(2);
		domain.remove(new Integer(2));
		excluded.add(2);
		
		assertEquals(v.getBaseDomain(), base);
		assertEquals(v.getDomain(), domain);
		assertEquals(v.getExcludedDomain(), excluded);
		
		
		// Dumb second exclusion: excluded domain is not sorted correctly
		v.exclude(1);
		domain.remove(new Integer(1));
		excluded.add(1);
		
		assertEquals(v.getBaseDomain(), base);
		assertEquals(v.getDomain(), domain);
		assertNotSame(v.getExcludedDomain(), excluded);
		
		Collections.sort(excluded);
		assertEquals(v.getExcludedDomain(), excluded);
		
		
		// Trying to reexclude... should do nothing
		v.exclude(2);
		
		assertEquals(v.getBaseDomain(), base);
		assertEquals(v.getDomain(), domain);
		assertEquals(v.getExcludedDomain(), excluded);
	}
	
	
	
	/**
	 * Testing various additions (quick way, exhaustive one in ModelTest)
	 */
	@Test
	public void testAdd() {
		Variable v = new Variable("X",1,3);
		
		assertEquals(v.getAssociatedConstraints().size(), 0);
		assertEquals(v.getAssociatedExpressions().size(), 0);
		assertEquals(v.getNeighbors().size(), 0);
		
		v.addConstraint(mock(Constraint.class));
		v.addExpression(mock(Expression.class));
		v.addNeighbor(mock(Variable.class));
		
		assertEquals(v.getAssociatedConstraints().size(), 1);
		assertEquals(v.getAssociatedExpressions().size(), 1);
		assertEquals(v.getNeighbors().size(), 1);
	}
}

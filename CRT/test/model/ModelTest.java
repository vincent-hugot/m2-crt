package model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class ModelTest {

	private Model m;
	@Test
	public void testNewVariableStringIntIntBoolean() {
		Variable v1, v2;
		m = new Model();
		v1 = m.newVariable("toto", 1, 100, true);
		v2 = new Variable("toto", 1, 100, true);
		assertTrue(v2.equals(v1));
	}

	@Test
	public void testNewVariableStringIntInt() {
		Variable v1, v2;
		m = new Model();
		v1 = m.newVariable("toto", 1, 100);
		v2 = new Variable("toto", 1, 100);
		assertTrue(v2.equals(v1));
	}
	
	@Test
	public void testNewVariableDomain() {
		Variable v1, v2;
		m = new Model();
		v1 = m.newVariable("toto", new Domain(1,3), true);
		v2 = new Variable("toto", new Domain(1,3), true);
		assertTrue(v2.equals(v1));
	}

	@Test
	public void testNewConstant() {
		Constant c1, c2;
		m = new Model();
		c1 = m.newConstant(42);
		c2 = new Constant(42);
		assertTrue(c2.equals(c1));
	}

	@Test
	public void testNewSubstitution() {
		Substitution e;
		Variable v1, v2, v3;
		m = new Model();
		v1 = m.newVariable("X",42,42);
		v2 = m.newVariable("Y",0,1);
		v3 = m.newVariable("Z",1,2);
		m.newSubstitution(v1, v2, Operator.Arithmetic.ADD, v3);
		e = new Substitution(v1, Operator.Arithmetic.ADD, v2, v3);
		
		assertFalse(m.getConstraints().contains(e));
		assertTrue(m.getSubstitutions().contains(e));

		// Added constraints and substitutions
		// (Only 1 element if any, no need to test if it's "e"...)
		assertEquals(v1.getAssociatedConstraints().size(), 0);
		assertEquals(v1.getAssociatedSubstitutions().size(), 1);
		assertEquals(v2.getAssociatedConstraints().size(), 0);
		assertEquals(v2.getAssociatedSubstitutions().size(), 1);
		assertEquals(v3.getAssociatedConstraints().size(), 0);
		assertEquals(v3.getAssociatedSubstitutions().size(), 1);
	}

	@Test
	public void testNewConstraint() {
		Constraint c;
		Variable v1, v2;
		m = new Model();
		v1 = m.newVariable("X",42,42);
		v2 = m.newVariable("Y",0,1);
		m.newConstraint(v1, v2, Operator.Constraint.EQUAL);
		c = new Constraint(v1, Operator.Constraint.EQUAL, v2);
		
		assertTrue(m.getConstraints().contains(c));
		assertFalse(m.getSubstitutions().contains(c));
		
		// Added constraints and substitutions
		// (Only 1 element if any, no need to test if it's "c"...)
		assertEquals(v1.getAssociatedConstraints().size(), 1);
		assertEquals(v1.getAssociatedSubstitutions().size(), 0);
		assertEquals(v2.getAssociatedConstraints().size(), 1);
		assertEquals(v2.getAssociatedSubstitutions().size(), 0);
	}
	
	
	/**
	 * Testing getConstraintConcerningVariables()
	 */
	@Test
	public void testCij() {
		Model m = new Model();
		
		Variable v1,v2,v3;
		v1 = m.newVariable("A", 1,2);
		v2 = m.newVariable("B", 1,2);
		v3 = m.newVariable("C", 1,2);
		
		Constraint c1,c2;
		c1 = m.newConstraint(v1,v2, Operator.Constraint.EQUAL);
		c2 = m.newConstraint(v1,v2, Operator.Constraint.GREATER_OR_EQUAL);
		m.newConstraint(v1,v3, Operator.Constraint.LOWER);
		
		ArrayList<Constraint> res = new ArrayList<Constraint>();
		res.add(c1);
		res.add(c2);
		
		// How it work? Adds every v1 constraint (c1,c2,c3),
		// then remove any non-v2 constraint (c3)
		// => res = (c1,c3)
		assertEquals(m.getConstraintConcerningVariables(v1,v2), res);
	}
}

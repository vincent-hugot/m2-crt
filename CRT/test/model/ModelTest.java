package model;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.mockito.Mockito.*;

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
	public void testNewConstant() {
		Constant c1, c2;
		m = new Model();
		c1 = m.newConstant(42);
		c2 = new Constant(42);
		assertTrue(c2.equals(c1));
	}

	@Test
	public void testNewExpression() {
		Expression e;
		Variable v1, v2, v3;
		m = new Model();
		v1 = mock(Variable.class);
		v2 = mock(Variable.class);
		v3 = mock(Variable.class);
		m.newExpression(v1, v2, Global.OpType.ADD, v3);
		e = new Expression(v1, Global.OpType.ADD, v2, v3);
		assertTrue(m.getConstraints().contains(e));
	}

	@Test
	public void testNewConstraint() {
		Constraint c;
		Variable v1, v2;
		m = new Model();
		v1 = mock(Variable.class);
		v2 = mock(Variable.class);
		m.newConstraint(v1, v2, Global.OpType.ADD);
		c = new Constraint(v1, Global.OpType.ADD, v2);
		assertTrue(m.getConstraints().contains(c));
	}

}

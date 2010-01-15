package model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Operator.Constraint;

import org.junit.Before;
import org.junit.Test;

public class BackupTest {

	private Model m;
	private Variable x, y, z;
	
	
	@Before
	public void setUp() {

		//Constraint c1;
		//Constraint c2;
		m = new Model();
		
		x = m.newVariable("X", 0, 5);
		y = m.newVariable("Y", 5, 1);
		z = m.newVariable("Z", 0, 3);
		
		
		m.newConstraint(x, y, Constraint.LOWER);
		m.newConstraint(y, z, Constraint.GREATER);
		
	}
	
	@Test
	public void testBackupEquals() {
		Variable v = m.backup(x);
		assertEquals(v, x);
	}
	
	@Test
	public void testBackupNotTheSameReference() {
		Variable v = m.backup(x);
		assertNotSame(v, x);
	}
	
	@Test
	public void testBackupEmptyDomain() {
		Variable v = m.newVariable("Y", new Domain(), false);
		assertEquals(v, m.backup(y));
	}
	
	@Test
	public void testBackupNotFound() {
		Variable v = m.newVariable("Lolwut", new Domain(), false);
		assertEquals(v, m.backup(v));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBackupNoAlterartionsConstraints() {
		ArrayList<Constraint> c = (ArrayList<Constraint>) m.getConstraints().clone();
		m.backup(x);
		assertEquals(c, m.getConstraints());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBackupNoAlterationsVariables() {
		ArrayList<Variable> v = (ArrayList<Variable>) m.getVariables().clone();
		m.backup(x);
		assertEquals(v, m.getVariables());
	}

}

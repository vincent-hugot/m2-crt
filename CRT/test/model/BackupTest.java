package model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Operator.Constraint;

import org.junit.Test;

public class BackupTest {

	private Model m;
	private Variable x, y, z;
	
	public BackupTest(){
		model.Constraint c1;
		model.Constraint c2;
		m = new Model();
		
		x = m.newVariable("X", 0, 5);
		y = m.newVariable("Y", 5, 1);
		z = m.newVariable("Z", 0, 3);
		
		m.getVariables().add(x);
		m.getVariables().add(y);
		m.getVariables().add(z);
		
		c1 = m.newConstraint(x, y, Constraint.LOWER);
		c2 = m.newConstraint(y, z, Constraint.GREATER);
		
		m.getConstraints().add(c1);
		m.getConstraints().add(c2);
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
	public void testBackupNoAlterartionsConstraints() {
		ArrayList<model.Constraint> c = (ArrayList<model.Constraint>) m.getConstraints().clone();
		m.backup(x);
		assertEquals(c, m.getConstraints());
	}
	
	@Test
	public void testBackupNoAlterationsVariables() {
		ArrayList<Variable> v = (ArrayList<Variable>) m.getVariables().clone();
		m.backup(x);
		assertEquals(v, m.getVariables());
	}

}

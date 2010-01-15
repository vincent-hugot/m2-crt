package model;

import static org.junit.Assert.*;

import java.util.HashMap;

import model.Operator.Constraint;

import org.junit.Before;
import org.junit.Test;

public class RestoreTest {

	private Model m;
	private Variable x, y, z;
	
	
	@Before
	public void setUp() {
		
		m = new Model();
		
		x = m.newVariable("X", 0, 5);
		y = m.newVariable("Y", 0, 5);
		z = m.newVariable("Z", 0, 5);
		
		m.newConstraint(x, y, Constraint.LOWER);
		m.newConstraint(y, z, Constraint.GREATER);
	}
	
	@Test
	public void testGeneralRestore() {
		
		HashMap<Integer,Variable> modelBackup = m.backupVariables(0);
		
		Domain dX = (Domain) x.getDomain().clone();
		Domain dY = (Domain) y.getDomain().clone();
		Domain dZ = (Domain) z.getDomain().clone();
		
		
		// Just to check correct cloning
		assertTrue(dX.equals(x.getDomain()));
		assertTrue(dY.equals(y.getDomain()));
		assertTrue(dZ.equals(z.getDomain()));
		assertNotSame(dX,x.getDomain());
		assertNotSame(dY,y.getDomain());
		assertNotSame(dZ,z.getDomain());
		
		x.getDomain().remove(1);
		y.getDomain().remove(1);
		z.getDomain().remove(1);
		
		// Just to check we changed something
		assertFalse(dX.equals(x.getDomain()));
		assertFalse(dY.equals(y.getDomain()));
		assertFalse(dZ.equals(z.getDomain()));
		
		m.restoreHashMap(modelBackup);
		
		// Actual test : restoring everything
		assertTrue(dX.equals(x.getDomain()));
		assertTrue(dY.equals(y.getDomain()));
		assertTrue(dZ.equals(z.getDomain()));
	}
	
	
	
	/*
	 * Simple regression test (3 backup in a row, restoring the first, then the second)
	 */
	@Test
	public void testMultipleRestores() {
		
		HashMap<Integer,Variable> modelBackup1 = m.backupVariables(0);
		HashMap<Integer,Variable> modelBackup2 = m.backupVariables(0);
		HashMap<Integer,Variable> modelBackup3 = m.backupVariables(0);

		Domain dX = (Domain) x.getDomain().clone();
		
		x.getDomain().remove(1);
		x.getDomain().remove(2);
		x.getDomain().remove(3);
		
		assertEquals(x.getDomain().size(), 3); // 3 values removed
		
		
		m.restoreHashMap(modelBackup2);
		assertEquals(dX, x.getDomain());
		assertEquals(x.getDomain().size(), 6);
		
		m.restoreHashMap(modelBackup1);
		assertEquals(dX, x.getDomain());
		assertEquals(x.getDomain().size(), 6);
		
		m.restoreHashMap(modelBackup3);
		assertEquals(dX, x.getDomain());
		assertEquals(x.getDomain().size(), 6);
		
		m.restoreHashMap(modelBackup1);
		assertEquals(dX, x.getDomain());
		assertEquals(x.getDomain().size(), 6);
		
		m.restoreHashMap(modelBackup2);
		assertEquals(dX, x.getDomain());
		assertEquals(x.getDomain().size(), 6);
	}
	
	
	
	
	/*
	 * Testing methods for Offseted restoration :
	 * Restoring at offset n, 0..n variables are not backuped.
	 * 
	 * Since the variable order is not known and implementation-dependant,
	 * we only check the number of correctly/incorrectly changed variables.
	 * 
	 * Changes are checked by a very local sample :
	 * each variable has 6 values ([0..5]), and we remove 1 value for testing.
	 */
	
	@Test
	public void testRestoreOffset0() {
		int offset = 0;
		
		HashMap<Integer,Variable> modelBackup = m.backupVariables(offset);
		
		x.getDomain().remove(1);
		y.getDomain().remove(1);
		z.getDomain().remove(1);
		
		m.restoreHashMap(modelBackup);
		
		int nonRestoredVars = 0;
		for (Variable var : m.getVariables()) {
			if (var.getDomain().size() != 6) nonRestoredVars++;
		}
		
		assertEquals(nonRestoredVars, offset); // Everything restored
	}
	
	@Test
	public void testRestoreOffset1() {
		int offset = 1;
		
		HashMap<Integer,Variable> modelBackup = m.backupVariables(offset);
		
		x.getDomain().remove(1);
		y.getDomain().remove(1);
		z.getDomain().remove(1);
		
		m.restoreHashMap(modelBackup);
		
		int nonRestoredVars = 0;
		for (Variable var : m.getVariables()) {
			if (var.getDomain().size() != 6) nonRestoredVars++;
		}
		
		assertEquals(nonRestoredVars, offset); // 2 restored (1 non-restored)
	}
	
	
	@Test
	public void testRestoreOffset2() {
		int offset = 2;
		
		HashMap<Integer,Variable> modelBackup = m.backupVariables(offset);
		
		x.getDomain().remove(1);
		y.getDomain().remove(1);
		z.getDomain().remove(1);
		
		m.restoreHashMap(modelBackup);
		
		int nonRestoredVars = 0;
		for (Variable var : m.getVariables()) {
			if (var.getDomain().size() != 6) nonRestoredVars++;
		}
		
		assertEquals(nonRestoredVars, offset); // Only 1 restored (2 non-restored)
	}
	
	
	
	@Test
	public void testRestoreOffset3() {
		int offset = 3;
		
		HashMap<Integer,Variable> modelBackup = m.backupVariables(offset);
		
		x.getDomain().remove(1);
		y.getDomain().remove(1);
		z.getDomain().remove(1);

		m.restoreHashMap(modelBackup);
		
		int nonRestoredVars = 0;
		for (Variable var : m.getVariables()) {
			System.out.print("hm ");
			if (var.getDomain().size() != 6) nonRestoredVars++;
		}
		
		System.out.println("\n" + nonRestoredVars + "/" + offset);
		assertEquals(nonRestoredVars, offset); // Nothing restored
	}
}

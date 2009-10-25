package model;

import static org.junit.Assert.*;
import org.junit.Test;

public class DomainTest {
	
	
	@Test
	public void testMinMax() {
		Domain d = new Domain(1,3);
		
		assertTrue(d.contains(1));
		assertTrue(d.contains(2));
		assertTrue(d.contains(3));
		
		assertEquals(d.least(),1);
		assertEquals(d.greatest(),3);
	}
	
	
	@Test
	public void testUnion() {
		Domain d1 = new Domain(1,3);
		Domain d2 = new Domain(2,4);
		
		d1.extend(d2);
		
		assertEquals(d1, new Domain(1,4));
	}
	
	
	@Test
	public void testIntersection() {
		Domain d1 = new Domain(1,3);
		Domain d2 = new Domain(2,4);
		
		d1.restrict(d2);
		
		assertEquals(d1, new Domain(2,3));
	}
	
	
	@Test
	public void testRemove() {
		Domain d = new Domain(1,3);
		d.remove(2);
		
		assertFalse(d.contains(2));
	}
	
	
	@Test
	public void testArithmeticOperation() {
		Domain d1 = new Domain(1,3);
		Domain d2 = new Domain(1,3);
		
		Domain dAdd = new Domain(2,6);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.ADD, d2), dAdd);
		
		Domain dSub = new Domain(-2,2);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.SUB, d2), dSub);
		
		Domain dMul = new Domain(1,9);
		dMul.remove(5); dMul.remove(7); dMul.remove(8);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.MUL, d2), dMul);
		
		Domain dDiv = new Domain(0,3);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.DIV, d2), dDiv);
		
		
		// After making some holes...
		d1.remove(2);
		d2.remove(2);
		
		dAdd = new Domain();
		dAdd.add(2); dAdd.add(4); dAdd.add(6);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.ADD, d2), dAdd);
		
		dSub = new Domain();
		dSub.add(0); dSub.add(-2); dSub.add(2);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.SUB, d2), dSub);
		
		dMul = new Domain();
		dMul.add(1); dMul.add(3); dMul.add(9);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.MUL, d2), dMul);
		
		dDiv = new Domain();
		dDiv.add(1); dDiv.add(0); dDiv.add(3);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.DIV, d2), dDiv);
		
		
		// With empty domains
		d1 = new Domain();
		d2 = new Domain();
		
			dAdd = new Domain();
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.ADD, d2), dAdd);
			dSub = new Domain();
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.SUB, d2), dSub);
			dMul = new Domain();
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.MUL, d2), dMul);
			dDiv = new Domain();
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.DIV, d2), dDiv);
		
		
		// Some divide by 0
		d1 = new Domain(1,3);
		d2 = new Domain(0,1);
		dDiv = new Domain(1,3);
		assertEquals(d1.arithmeticOperation(Operator.Arithmetic.DIV, d2), dDiv);
	}
}

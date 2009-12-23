/**
 * 
 */
package valuation;

import static org.junit.Assert.*;

import model.Domain;
import model.Model;
import model.Operator;
import model.Variable;

import org.junit.Test;

import translator.Translator;

import ac.AC3;

/**
 * @author Administrateur
 *
 */
public class FullLookAheadTest {
	Model m = new Model();
	
	public FullLookAheadTest(){
		
		Variable A = m.newVariable("A",0,1);
		Variable B = m.newVariable("B",1,2);
		Variable C = m.newVariable("C",1,2);
		Variable S1 = m.newVariable("S1",2,4);
		Variable S2 = m.newVariable("S2",2,5);
		m.newConstraint(A,B,Operator.Constraint.EQUAL);
		m.newConstraint(A,S2,Operator.Constraint.LOWER);
		m.newSubstitution(B,C,Operator.Arithmetic.ADD,S1);
		m.newSubstitution(A,S1,Operator.Arithmetic.ADD,S2);
		
		A.getDomain().remove(0);
		B.getDomain().remove(2);
		System.out.println(S1);System.out.println(S2);
		AC3 ac3 = new AC3(m);
		
		// Should be obvious, just for visual comparison
		assertEquals(S1.getDomain(), new Domain(2,4));
		assertEquals(S2.getDomain(), new Domain(2,5));
		
		ac3.updateSubstitutions();
		
		// See javadoc comment for new domain calculus
		// - Step 1: S1 and S2 are restricted
		// - Dependencies: S2 treated becore S1 would have given {3,4,5}
		assertEquals(S1.getDomain(), new Domain(2,3));
		assertEquals(S2.getDomain(), new Domain(3,4));
	}
	

	/**
	 * Test method for {@link valuation.FullLookAhead#FullLookAhead(model.Model)}.
	 */
	@Test
	public void testFullLookAhead() {
		FullLookAhead fla=new FullLookAhead(m);
		//System.out.println(m);
		//fla.run();
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#run()}.
	 */
	@Test
	public void testRun() {
		String content =
			"DOMAINS:\n" +
			"A : [0,...,5],\n" +
			"B : [1,...,2],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A = B,\n" +
			"A + B < A * 42 + C;";
		
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [1,...,200],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A = B;" ;
		
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [100,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = B, B = C;" ;
		
		Translator translator = new Translator("testfile",content);
		Model m = translator.translate();
		
		//AC3 ac3 = new AC3(m);
		//ac3.run();
		FullLookAhead f = new FullLookAhead(m);
		f.run();
	}

}

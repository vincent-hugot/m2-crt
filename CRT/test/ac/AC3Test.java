package ac;

import model.Domain;
import model.Model;
import model.Operator;
import model.Variable;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Unit tests on AC3.
 * 
 * Substitution tests are delicate: 2 tests for 2 part of 1 function.
 * A sample test is done for step 1 only, then one for step 2/3 considering step 1 is OK.
 * 
 * @author Mathias COQBLIN
 */
public class AC3Test {
	
	
	/**
	 * First updateSubstitions test method.
	 * Will test:
	 * - Step 1 (equal updated from left[op]right)
	 * - Correct dependencies (Sx treated in correct order)
	 * 
	 * 
	 * Step one: Z=A+B  =>  D(Z) restricted by D(A+B)
	 * 
	 * Retracing:
	 * A = B, A = {0,1}, B = {1,2}
	 * A < A+B+C, C = {1,2}
	 * => S1=B+C, S2=A+S1,  S1 = {2,3,4}, S2 = {2,3,4,5}
	 * => A = B, A < S2
	 * 
	 * Admiting that A and B are restricted to {1} each. (not possible since
	 * update is launched at A change, and B is only restricted in first update, but meh.)
	 * Here, S1 should be updated to D(S1) /\ new D(B+C) = {2,3},
	 * then S2 to D(S2) /\ new D(A+S1) = {3,4}
	 * 
	 * IF dependencies were not correct (S2 treated before S1), we would have S2={3,4,5}
	 * 
	 * (We do not study potential A/B/C restriction on step 2-3, for that see next test)
	 */
	@Test
	public void testUpdateSubstitutionsStep1() {
		Model m = new Model();
		
		Variable A = m.newVariable("A",0,1);
		Variable B = m.newVariable("B",1,2);
		Variable C = m.newVariable("C",1,2);
		Variable S1 = m.newVariable("S1",2,4);
		Variable S2 = m.newVariable("S2",2,5);
		m.newConstraint(A,B,Operator.Constraint.EQUAL);
		m.newConstraint(A,S1,Operator.Constraint.LOWER);
		m.newSubstitution(B,C,Operator.Arithmetic.ADD,S1);
		m.newSubstitution(A,S1,Operator.Arithmetic.ADD,S2);
		
		A.getDomain().remove(0);
		B.getDomain().remove(2);
		
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
	 * Second updateSubstitions test method.
	 * Will test:
	 * - Steps 2/3 (left and right updated)
	 * Will also re-test Step one (a whole substitution is tested)
	 * 
	 * 
	 * 
	 * 
	 * Retracing:
	 * A > B+C,  A = {2}, B = {0,1}, C = {1,2}
	 * => S1=B+C,  S1 = {1,2,3}
	 * => A > S1
	 * 
	 * Admiting that S1 is restricted to {1} (2/3 eliminated at revise(S1,A)
	 * 
	 * S1 shouldn't change in updateSubstitutions.
	 * B should be reduced to {0} (only value for {1}=B+{1,2}),
	 * then C to {1} (only value for {1}={0}+C)
	 * 
	 * Note that this very problem will be reused in main AC3 test, as it is solved.
	 */
	@Test
	public void testUpdateSubstitutionsStep23() {
		Model m = new Model();
		
		Variable A = m.newVariable("A",2,2);
		Variable B = m.newVariable("B",0,1);
		Variable C = m.newVariable("C",1,2);
		Variable S1 = m.newVariable("S1",1,3);
		m.newConstraint(A,S1,Operator.Constraint.GREATER);
		m.newSubstitution(B,C,Operator.Arithmetic.ADD,S1);
		
		S1.getDomain().remove(2);
		S1.getDomain().remove(3);
		
		AC3 ac3 = new AC3(m);
		
		// Should be obvious, just for visual comparison
		assertEquals(B.getDomain(), new Domain(0,1));
		assertEquals(C.getDomain(), new Domain(1,2));
		
		ac3.updateSubstitutions();
		
		// See javadoc comment for new domain calculus
		// - Step 2: B reduced to {0}
		// - Step 3: C reduced to {1}
		//assertEquals(B.getDomain(), new Domain(0,0));
		//assertEquals(C.getDomain(), new Domain(1,1));
		// TODO : ACTIVATE WHEN AC3 DONE.
	}
}

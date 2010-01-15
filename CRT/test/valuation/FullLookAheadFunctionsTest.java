/**
 * 
 */
package valuation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import model.Model;
import model.Variable;

import org.junit.Test;

import translator.Translator;

/**
 * @author Administrateur
 *
 */
public class FullLookAheadFunctionsTest {

	/**
	 * Test method for {@link valuation.FullLookAhead#FullLookAhead(model.Model)}.
	 */
	Model m;
	Translator translator;
	FullLookAhead f;
	String content;

	@Test
	public final void testFullLookAhead() {
		content="DOMAINS:"+
		"X1 : [1,..., 4],"+
		"X2 : [1,..., 4],"+
		"	X3 : [1,..., 4],"+
		"	X4 : [1,..., 4];"+
		"	CONSTRAINTS:"+
		"	X1 /= X2 + 1,"+
		"	X1 /= X3 + 2,"+
		"	X1 /= X4 + 3,"+
		"	X2 /= X3 + 1,"+
		"	X2 /= X4 + 2,"+
		"	X3 /= X4 + 1,"+
		"	X2 /= X1 + 1,"+
		"	X3 /= X1 + 2,"+
		"X4 /= X1 + 3,"+
		"X2 /= X3 - 1,"+
		"X2 /= X4 - 2,"+
		"X3 /= X4 - 1,"+
		"X1 /= X2,"+
		"X1 /= X3,"+
		"X1 /= X4,"+
		"X2 /= X3,"+
		"X2 /= X4,"+
		"X3 /= X4;";

		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);

		HashMap<Integer, Variable> hmap = m.backupVariables(0);


		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A", 1, 1));
		assertRes.add(new Variable("B", 1, 1));
		assertRes.add(new Variable("C", 5, 5));
		f.run();
		for (int i=0;i<hmap.values().toArray().length;i++){
			System.out.println(m.getVariables().toArray()[i]+" "+hmap.values().toArray()[i]);
		}

		for (int i=0;i<m.getVariables().size();i++){
			assertEquals(hmap.get(i),m.getVariables().get(i));
			System.out.println(hmap.get(i) +" "+m.getVariables().get(i));
		}
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#run()}.
	 */
	@Test
	public final void testRun() {
		content="DOMAINS:"+
		"X1 : [1,..., 4],"+
		"X2 : [1,..., 4],"+
		"	X3 : [1,..., 4],"+
		"	X4 : [1,..., 4];"+
		"	CONSTRAINTS:"+
		"	X1 /= X2 + 1,"+
		"	X1 /= X3 + 2,"+
		"	X1 /= X4 + 3,"+
		"	X2 /= X3 + 1,"+
		"	X2 /= X4 + 2,"+
		"	X3 /= X4 + 1,"+
		"	X2 /= X1 + 1,"+
		"	X3 /= X1 + 2,"+
		"X4 /= X1 + 3,"+
		"X2 /= X3 - 1,"+
		"X2 /= X4 - 2,"+
		"X3 /= X4 - 1,"+
		"X1 /= X2,"+
		"X1 /= X3,"+
		"X1 /= X4,"+
		"X2 /= X3,"+
		"X2 /= X4,"+
		"X3 /= X4;";

		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		for(int i=0; i<m.getVariables().size();i++){
			if (i<= m.getVariables().size()-3){
				assertNull(f.selectValueFlaLess3Vars(i));
				assertNotNull(f.selectValueFla(i));
			}	
			else{
				assertNotNull(f.selectValueFlaLess3Vars(i));
				assertNotNull(f.selectValueFla(i));
			}
		}
		
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#selectValueFla(int)}.
	 */
	@Test
	public final void testRun2() {
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		/*
		 * "X1 : [-10,...,10],"+
		"X2 : [-10,...,10],"+
		"X3 : [-10...10],"+
		"X4 : [-10,...,10];"+
		*/
		"X1 : [0,...,1],"+
		"X2 : [0,...,1],"+
		"X3 : [0...1],"+
		"X4 : [0,...,1];"+

		"CONSTRAINTS: "+
		"6 * X1 + 3 * X2 + 5 * X3 + 2 * X4 <= 10,"+
		"X3 + X4 <= 1,"+
		"X3 - X1 <= 0,"+
		"X4 - X2 <= 0;"
		;
		
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		
		//System.out.println(f.consistent(0, 0, 20, 0, 21));
		HashMap<Integer, Variable> ivar = new HashMap<Integer, Variable>();
		f.AddbackupSubstitutionsToHash(ivar, m.getVariables().get(21));
		System.out.println(ivar);
		f.AddbackupSubstitutionsToHash(ivar, m.getVariables().get(4));
		System.out.println(ivar);//doublon constante 0
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#selectValueFlaLess3Vars(int)}.
	 */
	@Test
	public final void testSelectValueFlaLess3Vars() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#consistent(int, int, int, int, int)}.
	 */
	@Test
	public final void testConsistent() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#futureEmpty()}.
	 */
	@Test
	public final void testFutureEmpty() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#AddbackupSubstitutionsToHash(java.util.HashMap, model.Variable)}.
	 */
	@Test
	public final void testAddbackupSubstitutionsToHash() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#initQueue(model.Variable, model.Variable, model.Variable)}.
	 */
	@Test
	public final void testInitQueue() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#findSubstitutionVals(model.Substitution, int, model.Variable)}.
	 */
	@Test
	public final void testFindSubstitutionVals() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#updateSubstitutions(int)}.
	 */
	@Test
	public final void testUpdateSubstitutions() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link valuation.FullLookAhead#updateSubstitutionsWithoutModifLessEquals(int)}.
	 */
	@Test
	public final void testUpdateSubstitutionsWithoutModifLessEquals() {
		//fail("Not yet implemented");
	}

}

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
		
		/*
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [1,...,200],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A < B;" ;
		*/
		
		/*
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [100,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = B, B = C;" ;
		*/
		
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [0,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = 2, B=3, C=5, D = D+1; " ;
		
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [0,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = 2, B=3, C=5, D = 1; " ;
		
		
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [0,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
//			"A = 2, B=3, C=5;" 
			"A < B, B < C;";
			;
		
		
			
			content =
				"DOMAINS:\n" +
				"A : [1,...,3],\n" +
				"B : [1,...,3],\n" +
				"C : [1,...,3];\n" +
				"CONSTRAINTS:\n" +
				"A < B, B < C;";
				;
				
				
				
				content =
					"DOMAINS:\n" +
					"A : [1,...,3],\n" +
					"B : [1,...,3],\n" +
					"C : [1,...,3];\n" +
					"CONSTRAINTS:\n" +
					"A > C, B < C;";
					;
					
		
		content =
			"DOMAINS:\n" +
			"A : [1,...,30],\n" +
			"B : [30,...,40],\n" +
			"C : [19,...,88];\n" +
			"CONSTRAINTS:\n" +
			"A >= B, B  = C;" ;
		
		content =
			"DOMAINS:\n" +
			/*
			"A : [28,...,30],\n" +
			"B : [28,...,32],\n" +
			"C : [29,...,32],\n" +
			"D : [29,...,32],\n" +
			"E : [29,...,32],\n" +
			"F : [29,...,32],\n" +
			"G : [29,...,32],\n" +
			"H : [29,...,32],\n" +
			"I : [29,...,32],\n" +
			"J : [29,...,32];\n" +
			*/
			
			"A : [1,...,10],\n" +
			"B : [1,...,10],\n" +
			"C : [1,...,10],\n" +
			"D : [1,...,10],\n" +
			"E : [1,...,10],\n" +
			"F : [1,...,10],\n" +
			"G : [1,...,10],\n" +
			"H : [1,...,10],\n" +
			"I : [1,...,10],\n" +
			"J : [1,...,10];\n" +
			
			"CONSTRAINTS:\n" +
			"A > B, B  > C, C > D, D > E, E > F, F > G, G > H, I > J;" ;
			
		
		content =
			"DOMAINS:\n" +
			"A : [28,...,30],\n"+
			"B : [28,...,30],\n"+
			"C : [28,...,30],\n"+
			"D : [28,...,30];\n"+
			"CONSTRAINTS:\n" +
			"A <> A;" ;//ok
		
		content =
			"DOMAINS:\n" +
			"A : [28,...,30];\n"+
			"CONSTRAINTS:\n" +
			"A <> A;" ;//pas ok
		
		content =
			"DOMAINS:\n" +
			"A : [28,...,30],\n"+
			"B : [28,...,30],\n"+
			"C : [28,...,30];\n"+
			"CONSTRAINTS:\n" +
			"B <> C;" ;//pas ok
		
		content =
			"DOMAINS:\n" +
			"A : [2,...,2],\n"+
			"B : [1,...,2],\n"+
			"C : [1,...,2],\n"+
			"D : [1,...,2];\n"+
			"CONSTRAINTS:\n" +
			"A <> B, A <> C, A <> D, B<>C, B<> D, C<> D;" ;//pas ok
		
		Translator translator = new Translator("testfile",content);
		Model m = translator.translate();
		
		//AC3 ac3 = new AC3(m);
		//ac3.run();
		System.out.println("m: "+m);
		FullLookAhead f = new FullLookAhead(m);
		System.out.println(f.run());
	}
	
	/**
	 * Test method for {@link valuation.FullLookAhead#run()}.
	 */
	@Test
	public void testCours() {
		/*
		String content =
			"DOMAINS:\n" +
			"X : [1,...,5],\n" +
			"W : [1,...,5],\n" +
			"Z : [1,...,5],\n" +
			"Y : [1,...,5];\n" +
			"CONSTRAINTS:\n" +
			"X > W, X > Y, W > Z, Y <> Z ;" ;
		
		Translator translator = new Translator("testfile",content);
		Model m = translator.translate();
		
		AC3 ac3 = new AC3(m);
		ac3.run();
		
		//System.out.println(m.getConstraintConcerningVariables(m.getVariables().get(0), m.getVariables().get(1)));
		
		//System.out.println(m);
		FullLookAhead f = new FullLookAhead(m);
		//f.run();
		//assertTrue(f.consistent(0, 3, 1, 2, 2));
		//assertTrue(f.consistent(0, 3, 1, 3, 2));
		 * 
		 */
	}

}

package valuation;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;

import model.Domain;
import model.Model;
import model.Variable;
import model.Operator;


import org.junit.Test;

import ac.AC3;

import translator.Translator;

public class FullLookAheadTest {
	Model m;
	Translator translator;
	FullLookAhead f;
	String content;

	public FullLookAheadTest(){

	}
	/**
	 * Test method for {@link valuation.FullLookAhead#run()}.
	 */
	@Test
	public void test1Subst() {
		content =
			"DOMAINS:\n" +
			"A : [0,...,5],\n" +
			"B : [1,...,2],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A = B,\n" +
			"A + B < A * 42 + C;";

		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);

		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A", 1, 1));
		assertRes.add(new Variable("B", 1, 1));
		assertRes.add(new Variable("C", 5, 5));
		

		assertEquals(f.run(), assertRes);
	}


	@Test
	public void test2last2varDifferent() {
		content =
			"DOMAINS:\n" +
			"A : [28,...,30],\n"+
			"B : [28,...,30],\n"+
			"C : [28,...,30];\n"+
			"CONSTRAINTS:\n" +
			"B <> C;" ;//pas ok
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A", 28, 28));
		assertRes.add(new Variable("B", 28, 28));
		assertRes.add(new Variable("C", 29, 29));

		assertEquals(f.run(), assertRes);
	}

	@Test
	public void test1varUnary() {
		content =
			"DOMAINS:\n" +
			"A : [28,...,30];\n"+
			"CONSTRAINTS:\n" +
			"A=A;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",28,28));

		assertEquals(assertRes, f.run());
	}

	@Test
	public void test1varUnaryFalse() {
		content =
			"DOMAINS:\n" +
			"A : [28,...,30];\n"+
			"CONSTRAINTS:\n" +
			"A<>A;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);

		assertEquals(null, f.run());
	}


	@Test
	public void testMore3varsUnaryFalse() {
		content =
			"DOMAINS:\n" +
			"A : [28,...,30],\n"+
			"B : [28,...,30],\n"+
			"C : [28,...,30],\n"+
			"D : [28,...,30];\n"+
			"CONSTRAINTS:\n" +
			"A <> A;" ;//ok
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);

		assertEquals(null, f.run());
	}

	@Test
	public void inconsistent() {
		content =
			"DOMAINS:\n" +
			"A : [2,...,2],\n"+
			"B : [1,...,2],\n"+
			"C : [1,...,2],\n"+
			"D : [1,...,2];\n"+
			"CONSTRAINTS:\n" +
			"A <> B, A <> C, A <> D, B<>C, B<> D, C<> D;" ;//pas ok
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);

		assertEquals(null, f.run());
	}

	@Test
	public void almostDecrescendo() {
		content =
			"DOMAINS:\n" +
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
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",8,8));
		assertRes.add(new Variable("B",7,7));
		assertRes.add(new Variable("C",6,6));
		assertRes.add(new Variable("D",5,5));
		assertRes.add(new Variable("E",4,4));
		assertRes.add(new Variable("F",3,3));
		assertRes.add(new Variable("G",2,2));
		assertRes.add(new Variable("H",1,1));
		assertRes.add(new Variable("I",2,2));
		assertRes.add(new Variable("J",1,1));

		assertEquals(assertRes, f.run());
	}



	@Test
	public void test1Superior1Equal() {
		content =
			"DOMAINS:\n" +
			"A : [1,...,30],\n" +
			"B : [30,...,40],\n" +
			"C : [19,...,88];\n" +
			"CONSTRAINTS:\n" +
			"A >= B, B  = C;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",30,30));
		assertRes.add(new Variable("B",30,30));
		assertRes.add(new Variable("C",30,30));

		assertEquals(assertRes, f.run());
	}

	@Test
	public void testAsupCsupB() {
		content =
			"DOMAINS:\n" +
			"A : [1,...,3],\n" +
			"B : [1,...,3],\n" +
			"C : [1,...,3];\n" +
			"CONSTRAINTS:\n" +
			"A > C, B < C;";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",3,3));
		assertRes.add(new Variable("B",1,1));
		assertRes.add(new Variable("C",2,2));

		assertEquals(assertRes, f.run());
	}

	@Test
	public void testAinfBinfC() {
		content =
			"DOMAINS:\n" +
			"A : [1,...,3],\n" +
			"B : [1,...,3],\n" +
			"C : [1,...,3];\n" +
			"CONSTRAINTS:\n" +
			"A < B, B < C;";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",1,1));
		assertRes.add(new Variable("B",2,2));
		assertRes.add(new Variable("C",3,3));

		assertEquals(assertRes, f.run());
	}

	@Test
	public void testWithoutRemovingValues() {
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [0,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A < B, B < C;";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",-77,-77));
		assertRes.add(new Variable("B",0,0));
		assertRes.add(new Variable("C",5,5));
		assertRes.add(new Variable("D",0,0));

		assertEquals(assertRes, f.run());
	}

	@Test
	public void testOperatorEqual() {
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [0,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = 2, B=3, C=5, D = 1; " ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",2,2));
		assertRes.add(new Variable("B",3,3));
		assertRes.add(new Variable("C",5,5));
		assertRes.add(new Variable("D",1,1));

		assertEquals(assertRes, f.run());
	}

	@Test
	public void testOperatorEqualAnd1SubstitutionFalse() {
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [0,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = 2, B=3, C=5, D = D+1; " ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);

		assertEquals(null, f.run());
	}

	@Test
	public void test2equalsFalse() {
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [100,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = B, B = C;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);

		assertEquals(null, f.run());
	}

	@Test
	public void withoutRemovingValues() {
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [1,...,200],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A < B;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",-77,-77));
		assertRes.add(new Variable("B",1,1));
		assertRes.add(new Variable("C",5,5));

		assertEquals(assertRes, f.run());
	}

	@Test
	public void NoModelChangeAfterRunningLookahead() { // test avec les domaines de variables réduits
		m = new Model();

		Variable A = m.newVariable("A",3,5);
		Variable B = m.newVariable("B",1,6);
		Variable C = m.newVariable("C",6,8);
		m.newConstraint(A,B,Operator.Constraint.LOWER);
		m.newConstraint(B,C,Operator.Constraint.LOWER);

		System.out.println("m: "+m);
		f = new FullLookAhead(m);

		f.run();
		System.out.println("domaine apres run A: "+ m.getVariables().get(0).getDomain());//domain de A aprés
		System.out.println("domaine B apres run : "+ m.getVariables().get(1).getDomain());//domain de B aprés
		System.out.println(" domaine C apres run : "+ m.getVariables().get(2).getDomain());//domain de C aprés
		assertEquals(m.getVariables().get(0).getDomain(), new Domain(3,5));
		assertEquals(m.getVariables().get(1).getDomain(), new Domain(1,6));
		assertEquals(m.getVariables().get(2).getDomain(), new Domain(6,8));
	}

	@Test
	public void NoModelChangeAfterRunningLookahead2() {// test avec les domaines de variables non réduits car inconssistance totale

		m = new Model();

		Variable X = m.newVariable("X",3,4);
		Variable Y = m.newVariable("Y",1,2);
		Variable Z = m.newVariable("Z",5,6);
		m.newConstraint(X,Y,Operator.Constraint.LOWER);
		m.newConstraint(Y,Z,Operator.Constraint.LOWER);
		System.out.println("m: "+m);
		f = new FullLookAhead(m);

		f.run();
		System.out.println("domaine X apres run : "+ m.getVariables().get(0).getDomain());//domain de A aprés
		System.out.println("domaine Y apres run : "+ m.getVariables().get(1).getDomain());//domain de B aprés
		System.out.println(" domaine Z apres run : "+ m.getVariables().get(2).getDomain());//domain de C aprés
		assertEquals(m.getVariables().get(0).getDomain(), new Domain(3,4));
		assertEquals(m.getVariables().get(1).getDomain(), new Domain(1,2));
		assertEquals(m.getVariables().get(2).getDomain(), new Domain(5,6));
	}
	
	@Test
	public void test1subst() {

		content =
			"DOMAINS:\n" +
			"A : [0,...,5],\n" +
			"B : [1,...,2],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A = B,\n" +
			"A + B < A + C;";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",1,1));
		assertRes.add(new Variable("B",1,1));
		assertRes.add(new Variable("C",5,5));

		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void test1superior() {

		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [1,...,200],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A > B;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",2,2));
		assertRes.add(new Variable("B",1,1));
		assertRes.add(new Variable("C",5,5));

		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void testDifferentTrue() {

		content =
			"DOMAINS:\n" +
			"A : [1,...,2],\n" +
			"B : [1,...,2],\n" +
			"C : [1,...,2],\n" +
			"D : [1,...,2];\n" +
			"CONSTRAINTS:\n" +
			"A <> B, B<>C, C<>D, D = B; " ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",1,1));
		assertRes.add(new Variable("B",2,2));
		assertRes.add(new Variable("C",1,1));
		assertRes.add(new Variable("D",2,2));

		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void testRedundant() {
		content =
			"DOMAINS:\n" +
			"A : [-77,...,500],\n" +
			"B : [0,...,200],\n" +
			"C : [5,...,10],\n" +
			"D : [0,...,1];\n" +
			"CONSTRAINTS:\n" +
			"A = 2, B=3, C=5,"+
			"A < B, B < C;";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",2,2));
		assertRes.add(new Variable("B",3,3));
		assertRes.add(new Variable("C",5,5));
		assertRes.add(new Variable("D",0,0));

		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void testCrescendo() {
		content =
			"DOMAINS:\n" +

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
			"A > B, B  > C, C > D, D > E, E > F, F > G, G > H, H > I,  I > J;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",10,10));
		assertRes.add(new Variable("B",9,9));
		assertRes.add(new Variable("C",8,8));
		assertRes.add(new Variable("D",7,7));
		assertRes.add(new Variable("E",6,6));
		assertRes.add(new Variable("F",5,5));
		assertRes.add(new Variable("G",4,4));
		assertRes.add(new Variable("H",3,3));
		assertRes.add(new Variable("I",2,2));
		assertRes.add(new Variable("J",1,1));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void aEqBplusC() {
		content =
			"DOMAINS:\n" +
			"A : [1,...,3],\n"+
			"B : [1,...,3],\n"+
			"C : [1,...,3];\n"+
			"CONSTRAINTS:\n" +
			"A = B+C;" ;
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("A",2,2));
		assertRes.add(new Variable("B",1,1));
		assertRes.add(new Variable("C",1,1));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void queen() {
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
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("X1",2,2));
		assertRes.add(new Variable("X2",4,4));
		assertRes.add(new Variable("X3",1,1));
		assertRes.add(new Variable("X4",3,3));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void sendMoreMoneyBetween2vars() {
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		"D : [6,...,8],"+
		"E : [4...5],"+
		"M : [0,...,1],"+
		"N : [5...6],"+
		"O : [0,...,1],"+
		"R : [8...9],"+
		"S : [8,...,9],"+
		"Y : [1,...,2];"+
		"CONSTRAINTS: D <> E,"+
		"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, (S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E = 10000 * M + 1000*O + N*100 + E*10 + Y; ";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("D",7,7));
		assertRes.add(new Variable("E",5,5));
		assertRes.add(new Variable("M",1,1));
		assertRes.add(new Variable("N",6,6));
		assertRes.add(new Variable("O",0,0));
		assertRes.add(new Variable("R",8,8));
		assertRes.add(new Variable("S",9,9));
		assertRes.add(new Variable("Y",2,2));
		assertEquals(assertRes, f.run());
	}
	
	/* too long
	@Test
	public void sendMoreMoney3premsFree() {
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		"D : [0,...,9],"+
		"E : [0...9],"+
		"M : [0,...,9],"+
		"N : [6...6],"+
		"O : [0,...,0],"+
		"R : [8...8],"+
		"S : [9,...,9],"+
		"Y : [2,...,2];"+
		"CONSTRAINTS: "+
		"D <> E,"+
		"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, "+
		"(S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E =M*10000 + O*1000+ N*100 +  E*10 + Y; ";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("D",7,7));
		assertRes.add(new Variable("E",5,5));
		assertRes.add(new Variable("M",1,1));
		assertRes.add(new Variable("N",6,6));
		assertRes.add(new Variable("O",0,0));
		assertRes.add(new Variable("R",8,8));
		assertRes.add(new Variable("S",9,9));
		assertRes.add(new Variable("Y",2,2));
		assertEquals(assertRes, f.run());
	}
	*/
	
	@Test
	public void equation() {
		
		content =
			"DOMAINS:\n" +
			"X : [1,...,3],\n"+
			"Y : [1,...,3],\n"+
			"Z : [1,...,3];\n"+
			"CONSTRAINTS:\n" +
			"X+Y < -4 + 3*Z ;" ;
		 
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("X",1,1));
		assertRes.add(new Variable("Y",1,1));
		assertRes.add(new Variable("Z",3,3));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void equation2() {
		
		content =
			"DOMAINS:\n" +
			"X : [1,...,3],\n"+
			"Y : [1,...,3],\n"+
			"Z : [1,...,3];\n"+
			"CONSTRAINTS:\n" +
			"X <> Y, X <> Z, Y <> Z ,Z = 2*X +3*Y - 5 ;";
		 
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("X",1,1));
		assertRes.add(new Variable("Y",2,2));
		assertRes.add(new Variable("Z",3,3));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void sendMoreMoneyFixed() {
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		"D : [7,...,7],"+
		"E : [5...5],"+
		"M : [1,...,1],"+
		"N : [6...6],"+
		"O : [0,...,0],"+
		"R : [8...8],"+
		"S : [9,...,9],"+
		"Y : [2,...,2];"+
		"CONSTRAINTS: "+
		"D <> E,"+
		"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, "+
		"(S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E =M*10000 + O*1000+ N*100 +  E*10 + Y; ";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("D",7,7));
		assertRes.add(new Variable("E",5,5));
		assertRes.add(new Variable("M",1,1));
		assertRes.add(new Variable("N",6,6));
		assertRes.add(new Variable("O",0,0));
		assertRes.add(new Variable("R",8,8));
		assertRes.add(new Variable("S",9,9));
		assertRes.add(new Variable("Y",2,2));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void sendMoreMoney2premsFree() {
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		"D : [5,...,9],"+
		"E : [4...9],"+
		"M : [1,...,1],"+
		"N : [6...6],"+
		"O : [0,...,0],"+
		"R : [8...8],"+
		"S : [9,...,9],"+
		"Y : [2,...,2];"+
		"CONSTRAINTS: "+
		"D <> E,"+
		"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, "+
		"(S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E =M*10000 + O*1000+ N*100 +  E*10 + Y; ";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("D",7,7));
		assertRes.add(new Variable("E",5,5));
		assertRes.add(new Variable("M",1,1));
		assertRes.add(new Variable("N",6,6));
		assertRes.add(new Variable("O",0,0));
		assertRes.add(new Variable("R",8,8));
		assertRes.add(new Variable("S",9,9));
		assertRes.add(new Variable("Y",2,2));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void sendMoreMoney2PremsVarFreeRestFixeds() {
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		"D : [5,...,9],"+
		"E : [4...9],"+
		"M : [1,...,1],"+
		"N : [6...6],"+
		"O : [0,...,0],"+
		"R : [8...8],"+
		"S : [9,...,9],"+
		"Y : [2,...,2];"+
		"CONSTRAINTS: "+
		"D <> E,"+
		"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, "+
		"(S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E =M*10000 + O*1000+ N*100 +  E*10 + Y; ";
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("D",7,7));
		assertRes.add(new Variable("E",5,5));
		assertRes.add(new Variable("M",1,1));
		assertRes.add(new Variable("N",6,6));
		assertRes.add(new Variable("O",0,0));
		assertRes.add(new Variable("R",8,8));
		assertRes.add(new Variable("S",9,9));
		assertRes.add(new Variable("Y",2,2));
		assertEquals(assertRes, f.run());
	}
	
	@Test
	public void sendMoreMoneyPremVarFalse() {
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		"D : [5,...,5],"+//7
		"E : [4...9],"+//5
		"M : [0,...,1],"+//1
		"N : [5...6],"+//6
		"O : [-1,...,0],"+//0
		"R : [7...8],"+//8
		"S : [8,...,9],"+//9
		"Y : [1,...,2];"+//2
		
		"CONSTRAINTS: "+
		"D <> E,"+
		"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, "+
		"(S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E =M*10000 + O*1000+ N*100 +  E*10 + Y; ";
		
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		
		assertEquals(null, f.run());
	}
	
	@Test
	public void testRunSendMoreBetween2vars30s() {
	
	String
	content = "DOMAINS:"+
	//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
	"D : [6,...,7],"+
	//"D : [6,...,7],"+
	"E : [4...5],"+
	//"M : [0,...,1],"+
	"M : [0,...,1],"+
	//"N : [5...6],"+
	"N : [5...6],"+
	"O : [-1,...,0],"+
	"R : [7...8],"+
	"S : [8,...,9],"+
	"Y : [0,...,2];"+
	"CONSTRAINTS: D <> E,"+
	"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, (S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E = 10000 * M + 1000*O + N*100 + E*10 + Y; ";
	translator = new Translator("testfile",content);
	m = translator.translate();
	f = new FullLookAhead(m);
	HashSet<Variable> assertRes = new HashSet<Variable>();

	assertRes.add(new Variable("D",7,7));
	assertRes.add(new Variable("E",5,5));
	assertRes.add(new Variable("M",1,1));
	assertRes.add(new Variable("N",6,6));
	assertRes.add(new Variable("O",0,0));
	assertRes.add(new Variable("R",8,8));
	assertRes.add(new Variable("S",9,9));
	assertRes.add(new Variable("Y",2,2));
	assertEquals(assertRes, f.run());
	}
	
	@Test
	public void ichio() {
	
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
	HashSet<Variable> assertRes = new HashSet<Variable>();

	assertRes.add(new Variable("X1",0,0));
	assertRes.add(new Variable("X2",0,0));
	assertRes.add(new Variable("X3",0,0));
	assertRes.add(new Variable("X4",0,0));
	assertEquals(assertRes, f.run());
	
	//X1= 0 @10 = 1 0 r
	//HashMap<Integer, Variable> ivar = new HashMap<Integer, Variable>();
	//f.AddbackupSubstitutionsToHash(ivar, m.getVariables().get(0));
	//System.out.println(ivar);
	
	}
	
	@Test
	public void ichio2() {
	
	content = "DOMAINS:"+
	//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
	/*
	 * "X1 : [-10,...,10],"+
	"X2 : [-10,...,10],"+
	"X3 : [-10...10],"+
	"X4 : [-10,...,10];"+
	*/
	"X1 : [0,...,1],"+
	"X2 : [1,...,1],"+
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
	HashSet<Variable> assertRes = new HashSet<Variable>();

	assertRes.add(new Variable("X1",0,0));
	assertRes.add(new Variable("X2",1,1));
	assertRes.add(new Variable("X3",0,0));
	assertRes.add(new Variable("X4",0,0));
	assertEquals(assertRes, f.run());
	
	//X1= 0 @10 = 1 0 r
	//HashMap<Integer, Variable> ivar = new HashMap<Integer, Variable>();
	//f.AddbackupSubstitutionsToHash(ivar, m.getVariables().get(0));
	//System.out.println(ivar);
	
	}
	
	/*
	@Test
	public void sendMoreComplete() {

		
		content = "DOMAINS:"+
		//O = 0, M = 1, Y = 2, E = 5, N = 6, D = 7, R = 8, and S = 9.
		"D : [0,...,9],"+
		"E : [0...9],"+
		"M : [0,...,9],"+
		"N : [0...9],"+
		"O : [0,...,9],"+
		"R : [0...9],"+
		"S : [0,...,9],"+
		"Y : [0,...,9];"+
		"CONSTRAINTS: "+
		"D <> E,"+
		"D<>M, D<>N, D<>O, D<>R, D<>S, D<>Y,E<>M, E<>N, E<>O, E<>R, E<>S, E<>Y,M<>N, M<>O, M<>R, M<>S, M<>Y,N<>O, N<>R, N<>S, N<>Y,O<>R, O<>S, O<>Y,R<>S, R<>Y,S<>Y, "+
		"(S+M)*1000 + (E+O)*100 + (N+R)*10 + D+E =M*10000 + O*1000+ N*100 +  E*10 + Y; ";
	
		translator = new Translator("testfile",content);
		m = translator.translate();
		f = new FullLookAhead(m);
		HashSet<Variable> assertRes = new HashSet<Variable>();

		assertRes.add(new Variable("X1",0,0));
		assertRes.add(new Variable("X2",1,1));
		assertRes.add(new Variable("X3",0,0));
		assertRes.add(new Variable("X4",0,0));
		assertEquals(assertRes, f.run());
	
	}
	*/
	
}


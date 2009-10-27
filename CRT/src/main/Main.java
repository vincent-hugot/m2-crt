package main;

import ac.AC3;
import model.Model;
import model.Variable;
import translator.Translator;


public class Main {

	public static void main(String[] args) throws Exception {
		
		//String file = "src/parser/test.txt";
		 //* A = B, A = {0,1}, B = {1,2}
		// * A < A+B+C, C = {1,2}
		/*String content = 
			"DOMAINS: " +
			"A : [0..1], B : [1..2], C : [1..2];" +
			"CONSTRAINTS: " +
			"A = B, A < B+C+A;";*/
		//A > B+C,  A = {2}, B = {0,1}, C = {1,2}
		String content = 
			"DOMAINS: " +
			"A : [2..2], B : [0..1], C : [1..2];" +
			"CONSTRAINTS: " +
			"A < B + C, A < A;"; // 2 < B + C, and B + C < 2 give the same resulsts.
		
		Translator tr = new Translator("testfile",content);
		Model m = tr.translate();
		tr.dumpErrors();
		
		for (Variable v : m.getVariables()) {
			System.out.println(v.getName() + " : " + v.getAssociatedConstraints()
					+ " : " + v.getAssociatedSubstitutions());
		}
		
		System.out.println("*** B e f o r e ***");
		System.out.println(m);
		
		AC3 ac3 = new AC3(m);
		ac3.ac3();
		
		System.out.println("\n*** A f t e r ***");
		System.out.println(m);
	}

}

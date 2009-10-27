package translator;

import model.Constant;
import model.Domain;
import model.Model;
import model.Operator;
import model.Variable;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Translator tests class.
 * 
 * After testing correct Model constructions (with nested substitutions),
 * a whole part is dedicated to parsing error cases.
 * 
 * Note: every error test requires the same boring part :
 * Creating a String file content, parse it, test, then remove it
 * 
 * @author Mathias COQBLIN
 */
public class TranslatorTest {
	
	@Test
	public void testTranslation() {
		
		// Test "file"
		String content =
			"DOMAINS:\n" +
			"A : [0,...,5],\n" +
			"B : [1,...,2],\n" +
			"C : [5,...,10];\n" +
			"CONSTRAINTS:\n" +
			"A = B,\n" +
			"A + B < A * 42 + C;";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		Model m = new Model();
		
		/* Following the same translator creation order... */
		
		// Domain declarations
		Variable A = m.newVariable("A",0,5);
		Variable B = m.newVariable("B",1,2);
		Variable C = m.newVariable("C",5,10);
		
		// 1st constraint
		m.newConstraint(A,B, Operator.Constraint.EQUAL);
		
		// 2nd constraint (:fear:)
		
		// Substitution @S1 = A+B
		Domain dS1 = A.getDomain().arithmeticOperation(
			Operator.Arithmetic.ADD, B.getDomain()); // {1,2,3,4,5,6,7}
		Variable S1 = m.newVariable(TranslationVisitor.SUBSTITUTE + "1", dS1, true);
		m.newSubstitution(A, B, Operator.Arithmetic.ADD, S1);

		// Substitution @S2 = A*42 (A*42 + C is waiting for this one)
		Constant c42 = m.newConstant(42);
		Domain dS2 = A.getDomain().arithmeticOperation(
				Operator.Arithmetic.MUL, c42.getDomain()); // {0,42,84,126,168,210}
		Variable S2 = m.newVariable(TranslationVisitor.SUBSTITUTE + "2", dS2, true);
		m.newSubstitution(A, c42, Operator.Arithmetic.MUL, S2);
		
		// Substitution @S3 = @S2+C
		Domain dS3 = S2.getDomain().arithmeticOperation(
			Operator.Arithmetic.ADD, C.getDomain()); // All of Aj+5...Aj+10, Aj in D(S2)
		Variable S3 = m.newVariable(TranslationVisitor.SUBSTITUTE + "3", dS3, true);
		m.newSubstitution(S2, C, Operator.Arithmetic.ADD, S3);
		
		// At last
		m.newConstraint(S1, S3, Operator.Constraint.LOWER);
		
		System.out.println(m);
		// Are there any error ?
		assertFalse(translator.fail());
		assertEquals(translator.getModel(), m);
	}
	
	
	
	
	
	/** Testing error cases */
	
	
	@Test
	public void testErrorLexerInvalidCharacter() {
		
		// Test "file"
		String content = "DOMAINS: X : [0,...,5]; CONSTRAINTS: X ? 2;";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserDomainAfterConstraint() {
		
		// Test "file"
		String content = "CONSTRAINTS: 2 < 3; DOMAINS: X : [0,...,5];";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserInvalidOperator() {
		
		// Test "file"
		String content = "DOMAINS: X : [0,...,5]; CONSTRAINTS: X + 3;";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserNoSeparator() {
		
		// Test "file"
		String content = "DOMAINS: X : [0,...,5]; CONSTRAINTS: X < 3 X > 5;";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserNoSemicolon() {
		
		// Test "file"
		String content = "DOMAINS: X : [0,...,5] CONSTRAINTS: X < 3";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserBadDomainSyntax() {
		
		// Test "file"
		String content = "DOMAINS: X : [0,..,5]; CONSTRAINTS: X < 3;";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorDoubleDeclaration() {
		
		// Test "file"
		String content = "DOMAINS: X : [0,...,5], X : [0,...,5], X : [0,...,5];";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorNotDeclared() {
		
		// Test "file"
		String content = "CONSTRAINTS: X < 3;";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !translator.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	
	@Test
	public void testNoDomainOK() {
		
		// Test "file"
		String content = "CONSTRAINTS: 1 < 3;";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertFalse(translator.fail()); // same as: !translator.getErrors().isEmpty()
		
		
		// Free: checking this useless model
		Model m = new Model();
		Constant c1 = m.newConstant(1);
		Constant c2 = m.newConstant(3);
		m.newConstraint(c1,c2,Operator.Constraint.LOWER);
		assertEquals(translator.getModel(), m);
	}
	
	
	@Test
	public void testNoConstraintOK() {
		
		// Test "file"
		String content = "DOMAINS: X : [0,...,5];";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertFalse(translator.fail());
		assertNotNull(translator.getModel());
	}
}

package translator;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Parsing error cases test class.
 * 
 * Note: every error test requires the same boring part :
 * Creating a String file content, parse it, test, then remove it
 * 
 * @author Mathias COQBLIN
 */
public class TranslatorErrorsTest {
	
	
	
	/** Testing error cases */
	
	
	@Test
	public void testErrorLexerInvalidCharacter() {
		
		// Test "file"
		String content = "X *: [0,...,5]; X $= 2";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue("Test - Parsing error - Invalid character",
				translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull("Test - Parsing error - Invalide character (no model)",
				translator.getModel());
	}
	
	
	@Test
	public void testErrorParserDomainAfterConstraint() {
		
		// Test "file"
		String content = "2 *< 3; X *: [0,...,5];";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue("Test - Parsing error - Domain after constraints",
				translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull("Test - Parsing error - Domain after constraints (no model)",
				translator.getModel());
	}
	
	
	@Test
	public void testErrorParserInvalidOperator() {
		
		// Test "file"
		String content = "X *: [0,...,5]; X *+ 3";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue("Test - Parsing error - Invalid operator",
				translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull("Test - Parsing error - Invalid operator (no model)",
				translator.getModel());
	}
	
	
	@Test
	public void testErrorParserNoColon() {
		
		// Test "file"
		String content = "X *: [0,...,5]; X *< 3 X *> 5";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue("Test - Parsing error - No colon",
				translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull("Test - Parsing error - No colon (no model)",
				translator.getModel());
	}
	
	
	@Test
	public void testErrorParserNoSemicolon() {
		
		// Test "file"
		String content = "X *: [0,...,5] X *< 3";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue("Test - Parsing error - No semicolon",
				translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull("Test - Parsing error - No semicolon (no model)",
				translator.getModel());
	}
	
	
	@Test
	public void testErrorParserBadDomainSyntax() {
		
		// Test "file"
		String content = "X *: [0,..,5]; X *< 3";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue("Test - Parsing error - Bad domain syntax",
				translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull("Test - Parsing error - Bad domain syntax (no model)",
				translator.getModel());
	}
}

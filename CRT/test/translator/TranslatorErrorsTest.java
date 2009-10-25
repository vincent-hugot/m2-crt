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
		assertTrue(translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserDomainAfterConstraint() {
		
		// Test "file"
		String content = "2 *< 3; X *: [0,...,5];";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserInvalidOperator() {
		
		// Test "file"
		String content = "X *: [0,...,5]; X *+ 3";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserNoColon() {
		
		// Test "file"
		String content = "X *: [0,...,5]; X *< 3 X *> 5";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserNoSemicolon() {
		
		// Test "file"
		String content = "X *: [0,...,5] X *< 3";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
	
	
	@Test
	public void testErrorParserBadDomainSyntax() {
		
		// Test "file"
		String content = "X *: [0,..,5]; X *< 3";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue(translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull(translator.getModel());
	}
}

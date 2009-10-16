package translator;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Note: every error test requires the same boring part :
 * Creating a temp file, parse it, test, then remove it
 * Common methods are here for creation, tests, and deletion
 * 
 * @author Mathias COQBLIN
 */
public class TranslatorTest {
	
	
	
	/** Testing error cases */
	
	
	@Test
	public void testErrorInvalidCharacter() {
		
		// Test "file"
		String content = "X *: [-100,...,100]; X $= 2";
		
		Translator translator = new Translator("testfile",content);
		translator.translate();
		
		// Are there any error ?
		assertTrue("Test - Parsing error - Invalid character",
				translator.fail()); // same as: !spy.getErrors().isEmpty()
		assertNull("Test - Parsing error - Invalide character (no model)",
				translator.getModel());
	}
}

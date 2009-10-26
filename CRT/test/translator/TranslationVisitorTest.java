package translator;

import model.Model;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.SimpleNode;
import static org.mockito.Mockito.*;



public class TranslationVisitorTest {
	
	
	@Test
	public void testModelReturned() {
		SimpleNode ast = mock(SimpleNode.class);
		
		TranslationVisitor visitor = new TranslationVisitor(ast);
		Model model = visitor.translate();
		
		assertNotNull(model);
	}
	
}

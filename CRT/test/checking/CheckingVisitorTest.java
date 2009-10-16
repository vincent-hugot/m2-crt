package checking;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import parser.SimpleNode;



public class CheckingVisitorTest {
	
	
	/**
	 * Test a few things about error handling :
	 * - Error list is created at instantiation time
	 * - Error list is initially empty
	 * - When fail() is activated, one and only one error is added (tested 5 times)
	 */
	@Test
	public void testErrorAdded() {
		SimpleNode ast = mock(SimpleNode.class);
		
		CheckingVisitor visitor = new CheckingVisitor(ast,"");
		
		assertNotNull("CheckingVisitor - fail() - Error list created",
				visitor.getErrors());
		assertTrue("CheckingVisitor - fail() - Error list empty",
				visitor.getErrors().isEmpty());
		
		for (int i=1; i<=5; i++) {
			int nbrErrors = visitor.getErrors().size();
			visitor.fail("Whoops", 42);
			assertEquals("CheckingVisitor - fail() - Error "+i+" added",
					visitor.getErrors().size(), nbrErrors+1);
		}
	}
	
}

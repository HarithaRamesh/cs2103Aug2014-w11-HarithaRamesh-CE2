
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextBuddyTest {
	
	@BeforeClass
	public static void initialize() throws IOException{
		TextBuddy.prepareTextBuddy("mytextfile.txt");
	}

	
	@Test
	public void testClear() throws IOException{
		//check if the "clear" command returns the right status message
		assertEquals("all content deleted from mytextfile.txt", TextBuddy.executeCommand("clear"));
		//check if the file was actually cleared
		assertEquals(0, TextBuddy.getLineCount()); 

	}
	
	@Test
	public void testAdd() throws IOException{
		// check if "add" command returns the right message
		assertEquals("added to mytextfile.txt: \"hello\"", TextBuddy.executeCommand("add hello"));
		// check if hello is added
		assertEquals(1, TextBuddy.getLineCount()); 
	}
	
	@Test
	public void testDelete() throws IOException{
		//to set the hello for this test to delete
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add hello");
		
		//or could work on a separate text file. 
		
		// check if "delete" command returns the right message
		assertEquals("deleted from mytextfile.txt: \"hello\"", TextBuddy.executeCommand("delete 1"));
		// check if hello is deleted
		assertEquals(0, TextBuddy.getLineCount()); 
	}
	
}

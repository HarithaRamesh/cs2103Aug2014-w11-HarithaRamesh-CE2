
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextBuddyTest {
	
	@BeforeClass
	public static void initialize() throws IOException{
		TextBuddy.prepareTextBuddy("mytextfile.txt");
	}
	//assertEquals("1. aaa"+System.lineSeparator()+"2. bbb", 
		//	TextBuddy.executeCommand("display")); 
	@Test
	public void testSortEmpty() throws IOException{
		TextBuddy.executeCommand("clear");
		//check if sorting empty file returns message
		assertEquals("mytextfile.txt is empty", TextBuddy.sort("sort"));
	
	}
	
	@Test
	public void testSortCommand() throws IOException{
		TextBuddy.executeCommand("add aaa");
		//check if the "sort" command returns the right status message
		assertEquals("sorted mytextfile.txt successfully", TextBuddy.sort("sort"));
		

	}
	

	
}

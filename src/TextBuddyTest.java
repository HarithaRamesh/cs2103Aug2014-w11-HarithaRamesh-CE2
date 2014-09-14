
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextBuddyTest {
	
	private static StringBuilder sortedLines = new StringBuilder();		
	@BeforeClass
	public static void initialize() throws IOException{
		TextBuddy.prepareTextBuddy("mytextfile.txt");
		setInitialFile();
		setOutputString();
		
	}
	
	@Test
	public void testSort() throws IOException{
		
		//check if invalid input commands are checked
		assertEquals("invalid command format: sort alpha", TextBuddy.executeCommand("sort alpha"));
		
		//check if the "sort" command returns the right status message
		assertEquals("sorted mytextfile.txt successfully", TextBuddy.executeCommand("sort"));
		
		//check if sorted correctly
		assertEquals(sortedLines.toString(), TextBuddy.executeCommand("display"));
				
		//check if sorting empty file returns empty message
		TextBuddy.executeCommand("clear");
		assertEquals("mytextfile.txt is empty", TextBuddy.executeCommand("sort"));

	}


	public static void setInitialFile() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add Ccc");
		TextBuddy.executeCommand("add aaa");
		TextBuddy.executeCommand("add !hey there");
		TextBuddy.executeCommand("add Aaa");
		TextBuddy.executeCommand("add BbB");
		TextBuddy.executeCommand("add ccc");
	}


	public static void setOutputString() {
		sortedLines.append("1. !hey there");
		sortedLines.append(System.lineSeparator());
		sortedLines.append("2. aaa");
		sortedLines.append(System.lineSeparator());
		sortedLines.append("3. Aaa");
		sortedLines.append(System.lineSeparator());
		sortedLines.append("4. BbB");
		sortedLines.append(System.lineSeparator());
		sortedLines.append("5. Ccc");
		sortedLines.append(System.lineSeparator());
		sortedLines.append("6. ccc");
	}

	
}

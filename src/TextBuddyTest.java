
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextBuddyTest {
	
	private static StringBuilder sortOutputLines = new StringBuilder();	
	private static StringBuilder searchOutputLines = new StringBuilder();	
	@BeforeClass
	public static void initialize() throws IOException{
		TextBuddy.prepareTextBuddy("mytextfile.txt");
		
	}
	
	@Test
	public void testSort() throws IOException{
		setInitialSortInputs();
		setSortOutput();
		//check if invalid input commands returns right message
		assertEquals("invalid command format: sort blah", TextBuddy.executeCommand("sort blah"));
		
		//check if the "sort" command returns the right status message
		assertEquals("sorted mytextfile.txt successfully", TextBuddy.executeCommand("sort"));
		
		//check if sorted correctly
		assertEquals(sortOutputLines.toString(), TextBuddy.executeCommand("display"));
				
		//check if sorting empty file returns empty message
		TextBuddy.executeCommand("clear");
		assertEquals("mytextfile.txt is empty", TextBuddy.executeCommand("sort"));

	}
	@Test
	public void testSearch() throws IOException{
		setInitialSearchInputs();
		setSearchOutput();
		//check if no word input command returns right message
		assertEquals("invalid command format: search", TextBuddy.executeCommand("search"));
		
		//check if command with wrong number of parameters returns right message
		assertEquals("invalid command format: search cat dog",
				TextBuddy.executeCommand("search cat dog"));
		
		//check if no match returns the right message
		assertEquals("Cannot find word in mytextfile.txt: \"Hola\"", 
				TextBuddy.executeCommand("search Hola"));	
		
		//check if searching word returns right display
		assertEquals(searchOutputLines.toString(), 
				TextBuddy.executeCommand("search bananas"));
		
		//check if searching in empty file returns empty message
				TextBuddy.executeCommand("clear");
				assertEquals("mytextfile.txt is empty", TextBuddy.executeCommand("search bananas"));
		
	}


	public void setInitialSortInputs() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add Ccc");
		TextBuddy.executeCommand("add aaa");
		TextBuddy.executeCommand("add !hey there");
		TextBuddy.executeCommand("add Aaa");
		TextBuddy.executeCommand("add BbB");
		TextBuddy.executeCommand("add ccc");
	}
	
	public void setSortOutput() {
		sortOutputLines.append("1. !hey there");
		sortOutputLines.append(System.lineSeparator());
		sortOutputLines.append("2. aaa");
		sortOutputLines.append(System.lineSeparator());
		sortOutputLines.append("3. Aaa");
		sortOutputLines.append(System.lineSeparator());
		sortOutputLines.append("4. BbB");
		sortOutputLines.append(System.lineSeparator());
		sortOutputLines.append("5. Ccc");
		sortOutputLines.append(System.lineSeparator());
		sortOutputLines.append("6. ccc");
	}
	public void setInitialSearchInputs() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add Bananas are great!");
		TextBuddy.executeCommand("add Have a banana");
		TextBuddy.executeCommand("add Hey there bananas");
		TextBuddy.executeCommand("add BaNaNAnana");
		TextBuddy.executeCommand("add Banana-rama whee");
		TextBuddy.executeCommand("add peel my BANANAS");
	}
	
	public void setSearchOutput() {
		searchOutputLines.append("1. Bananas are great!");
		searchOutputLines.append(System.lineSeparator());
		searchOutputLines.append("3. Hey there bananas");
		searchOutputLines.append(System.lineSeparator());
		searchOutputLines.append("6. peel my BANANAS");
	}

}

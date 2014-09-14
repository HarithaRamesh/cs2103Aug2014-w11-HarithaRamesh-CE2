import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Assumptions made: 
 * 
 * 1. User is allowed to edit already existing text files, or create one if the 
 * input file doesn't exist. The new lines added by user will be at end of text 
 * file and display will show all lines including those from existing text file. 
 * These lines can also be deleted.
 * 
 * 2. Save to text file after each editing command.
 * 
 * 3. Delete command only allows positive integers that represent line number as 
 * parameters for deletion. 
 * 
 * 4. Cannot add or search whitespaces
 * 
 * 5. Sort command sorts it alphabetically and ignores case
 * 
 * 6. Search command displays the lines with their respective line numbers from file, so that
 * if you want to delete a line, you can immediately just call the delete command and put the
 * respective line number
 * 
 * 7. Can only search for one word at a time
 * 
 * @author Haritha Ramesh
 *
 */


public class TextBuddy {
	

	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use.";
	private static final String MESSAGE_ADD = "added to %1s: \"%2s\"";
	private static final String MESSAGE_DELETE = "deleted from %1s: \"%2s\"";
	private static final String MESSAGE_ALL_CLEAR = "all content deleted from %1s";
	private static final String MESSAGE_EMPTY = "%1s is empty";
	private static final String MESSAGE_SORT = "sorted %1s successfully";
	private static final String MESSAGE_SEARCH_NOMATCH = "Cannot find word in %1s: \"%2s\"";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format: %1$s";
	private static final String MESSAGE_INVALID_LINES = "invalid line number: %1$s";

	// These are the possible command types and their respective commands
	enum CommandType { 
		ADD ("add"), 
		DISPLAY ("display"), 
		DELETE ("delete"), 
		CLEAR ("clear"), 
		SORT ("sort"), 
		SEARCH ("search"), 
		INVALID, 
		EXIT ("exit");

		private String commandString;

		CommandType(){
		}
		
		CommandType(String command){
			this.commandString = command;
		}
		
		String getCommand(){
			return commandString;
		}
	};
	
	//Array to keep track of lines in the text file. 
	private static ArrayList<String> textLines = new ArrayList<String>();
	
	private static Scanner scanner = new Scanner(System.in);
	
	
	private static String textFileName;
	private static File textFile;

	
	public static void main(String[] args) throws IOException {
		
		prepareTextBuddy(args[0]);
		runTextBuddy();

	}

	/**
	 * Initializes the file and shows welcome message
	 * @param string
	 * @throws IOException
	 */
	public static void prepareTextBuddy(String string) throws IOException {
		textFileName = string;
		textFile = new File(textFileName);
		readFile();
		showToUser(String.format(MESSAGE_WELCOME, textFileName));
	}
	
	/**
	 * Awaits and executes user command till user decides to exit. 
	 * @throws IOException
	 */
	private static void runTextBuddy() throws IOException {
		while(true){
			showToUser("command: ");
			String command = scanner.nextLine();
			String feedback = executeCommand(command);
			showToUser(feedback);
		}
	}
	
	/**
	 * Reads the lines in file and adds them (if any) to the textLines array
	 * so that we can manipulate the array of lines easily
	 * @throws IOException
	 */
	public static void readFile() throws IOException {

		checkAndCreateFile();
		BufferedReader br = new BufferedReader(new FileReader(textFile));

		try {

			String line = br.readLine();

			while (line != null) {
				textLines.add(line);
				line = br.readLine();
			}

		} finally {
			br.close();

		}
	}

	/**
	 * Checks if file exists, creates if file doesn't exist
	 */
	private static void checkAndCreateFile() {

		try {
			textFile.createNewFile();
		} catch (IOException ioe) {
			System.out.println("Error while creating empty file: " + ioe);
		}
	}

	public static String executeCommand(String userCommand) throws IOException {
		
		if (userCommand.trim().equals("")){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		String commandTypeString = getFirstWord(userCommand);

		CommandType commandType = determineCommandType(commandTypeString);

		switch (commandType) {
			case ADD:
				return add(userCommand);
			case DISPLAY:
				return display(userCommand);
			case DELETE:
				return delete(userCommand);
			case CLEAR:
				return clear(userCommand);
			case SORT:
				return sort(userCommand);
			case SEARCH:
				return search(userCommand);
			case INVALID:
				return String.format(MESSAGE_INVALID_FORMAT, userCommand);
			case EXIT:
				updateFile();
				System.exit(0);
			default:

				throw new Error("Unrecognized command type");
		}
	}

	private static CommandType determineCommandType(String commandTypeString) {
		
		if (commandTypeString == null){
			throw new Error("command type string cannot be null!");
		}

		if (commandTypeString.equalsIgnoreCase(CommandType.ADD.getCommand())) {
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase(CommandType.DISPLAY.getCommand())) {
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase(CommandType.DELETE.getCommand())) {
			return CommandType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase(CommandType.CLEAR.getCommand())) {
			return CommandType.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase(CommandType.SORT.getCommand())) {
			return CommandType.SORT;
		} else if (commandTypeString.equalsIgnoreCase(CommandType.SEARCH.getCommand())) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(CommandType.EXIT.getCommand())) {
		 	return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}
	
	/**
	 * Writes line array into text file
	 * @throws IOException
	 */
	public static void updateFile() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(textFile));

		try {
			
			Iterator<String> writeIter = textLines.iterator();

			while (writeIter.hasNext()) {
				String write = writeIter.next();
				bw.write(write);
				bw.newLine();
			}

		} finally {

			bw.close();
		}
		
	}

	/*
	 *=============== Methods of commands================
	 * 
	 */
	
	/**
	 * Searches for word in the lines of the file, only one word allowed
	 * @param userCommand
	 * @return
	 */
	private static String search(String userCommand) {
		String parameter = removeFirstWord(userCommand);
		
		if(parameter.isEmpty() || !hasCorrectNumOfParameters(parameter, 1)){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		if(textLines.isEmpty()){
			return String.format(MESSAGE_EMPTY, textFileName );
		}
		
		return searchForWordAndDisplay(parameter);
		
	}
	
	/**
	 * Iterates through lines to look for word, and displays a list of lines numbered
	 * by their respective line number from the original file
	 * @param keyword
	 * @return
	 */
	private static String searchForWordAndDisplay(String keyword) {
		StringBuilder displayLines = new StringBuilder();
		
		Iterator<String> iterArray = textLines.iterator();
		
		int numLines = 1;
		
		while (iterArray.hasNext()) {
			String nextLine = iterArray.next();
			
			//check if word exists in line and display if it does
			if (nextLine.toLowerCase().indexOf(keyword.toLowerCase()) != -1 ) {
				String outputLine = numLines + ". " + nextLine;
				displayLines.append(outputLine);
				//the last line should not have a line break after
				if(iterArray.hasNext()){
					displayLines.append(System.lineSeparator());
					numLines++;
				}
			}else{
				numLines++;
			}
		}
		//if no match found
		if(displayLines.length()==0){
			return String.format(MESSAGE_SEARCH_NOMATCH, textFileName, keyword);
		}
		return displayLines.toString();
	}
	
	/**
	 * Sorts alphabetically, ignoring case
	 * @param userCommand
	 * @return
	 * @throws IOException
	 */
	private static String sort(String userCommand) throws IOException {
		String parameter = removeFirstWord(userCommand);

		if(!parameter.isEmpty()){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		if(textLines.isEmpty()){
			return String.format(MESSAGE_EMPTY, textFileName );
		}
		
		Collections.sort(textLines, String.CASE_INSENSITIVE_ORDER);
		updateFile();
		return String.format(MESSAGE_SORT, textFileName );
	}
	
	/**
	 * Adds line given by user to the end of text file
	 * @param userCommand
	 * @return
	 * @throws IOException
	 */
	private static String add(String userCommand) throws IOException {
		String parameter = removeFirstWord(userCommand);
		
		if(parameter.isEmpty()){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		textLines.add(parameter);
		updateFile();
		return String.format(MESSAGE_ADD, textFileName, parameter);
		
	}
	
	/**
	 * Shows numbered list of lines to user
	 * @param userCommand
	 * @return
	 */
	private static String display(String userCommand) {
		String parameter = removeFirstWord(userCommand);
		
		if(!parameter.isEmpty()){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		if(textLines.isEmpty()){
			return String.format(MESSAGE_EMPTY, textFileName );
			
		}else{
			
			return createListOfLines();
			
		}
	}
	
	/**
	 * Creates list of all lines from the line array
	 * @return
	 */
	private static String createListOfLines() {
		
		StringBuilder displayLines = new StringBuilder();
		
		Iterator<String> iterArray = textLines.iterator();
		
		int numLines =1;
		while (iterArray.hasNext()) {
			String nextLine = iterArray.next();
			String outputLine = numLines + ". " + nextLine;
			displayLines.append(outputLine);
			//the last line should not have a line break after
			if(iterArray.hasNext()){
				displayLines.append(System.lineSeparator());
				numLines++;
			}
		}
		return displayLines.toString();
	}
	
	/**
	 * Deletes specified line from array using line number
	 * @param userCommand
	 * @return
	 * @throws IOException 
	 */
	private static String delete(String userCommand) throws IOException {
		String parameter = removeFirstWord(userCommand);
		
		if(parameter.isEmpty()|| !parameter.matches("[0-9]+")){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		int lineNum = Integer.parseInt(parameter)-1;
		
		if(lineNum >= textLines.size() || lineNum < 0 ){
			return String.format(MESSAGE_INVALID_LINES, lineNum+1);
		}
		
		String deletedLine = textLines.get(lineNum);
		textLines.remove(lineNum);
		updateFile();
		return String.format(MESSAGE_DELETE, textFileName, deletedLine );
	}

	private static String clear(String userCommand) throws IOException {
		String parameter = removeFirstWord(userCommand);
		
		if(!parameter.isEmpty()){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		textLines.clear();
		updateFile();
		return String.format(MESSAGE_ALL_CLEAR, textFileName );
	}

	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		return userCommand.trim().split("\\s+")[0];
	}
	
	private static Boolean hasCorrectNumOfParameters(String userCommand, int num){
		String[] parameters = userCommand.trim().split("\\s+");
		return (parameters.length == num);
	}
	public static void showToUser(String message) {
		System.out.println(message);
	}
	
	public static int getLineCount() {
		return textLines.size();
	}
}

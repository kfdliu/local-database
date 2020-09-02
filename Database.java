/*--------------------------------------------------------------------------------------*/
/*                                                                                      */
/*  Database.java - A text-based and menu-driven database manager that allows users to  */
/*                  view, edit and manage individual custom-made databases.             */
/*                                                                                      */
/*                  Options include:                                                    */
/*                  - View database                                                     */
/*                  - Edit database (add entry, delete entry, change entry)             */
/*                  - Search for entry in specified category                            */
/*                  - Sort by category in ascending or descending order                 */
/*                  - Change the name/password of database                              */
/*                  - Empty database                                                    */
/*                  - Delete database                                                   */
/*                                                                                      */
/*                  Users can create new databases or use existing ones.                */
/*                  All the databases are stored as ".ssf" (super secure files)         */
/*                  Database names and passwords are stored in namespasswords.ssf       */
/*                                                                                      */
/*--------------------------------------------------------------------------------------*/
/*                                                                                      */
/*  Author: Davis Liu                                                                   */
/*  Date: Monday, May 13th, 2019                                                        */
/*                                                                                      */
/*--------------------------------------------------------------------------------------*/
/*                                                                                      */
/*  Input: Database (.ssf) Files, Backup Filename, Search Query, Sort category, Entry   */
/*         to Delete/Edit, DB Category Names, DB names/passwords (namespasswords.ssf),  */
/*         Various menu choices, Entry values, New DB name/password                     */             
/*                                                                                      */
/*  Output: Various input validation/confirmation prompts, Table of Entries, New DB,    */
/*          Sorted DB, DB File, DB Backup, Results from search, User interface elements */
/*          Various menus and loading screens                                           */
/*                                                                                      */
/*--------------------------------------------------------------------------------------*/

import java.io.*;
import java.util.*;
import java.text.*;

public class Database
{
    // Adds a singular entry to the current database
    static void addEntry (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] categoryTitles, int[] count, BufferedReader stdin) throws IOException
    {
	clearScreen ();

	validateString (stdin, count [0], c1, 0, "What would like to add under category #1?" + " (" + categoryTitles [0] + ") "); // Asks the user for input to each category (1-5)
	validateString (stdin, count [0], c2, 1, "What would like to add under category #2?" + " (" + categoryTitles [1] + ") "); // The inputs are validated for length/blanks
	validateString (stdin, count [0], c3, 2, "What would like to add under category #3?" + " (" + categoryTitles [2] + ") ");
	validateString (stdin, count [0], c4, 3, "What would like to add under category #4?" + " (" + categoryTitles [3] + ") ");
	validateString (stdin, count [0], c5, 4, "What would like to add under category #5?" + " (" + categoryTitles [4] + ") ");
	count [0]++; // Increments the entries count because a new entry has been added

	System.out.print ("\nEntry Successfully Added!\n"); // Confirmation message
    }


    // Displays the menu that allows the user to change either the name or password of the database they currently have open, receives input
    static void changeCredentials (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] database, String[] names, String[] pws, String[] namePass, int[] count, BufferedReader stdin) throws IOException, InterruptedException
    {
	char response; // Stores the user's choice from the menu

	clearScreen ();

	printMenu (null, null, 5); // Prints the menu of ID 5 (Change name or Change Password)
	do // Loops until the user has selected a valid choice from the menu
	{
	    response = validateChar (stdin).charAt (0); // Receives input from the user and validates if it can be a char
	    switch (response) // Change name or password based on user's response
	    {
		case '1': // Changes the name of the current database and exits from the current database
		    changeName (c1, c2, c3, c4, c5, database, names, pws, namePass, count, stdin);
		    pressEnter ();
		    saveDatabase (database, c1, c2, c3, c4, c5, count); // Saves the database entries
		    database [0] = null; // Exits the current database
		    break;
		case '2': // Changes the password of the current database and exits from the current database
		    changePassword (c1, c2, c3, c4, c5, database, names, pws, namePass, count, stdin);
		    saveDatabase (database, c1, c2, c3, c4, c5, count); // Saves the database entries
		    pressEnter ();
		    database [0] = null; // Exits the current database
		    break;
		case 'e': // Returns back to the database menu
		    break;
		default:
		    System.out.print ("That is not a valid choice, please try again: "); // Prints invalid input prompt
		    response = ' ';
		    break;
	    }
	}
	while (response == (' ')); // Loops until the user has selected a valid choice from the menu
    }


    // Changes a single entry in the database
    static void changeEntry (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] categoryTitles, int[] count, BufferedReader stdin) throws IOException
    {
	String response = ""; // Stores the user's input
	int responseInt; // Stores the user's entry number of choice once the response is validated

	System.out.println (); // Print an empty line for aesthetics
	viewDatabase (c1, c2, c3, c4, c5, count); // Displays the database for the user to choose an entry number
	System.out.print ("\nWhich entry would you like to change? (entry #, enter 0 to go back) ");

	response = validateEntryNumber (count, stdin); // Receives and validates the users input so that it can be converted into an int
	responseInt = Integer.parseInt (response); // Convert the response into an int so it can be used in a loop

	if (responseInt != 0) // Will only change an entry if the user enters a number other than 0 (entering 0 returns ot the menu)
	{
	    validateString (stdin, responseInt, c1, 0, "\nWhat would you like to change the '" + categoryTitles [0] + "' to? "); // Asks the user for input to each category (1-5)
	    validateString (stdin, responseInt, c2, 1, "What would you like to change the '" + categoryTitles [1] + "' to? "); // The inputs are validated for length
	    validateString (stdin, responseInt, c3, 2, "What would you like to change the '" + categoryTitles [2] + "' to? ");
	    validateString (stdin, responseInt, c4, 3, "What would you like to change the '" + categoryTitles [3] + "' to? ");
	    validateString (stdin, responseInt, c5, 4, "What would you like to change the '" + categoryTitles [4] + "' to? ");
	    System.out.print ("\nEntry Successfully Changed!\n"); // Prints confirmatory message

	    pressEnter ();
	}
    }


    // Changes the name of the current database
    static void changeName (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] database, String[] names, String[] pws, String[] namePass, int[] count, BufferedReader stdin) throws IOException
    {
	int counter = 0; // Stores the amount of existing databases
	String response = "", line; // Response stores the user's  reponse and line stores the line that is currently being read from the text file

	System.out.print ("\nWhat would you like to rename the database to? "); // Asks the user what they want the new database's name to be

	do // Loops until the user enters something other than a blank space
	{
	    response = stdin.readLine (); // Recieves the user's response
	    if ((response.trim ()).equals ("")) // If the user types in an empty space, they are prompted to enter something
		System.out.print ("You cannot have an empty name! Please try again: ");
	}
	while (response == null || (response.trim ()).equals ("")); // Loops until the user enters something other than a an empty space

	BufferedWriter writer = new BufferedWriter (new FileWriter (response + ".ssf")); // Declaration of a file writing object that writes to a file with the desired name

	for (int i = 0 ; i < count [0] ; i++) // Cycles through all the existing entries in the database so they can be written to the new file
	{
	    writeLine (writer, c1 [i]); // Writes category 1 of the entry to a line
	    writeLine (writer, c2 [i]); // Writes category 2 of the entry to a line
	    writeLine (writer, c3 [i]); // Writes category 3 of the entry to a line
	    writeLine (writer, c4 [i]); // Writes category 4 of the entry to a line
	    writeLine (writer, c5 [i]); // Writes category 5 of the entry to a line
	}
	writer.close (); // Closes the writer object to save the changes

	(new File (database [0])).delete (); // Deletes the database file with the old name

	counter = readNPFile (names, pws); // Reads through all the existing databases and puts their names and passwords into arrays

	for (int i = 0 ; i < counter ; i++) // Cycles through all the database names to look for the old name of the database so it can be changed in the namespasswords file
	{
	    if (names [i].equals (namePass [0])) // When the old name is found, it is changed to the new name
	    {
		names [i] = response;
	    }
	}

	writeNPFile (names, pws, counter + 1); // Saves all the names and passwords back to the namespasswords file

	clearScreen ();
	System.out.println ("\nDatabase Name Change Successfully Saved! A Restart is Required."); // Confirmatory statement that tells user the current database will be closed
    }


    // Changes the password of the current database
    static void changePassword (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] database, String[] names, String[] pws, String[] namePass, int[] count, BufferedReader stdin) throws IOException
    {
	int counter = 0; // Stores the amount of existing entries
	String response = "", line; // Response stores the user's input and line stores the line that is currently being read from the text file

	System.out.print ("\nPlease enter your current password: "); // Asks the user to enter their current password
	do // Loops until the user enters in the correct password
	{
	    response = stdin.readLine (); // Receives the input from the user
	    if (!(response.trim ()).equals (namePass [1])) // If the password does not match, a message is printed
	    {
		System.out.print ("That password does not match! Please try again: ");
	    }
	}
	while (response == null || !(response.trim ()).equals (namePass [1])); // Loops until the user enters in the correct password

	System.out.print ("\nWhat would you like to set as the new password? "); // Asjs the user what they would like to set the new password to

	do // Loops until the user enters a password that is at least 5 characters long
	{
	    response = stdin.readLine (); // Receives input from the user
	    if ((response.trim ()).equals ("")) // If the password is empty, a message is printed
	    {
		System.out.print ("You cannot have an empty password! Please try again: ");
	    }
	    else if (response.length () < 5) // If the password is between 1-5 chaacters long, a message is printed
	    {
		System.out.print ("You must choose a password that is at least 5 characters long, please try again: ");
	    }
	}
	while (response == null || (response.trim ()).equals ("") || (response.trim ()).length () < 5); // Loops until the user enters a password that is at least 5 characters long

	counter = readNPFile (names, pws); // Reads through all the existing databases and puts their names and passwords into arrays

	for (int i = 0 ; i < counter ; i++) // Cycles through all the entries to look for the old password of the database so it can be changed in the namespasswords file
	{
	    if (names [i].equals (namePass [0]) && pws [i].equals (namePass [1])) // If the old name is found, it is changed to the current name
	    {
		pws [i] = response.trim ();
	    }
	}

	writeNPFile (names, pws, counter + 1); // Saves all the names and passwords back to the namespasswords file

	clearScreen ();
	System.out.println ("\nPassword Change Successfully Saved! A Restart is Required."); // Confirmatory statement that tells user the current database will be closed
    }


    static void checkEmpty (int[] count, boolean[] arrayEmpty)  // Returns true or false based on if the current database is empty or not
    {
	if (count [0] == 0 || count [0] == 1)
	{
	    arrayEmpty [0] = true; // If the count value is 0 or 1 which means the database is empty, true is returned
	}
	else
	{
	    arrayEmpty [0] = false; // If the database is not empty, returns false
	}
    }


    static void clearScreen ()  //"Clears" the screen by printing enough lines to push text off the screen
    {
	for (int i = 0 ; i < 40 ; i++) // Prints 40 lines to pseudo-clear the screen
	{
	    System.out.println ();
	}
    }


    //Creates a backup of the open collection
    static void createBackup (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] database, String[] categoryTitles, String[] names, String[] pws, String[] namePass, int[] count, BufferedReader stdin) throws IOException, InterruptedException
    {
	String response; // Stores the user's desired name of database backup
	int counter, backupNum = 1; //Counts how many existing databases there are, and backupNum counts the number of backup files

	clearScreen ();

	counter = readNPFile (names, pws); // Reads through all the existing databases and puts the names and passwords into arrays

	while ((new File ("Backup " + backupNum + " of " + namePass [0] + ".ssf")).exists ()) // Checks how many backups exist and increments the number if the previuos # exists
	{
	    backupNum++;
	}

	names [counter] = ("Backup " + backupNum + " of " + namePass [0]);  // Adds the new database to the array of database names
	pws [counter] = namePass [1]; // Adds the new password to the array of database passwords
	counter++; // Increments the counter because a new database has been added

	writeNPFile (names, pws, counter + 1); // Writes the arrays to the namespasswords.ssf file

	// Declaration of the file writing object that writes the new backup file with original database name and backup number attached
	BufferedWriter writer = new BufferedWriter (new FileWriter ("Backup " + backupNum + " of " + namePass [0] + ".ssf"));

	for (int i = 0 ; i < count [0] ; i++) // Cycles through all the entries
	{
	    // Writes all the entries into the new backup file
	    writeLine (writer, c1 [i]); // Writes category 1 of the entry to a line
	    writeLine (writer, c2 [i]); // Writes category 2 of the entry to a line
	    writeLine (writer, c3 [i]); // Writes category 3 of the entry to a line
	    writeLine (writer, c4 [i]); // Writes category 4 of the entry to a line
	    writeLine (writer, c5 [i]); // Writes category 5 of the entry to a line
	}
	writer.close (); // Closes the write object so that the changes are saved

	System.out.print ("\nCopying Contents"); // Prints a loading screen text

	for (int i = 0 ; i < 10 ; i++) // Prints the loading screen text dots
	{
	    Thread.sleep (150);
	    System.out.print (".");
	}

	clearScreen ();

	// Prints confirmatory message
	System.out.println ("\nBackup Successfully Saved as: " + "\"" + "Backup " + backupNum + " of " + namePass [0] + ".ssf\"");
	System.out.println ("The backup will have the same password as the current database.");
    }


    //Creates a new database and allows the user to set a password
    static void createDatabase (String[] categoryNames, String[] names, String[] pws, BufferedReader stdin) throws IOException
    {
	String name = "", pass = "", line; //Stores the name, password, and current line when reading a program
	boolean repeat; //Boolean that checks if the entered name already exists
	int counter; //Counts how many existing databases there are

	clearScreen ();

	counter = readNPFile (names, pws); // Reads through all the existing databases and puts the names and passwords into arrays

	System.out.print ("What would you like to name the new database? "); //Receives and validates the user's choice of database name
	do  // Loops until a unique name is entered
	{
	    repeat = false; // Sets the repeat boolean to the default of false
	    name = stdin.readLine (); // Recieves the name input from the user

	    for (int i = 0 ; i < counter ; i++) // Loops through all the existing database names to check for repetition
	    {
		if ((name.toLowerCase ()).equals (names [i].toLowerCase ())) // Compares the lowercase version of the entered name to the existing names
		{
		    repeat = true; // If the names are the same, repeat becomes true which causes the loop to repeat
		    System.out.print ("A database with that name already exists, please enter a unique name: "); // Tells user to enter input again
		}
	    }
	    if ((name.trim ()).equals ("")) // If the name entered is empty, it causes the loop to repeat
	    {
		System.out.print ("You cannot have an empty name! Please enter a name: "); // Prints message telling the user to enter a new name
	    }
	}
	while (repeat || (name.trim ()).equals ("")); // Loops until a unique name is entered

	System.out.print ("What would you like to set as the password? (Min. 5 characters) "); //Receives and validates the user's password choice
	while (((pass = stdin.readLine ()).trim ()).length () < 5) // Loops until a password of at least 5 characters is entered
	{
	    if (pass.length () < 5) // If the password is less than 5 characters, user will be prompted to enter a new password
	    {
		System.out.print ("That password is too short, please enter a new password: ");
	    }
	}

	names [counter] = name; // Adds the new database to the array of database names
	pws [counter] = pass; // Adds the new password to the array of database passwords
	counter++; // Increments the counter because a new database has been added

	writeNPFile (names, pws, counter + 1); // Writes the arrays to the namespasswords.ssf file

	BufferedWriter categoryWriter = new BufferedWriter (new FileWriter (name + ".ssf")); // Writer object that writes the category names to the new file

	clearScreen ();

	System.out.println ("NOTE: Max 21 characters for each category name, if a blank is entered, the category is set to N/A."); //Receives and validates the 5 category names
	for (int i = 0 ; i < 5 ; i++) // Loops 5 times for the 5 categories
	{
	    do // Loops until an entry that is 25 or less characters is entered
	    {
		System.out.print ("Please enter the name of category #" + (i + 1) + ": "); // Asks the user to input 1 of the 5 categories
		categoryNames [i] = stdin.readLine (); // Receives input from the user
		if ((categoryNames [i].trim ()).length () > 21) // If the entered category name is too long, user is prompted
		{
		    System.out.print ("That name is too long! ");
		}
		else if ((categoryNames [i].trim ()).length () == 0 || (categoryNames [i].trim ()).equals ("")) // If the user enters a blank, the category name is set to N/A
		{
		    System.out.println ("This category has been set to N/A.");
		    categoryNames [i] = "N/A";
		}
	    }
	    while (categoryNames [i].length () > 25); // Loops until an entry that is 25 or less characters is entered
	}

	for (int i = 0 ; i < 5 ; i++) //Writes the category names to the file
	{
	    writeLine (categoryWriter, categoryNames [i]); // Wites each category name to a new line
	}
	categoryWriter.close (); // Closes the file writer object to save the changes

	clearScreen ();

	System.out.print ("Database with the name \"" + name + "\" and password \""); // Confirms name and password of the new database
	// Prints "hidden" password (example: password = ****word)
	for (int i = 0 ; i < pass.length () - 4 ; i++)
	    System.out.print ("*");
	for (int i = 4 ; i > 0 ; i--)
	    System.out.print (pass.charAt (pass.length () - i));

	System.out.print ("\" successfully created!\n"); // Prints confirmatory message

	pressEnter ();
	clearScreen ();
    }


    static void credentialsResponse (boolean credentials) throws InterruptedException //Prints a message depending on if the database name/password entered are valid
    {
	if (!credentials) // If the credentials are wrong, prints a message prompting user to try again
	{
	    System.out.println ("\nIncorrect credentials, please try again!");
	    Thread.sleep (1000);
	    clearScreen ();
	}
	else // If the credentials are correct, prints the login loading screen text
	{
	    System.out.print ("\nNow logging on");
	    Thread.sleep (750);
	    for (int i = 0 ; i < 10 ; i++)
	    {
		Thread.sleep (400);
		System.out.print (".");
	    }
	}
    }


    static String databaseMenuSelection (BufferedReader stdin) throws IOException // Receives the input from the user for the main menu (the databasse menu)
    {
	String response = ""; // Stores the the user's choice form the database menu

	System.out.print ("\nPlease select an option: "); // Asks the user to select an option from the database menu
	do // Loops until a valid choice from the menu is entered
	{
	    response = stdin.readLine ();
	    if (!response.equals ("1") && !response.equals ("2") && !response.equals ("3") && !response.equals ("4") && !response.equals ("5") && !response.equals ("6") && !response.equals ("7") && !response.equals ("8") && !response.equals ("e") && !response.equals ("E"))
	    {
		System.out.print ("Incorrect input, please try again: "); // Tells the user if they entered an incorrect choice
	    }
	}
	while (!response.equals ("1") && !response.equals ("2") && !response.equals ("3") && !response.equals ("4") && !response.equals ("5") && !response.equals ("6") && !response.equals ("7") && !response.equals ("8") && !response.equals ("e") && !response.equals ("E"));
	// Loops until a valid choice from the menu is entered

	return response; // Returns a validated response
    }


    // Deletes the current database
    static boolean deleteDatabase (String[] database, String[] names, String[] pws, String[] namePass, BufferedReader stdin) throws IOException, InterruptedException
    {
	char response; // Stores the user's choice
	int counter, deleteLocation = 0; // Counter tracks how many total databases exist, deleteLocation is the location of the deleted file in the names and pws arrays

	System.out.print ("\nAre you sure about this? (Y/N) "); // Ask the user if they are sure about their choice
	do // Loops until y/n is entered the first time
	{
	    response = validateChar (stdin).charAt (0); // Recieves and validates the user's choice from the menu to be a char
	    response = setLowercase (response); // Sets the char value of the reponse to be lowercase

	    switch (response)
	    {
		case 'y':
		    System.out.print ("\nAre you really super duper sure about this? (Y/N) "); // Ask the user again if they are sure about their choice
		    do // Loops until y/n is entered the second time
		    {
			response = validateChar (stdin).charAt (0); // Receives and validates the user's choice from the menu to be a char
			response = setLowercase (response); // Sets the char value of the reponse to be lowercase

			switch (response)
			{
			    case 'y':
				System.out.println (); // Prints countdown text of deleting the file
				for (int i = 5 ; i > 0 ; i--)
				{
				    System.out.print (i);
				    Thread.sleep (250);
				    System.out.print (".");
				    Thread.sleep (250);
				}
				System.out.print (" Poof\n"); // Prints loading screen text of deleting the file

				counter = readNPFile (names, pws); // Reads the namespasswords.ssf to 2 arrays

				for (int i = 0 ; i < counter ; i++) // Cycles through all the database to look for the database that will be deleted
				{
				    if (names [i].equals (namePass [0]) && pws [i].equals (namePass [1])) // Sets the name of the database and its password to null
				    {
					names [i] = null;
					pws [i] = null;
					deleteLocation = i;
				    }
				}
				for (int i = deleteLocation ; i < counter - deleteLocation ; i++) // Pushes back any existing database names and passwords to fill the null created
				{
				    swapTwoValues (names, i); // Pushes the null useername entry 1 element up
				    swapTwoValues (pws, i); // Pushes the null password entry 1 element up
				}

				writeNPFile (names, pws, counter); // Writes the new changes to namepasswords.ssf
				new File (database [0]).delete (); // Deletes the database file
				return true; // Informs the main method that the file was successfully deleted
			    case 'n':
				System.out.println ("\nWhew... that was a close one!"); // Prints a confirmatory message
				pressEnter ();
				return false; // Informs the main method that the file was not deleted
			    default:
				System.out.print ("That is not a valid choice, please try again: "); // Tells the user that their choice is not valid
				break;
			}
		    }
		    while (response != 'y' && response != 'n'); // Loops until y/n is entered the second time
		case 'n':
		    System.out.println ("\nWhew... that was a close one!"); // Prints a confirmatory message
		    pressEnter ();
		    return false; // Informs the main method that the file was not deleted
		default:
		    System.out.print ("That is not a valid choice, please try again: "); // Tells the user that their choice is not valid
		    break;
	    }
	}
	while (response != 'y' && response != 'n'); // Loops until y/n is entered the first time
	return false;
    }


    // Deletes an entry in the database
    static void deleteEntry (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] categoryTitles, int[] count, BufferedReader stdin) throws IOException
    {
	String response; // String that holds the entry that the user wants to delete

	System.out.println ();
	viewDatabase (c1, c2, c3, c4, c5, count); // Displays the database for the user to choose an entry to delete
	System.out.print ("\nWhich entry would you like to delete (entry #, enter 0 to go back)? "); // Asks the user which entry they'd like to delete

	response = validateEntryNumber (count, stdin); // Validates the response to be able to be read as an integer and be an existing entry number

	if (Integer.parseInt (response) != 0) // Only deletes a file if a number other than 0 is entered
	{
	    for (int i = Integer.parseInt (response) ; i < count [0] ; i++) // Moves the entry to be deleted to be the highest element number in the array
	    {
		c1 [i] = c1 [i + 1];
		c2 [i] = c2 [i + 1];
		c3 [i] = c3 [i + 1];
		c4 [i] = c4 [i + 1];
		c5 [i] = c5 [i + 1];
	    }
	    c1 [count [0]] = null; // Sets the entry to be deleted to null
	    c2 [count [0]] = null;
	    c3 [count [0]] = null;
	    c4 [count [0]] = null;
	    c5 [count [0]] = null;
	    count [0]--; // Decreases the entry count because an entry was just deleted

	    System.out.print ("\nEntry Successfully Deleted!\n"); // Confirmatory message

	    pressEnter ();
	}
    }


    static boolean doubleTest (String testString)  // Validates if a given string can be converted into a double
    {
	try
	{
	    Double.parseDouble (testString); // Tries to convert the String into an double
	}
	catch (Exception e)  // If the string cannot be converted, false is returned
	{
	    return false;
	}
	return true; // If the string can be converted, true is returned
    }


    // Displays the menu that allows the user to choose to add, delete, or change an entry in the current database, accepts the user's choice
    static void editDatabase (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] database, String[] categoryTitles, int[] count, BufferedReader stdin, boolean[] emptyArray, int MAX) throws IOException
    {
	char response; // Stores the user's choice from the menu

	clearScreen ();

	printMenu (null, null, 2); // Prints menu of ID 2 (add, delete, change)
	do // Loops until a valid choice from the menu is chosen
	{
	    response = validateChar (stdin).charAt (0); // Receives input and validates it to be a char
	    switch (response)
	    {
		case '1': // Adds an new entry to the database
		    if (count [0] != MAX) // Checks if the database is at max capacity, and only allows entry additions if it isn't
		    {
			addEntry (c1, c2, c3, c4, c5, categoryTitles, count, stdin);
			pressEnter ();
		    }
		    else // Prompts the user to delete entries if the databae is full
		    {
			System.out.println ("\nThe database is currently full, please delete some entries");
			pressEnter ();
		    }
		    break;
		case '2': // Deletes an entry in the database
		    checkEmpty (count, emptyArray); // Checks if the database is empty
		    if (emptyArray [0])
		    {
			System.out.println ("\nThe database is empty!"); // If the dataabse is empty, tells the user it is empty
			pressEnter ();
		    }
		    else // Only allows the user to delete an entry if the database is not full
		    {
			deleteEntry (c1, c2, c3, c4, c5, categoryTitles, count, stdin);
		    }
		    break;
		case '3': // Deletes an entry in the database
		    checkEmpty (count, emptyArray); // Checks if the database is empty
		    if (emptyArray [0])
		    {
			System.out.println ("\nThe database is empty!"); // If the dataabse is empty, tells the user it is empty
			pressEnter ();
		    }
		    else // Only allows the user to change an entry if the database is not full
		    {
			changeEntry (c1, c2, c3, c4, c5, categoryTitles, count, stdin);
		    }
		    break;
		case 'e': // returns the user back to the previous menu
		    break;
		default:
		    System.out.print ("That is not a valid choice, please try again: "); // Prompts the user to enter a valid input and causes the loop to repeat
		    response = ' ';
		    break;
	    }
	}
	while (response == ' '); // Loops until a valid choice from the menu is chosen
    }


    // Clears all entries in the current database
    static void emptyDatabase (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, int[] count, BufferedReader stdin) throws IOException
    {
	char response; // Stores the user's choice

	System.out.print ("\nAre you sure about this? (y/n) "); // Ask the user if they are sure about this choice

	do // Validate for y/n response from user
	{
	    response = validateChar (stdin).charAt (0); // Validates that input is a char
	    response = setLowercase (response); // Converts choice to a lowercase char

	    switch (response)
	    {
		case 'y': // Empties the database
		    for (int i = 1 ; i < count [0] ; i++) // Sets all entries to null
		    {
			c1 [i] = null; // Sets entry in category 1 to empty
			c2 [i] = null; // Sets entry in category 2 to empty
			c3 [i] = null; // Sets entry in category 3 to empty
			c4 [i] = null; // Sets entry in category 4 to empty
			c5 [i] = null; // Sets entry in category 5 to empty
		    }
		    count [0] = 1; // Sets the number of entries back to 0, count is equal to 1 because the category titles count as 1 in the count

		    System.out.println ("\nDatabase successfully wiped clean!"); // Confirmational message

		    pressEnter ();
		    break;
		case 'n': // Returns the user back to the previous menu
		    break;
		default:
		    System.out.print ("That is not a valid choice, please try again: "); // Message prompting user to put in valid input
		    break;
	    }
	}
	while (response != 'y' && response != 'n'); // Keeps looping until y/n is entered
    }


    //Prints a message for exiting the program then closes the window if closeProgram is set to true
    static void exitMessage (boolean closeProgram) throws InterruptedException
    {
	System.out.print ("\nNow exiting"); // Prints now exiting message
	Thread.sleep (500);

	for (int i = 0 ; i < 10 ; i++) // Prints 10 dots for the loading screen
	{
	    Thread.sleep (250); // Delays the loading dots
	    System.out.print ("."); // Prints the loading dots
	}

	if (closeProgram) // If the boolean passed to the method is equal to true, closes the program
	{
	    System.exit (0); // Closess the window
	}
    }


    static boolean integerTest (String testString)  // Tests if an String can be converted to an integer
    {
	try
	{
	    Integer.parseInt (testString); // Tries to convert the String into an int
	}
	catch (Exception e)  // If the string cannot be converted, false is returned
	{
	    return false;
	}
	return true; // If the string can be converted, true is returned
    }


    static void login (String[] namePass, BufferedReader stdin) throws IOException //Prompts the user to enter a database name and password
    {
	System.out.println ("\nPlease enter your database credentials:\n");
	System.out.print ("Database Name (Guest User is \"potato\"): "); // Asks the user to enter a database name
	namePass [0] = stdin.readLine (); // Receives the name input from the user
	System.out.print ("Password (Guest User is \"pommedeterre\"): "); // Asks the user to enter a database password
	namePass [1] = stdin.readLine (); // Receives the name input from the user
    }


    static char loginMenu (BufferedReader stdin) throws IOException //Prints initial menu and receives input
    {
	char response; // Stores the user's choice from the menu

	printMenu (null, null, 0); // Print menu of ID code 0 (existing database, new database, exit)

	do // Loops until the user enters a valid choice from the menu
	{
	    response = validateChar (stdin).charAt (0); // Validates the input to be a char
	    response = setLowercase (response); // Converts the char to lowercase

	    if (response != 'a' && response != 'b' && response != 'c' && response != ' ') // If the input is invalid, prints message prompting user to try again
	    {
		System.out.print ("Incorrect input, please try again: ");
	    }
	}
	while (response != 'a' && response != 'b' && response != 'c' && response != ' '); // Loops until the user enters a valid choice from the menu

	return response; // Returns the validated choice from the menu
    }


    static void openReadMe () throws IOException // Opens the readme.txt file when the program opens
    {
	Runtime runtime = Runtime.getRuntime ();
	Process process = runtime.exec ("notepad.exe readme.txt");
    }


    static void openingScreen () throws IOException, InterruptedException // Deals with program opening sequence and giant name printout
    {
	BufferedReader reader = new BufferedReader (new FileReader ("opening sequence.ssf")); // Creates a reader object to read from the the opening sequence graphics file
	String line;  //Stores the line that is currently being read in the file

	// Prints amessage box that prompts the user to enable fullscreen mode on the program
	System.out.println ("\n\n\nO------------------------------------------------------------O");
	System.out.println ("| This program is best experienced in fullscreen mode.       |\n| Please set the window to fullscreen mode before continuing.|");
	System.out.println ("O------------------------------------------------------------O");

	pressEnter ();

	System.out.println ("\n\n\n\n\n\n\n"); // Prints a bunch of empty lines
	Thread.sleep (500); // Dramatic pause

	while ((line = reader.readLine ()) != null) // Keeps reading the file until it reaches blank space at the end
	{
	    System.out.println (line); // Prints each line from the text file in a delayed manner
	    Thread.sleep (150);
	}
	reader.close (); // Closes the reader

	Thread.sleep (1000); // Dramatic pause
	clearScreen ();
    }


    static void pressEnter () throws IOException //Prompts the user to press enter to continue
    {
	System.out.print ("\nPress ENTER to continue..."); // Prompts the user to press enter
	try
	{
	    int read = System.in.read (new byte [2]);
	}
	catch (IOException e)
	{
	    e.printStackTrace ();
	}
    }


    static void printDatabases (String[] names, String[] pws) throws IOException // Prints out all the current available databases
    {
	int counter; // Stores the number of existing databases

	clearScreen ();

	System.out.println ("Existing Databases: \n");
	counter = readNPFile (names, pws); // Reads all the existing databases to the arrays and returns the number of existing databases

	for (int i = 0 ; i < counter ; i++) // Prints out all the available databases as a nice list when the user needs to enter credentials
	{
	    System.out.println ("~ " + names [i]);
	}
    }


    static void printLongLine (char midChar, char edgeChar)  //Prints a long line of char c with char c2 at the ends
    {
	System.out.print (edgeChar); // Print out the character at the end of the line
	for (int i = 0 ; i < 155 ; i++) // Constant value of 155 (the length of the full size window)
	{
	    System.out.print (midChar); // Print out the chaaracters in the middle
	}
	System.out.println (edgeChar); // Print out the character at the end of the line
    }


    static void printMenu (String[] database, String[] categoryTitles, int menuID)  //Prints all the menus in the program, prints a different menu according to menuID passed
    {
	switch (menuID) // Displays a different menu depending on which menuID was passed when the method was called
	{
	    case 0: // Opening screen menu --> 0
		System.out.println ("Welcome to the Super Cool Database Manager!\n");
		System.out.println ("[A] Open an existing database");
		System.out.println ("[B] Create a new database (New User)");
		System.out.println ("[C] Exit");
		System.out.print ("\nPlease select an option: ");
		break;
	    case 1: // Current database options menu --> 1
		System.out.println ("Current Database: " + database [0] + "\n");
		System.out.println ("[1] View Database");
		System.out.println ("[2] Edit Database (Add, Delete, Change)");
		System.out.println ("[3] Search for Entry");
		System.out.println ("[4] Sort by Category");
		System.out.println ("[5] Create Backup");
		System.out.println ("[6] Change Database Name/Password");
		System.out.println ("[7] Empty Database");
		System.out.println ("[8] Delete Database");
		System.out.println ("[e] Save and Exit Current Database");
		System.out.println ("[E] Save and Exit Program");
		break;
	    case 2: // Edit database menu --> 2
		System.out.println ("[1] Add Entry");
		System.out.println ("[2] Delete Entry");
		System.out.println ("[3] Change Entry");
		System.out.println ("[e] Go Back");
		System.out.print ("\nPlease select an option: ");
		break;
	    case 3:  // Search database menu --> 3
		System.out.println ("[1] " + categoryTitles [0]);
		System.out.println ("[2] " + categoryTitles [1]);
		System.out.println ("[3] " + categoryTitles [2]);
		System.out.println ("[4] " + categoryTitles [3]);
		System.out.println ("[5] " + categoryTitles [4]);
		System.out.println ("[e] Go Back");
		System.out.print ("\nUnder which category would you like to search? ");
		break;
	    case 4: // Sort database menu --> 4
		System.out.println ("[1] " + categoryTitles [0]);
		System.out.println ("[2] " + categoryTitles [1]);
		System.out.println ("[3] " + categoryTitles [2]);
		System.out.println ("[4] " + categoryTitles [3]);
		System.out.println ("[5] " + categoryTitles [4]);
		System.out.println ("[e] Go Back");
		System.out.print ("\nBy which category would you like to sort? ");
		break;
	    case 5: // Change credentials menu --> 5
		System.out.println ("[1] Change Database Name");
		System.out.println ("[2] Change Database Password");
		System.out.println ("[e] Go Back");
		System.out.print ("\nPlease select an option: ");
		break;
	}
    }


    static void printPaddedEntry (String entry, boolean smallSize, boolean twoDigit)  //Prints an entry and pads it
    {
	if (smallSize) // Pads the entry numbers much less than the actual entries
	    if (!twoDigit) // Pads a bit more for 1 digit numbers
	    {
		System.out.print ("|   " + entry + "   ");
	    }
	    else // Pads less for 2 digit number
	    {
		System.out.print ("|   " + entry + "  ");
	    }
	else // Pads the entries to the same size for printing a nice table
	{
	    int x = 25 - entry.length (); // Calculates how much to pad the entries

	    System.out.print ("|   " + entry);

	    for (int i = 0 ; i < x ; i++)
	    {
		System.out.print (" ");
	    }
	}
    }


    static int readDatabaseFile (String[] database, String[] c1, String[] c2, String[] c3, String[] c4, String[] c5) throws IOException //Reads a file into the 5 category arrays
    {
	BufferedReader reader = new BufferedReader (new FileReader (database [0])); // Creates a file reader object to read the current database file
	String line = null;
	int count = 0; // Keeps track of the number of entries in the database

	while ((line = reader.readLine ()) != null && !line.trim ().equals ("")) // Keeps reading the file until it reaches blank space at the end
	{
	    c1 [count] = line; // Read category 1
	    line = reader.readLine ();
	    c2 [count] = line; // Read category 2
	    line = reader.readLine ();
	    c3 [count] = line; // Read category 3
	    line = reader.readLine ();
	    c4 [count] = line; // Read category 4
	    line = reader.readLine ();
	    c5 [count] = line; // Read category 5
	    count++; // Increment number of entries every 5 lines
	}
	reader.close (); // Closes the reader

	return count; //Returns the number of existing entries in the database
    }


    static int readNPFile (String[] names, String[] pws) throws IOException // Reads names and passwords from namepasswords.ssf and returns # of databases
    {
	BufferedReader npReader = new BufferedReader (new FileReader ("namespasswords.ssf"));
	String line; // Stores the ;ine currently being read in file
	int npCount = 0;  // Keeps track of the number of existing databases

	while ((line = npReader.readLine ()) != null && !line.trim ().equals ("")) // Keeps reading the file until it reaches blank space at the end
	{
	    names [npCount] = line; // Read database name
	    line = npReader.readLine ();
	    pws [npCount] = line; // Read accompanying password
	    npCount++; // Increment number of databases every 2 lines
	}
	npReader.close ();

	return npCount; // Returns the number of existing databases
    }


    //Writes the current arrays to the database file
    static void saveDatabase (String[] database, String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, int[] count) throws IOException, InterruptedException
    {
	BufferedWriter writer = new BufferedWriter (new FileWriter (database [0])); // Declares the writer object that will wrtie to the current database file
	for (int i = 0 ; i < count [0] ; i++) // Cycles through all the existing entries
	{
	    writeLine (writer, c1 [i]); // Writes the first category
	    writeLine (writer, c2 [i]); // Writes the second category
	    writeLine (writer, c3 [i]); // Writes the third category
	    writeLine (writer, c4 [i]); // Writes the fourth category
	    writeLine (writer, c5 [i]); // Writes the fifth category
	}
	writer.close (); // Closes the file writer object to save the changes to the database file

	System.out.println ("\nDatabase Successfully Saved!"); // Confirmatory message
	Thread.sleep (500);
    }


    //Searchs a specific category for a matching String
    static void searchCategory (BufferedReader stdin, String[] arr, String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, String[] c1Found, String[] c2Found, String[] c3Found, String[] c4Found, String[] c5Found, int[] count) throws IOException
    {
	String[] response = new String [1]; // Stores the category the user would like to search through
	int[] foundCount = new int [1]; // Stores the amount of matching entries found
	foundCount [0] = 1; // Initializes the found count to 1

	System.out.print ("\nWhat entry value would you like to search for? "); // Ask the user what they would like to search for
	response [0] = stdin.readLine (); // Receives the input from the user

	for (int i = 1 ; i < count [0] ; i++) // Searches through all the entries to find matches
	{
	    if (arr [i].equalsIgnoreCase (response [0])) // If there is a match, the matching entry is added to the arrays of found entries
	    {
		c1Found [foundCount [0]] = c1 [i]; // Assigns the matching values to arrays with only matching values
		c2Found [foundCount [0]] = c2 [i];
		c3Found [foundCount [0]] = c3 [i];
		c4Found [foundCount [0]] = c4 [i];
		c5Found [foundCount [0]] = c5 [i];
		foundCount [0]++; // Increment the amount of found meatches by 1
	    }
	}

	if (foundCount [0] == 1) // If no matches were found, a message is printed
	{
	    System.out.println ("\nNo results found. ");
	}
	else // If matches were found, a message is printed and the results are displayed
	{
	    System.out.println ("\nResults Found:\n");
	    viewDatabase (c1Found, c2Found, c3Found, c4Found, c5Found, foundCount); // Displays the results that were found
	}

	pressEnter ();
    }


    //Searches for specified value under a specified category
    static void searchDatabase (String[] database, String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, BufferedReader stdin, int[] count, String[] categoryTitles) throws IOException
    {

	char response; // Stores the user's choice from the menu
	// Arrays to store all the found results
	String[] c1Found = new String [50], c2Found = new String [50], c3Found = new String [50], c4Found = new String [50], c5Found = new String [50];

	c1Found [0] = categoryTitles [0]; // Sets all the first elements in the results arrays to the category titles
	c2Found [0] = categoryTitles [1];
	c3Found [0] = categoryTitles [2];
	c4Found [0] = categoryTitles [3];
	c5Found [0] = categoryTitles [4];

	clearScreen ();

	printMenu (null, categoryTitles, 3); // Prints menu of ID 3 (category1, category2, category3, caetgoery4, category5)
	do // Loops until the user enters a valid choice from the menu
	{
	    response = validateChar (stdin).charAt (0); // receives input and validates it to be a char
	    switch (response)
	    {
		case '1': // Searches under category 1
		    searchCategory (stdin, c1, c1, c2, c3, c4, c5, c1Found, c2Found, c3Found, c4Found, c5Found, count);
		    break;
		case '2': // Searches under category 2
		    searchCategory (stdin, c2, c1, c2, c3, c4, c5, c1Found, c2Found, c3Found, c4Found, c5Found, count);
		    break;
		case '3': // Searches under category 3
		    searchCategory (stdin, c3, c1, c2, c3, c4, c5, c1Found, c2Found, c3Found, c4Found, c5Found, count);
		    break;
		case '4': // Searches under category 4
		    searchCategory (stdin, c4, c1, c2, c3, c4, c5, c1Found, c2Found, c3Found, c4Found, c5Found, count);
		    break;
		case '5': // Searches under category 5
		    searchCategory (stdin, c5, c1, c2, c3, c4, c5, c1Found, c2Found, c3Found, c4Found, c5Found, count);
		    break;
		case 'e': // Returns to the previous menu
		    break;
		default:
		    System.out.print ("That is not a valid choice, please try again: "); // Prompts the user to enter a correct choice from the menu and repeats the loop
		    response = ' ';
		    break;
	    }
	}
	while (response == (' ')); // Loops until the user enters a valid choice from the menu
    }


    // Sets the categoryTitles array method by reading from the first 5 lines of the database file
    static void setCategoryTitles (String[] categoryTitles, BufferedReader reader) throws IOException
    {
	for (int i = 0 ; i < 5 ; i++)
	{
	    categoryTitles [i] = reader.readLine (); // The category titles are filled into an array to be used in other methods
	}
    }


    static char setLowercase (char c)  // Converts a char to lowercase
    {
	String str = "" + c; // Converts an entered char to a String before it can be converted to all lowercase
	return (str.toLowerCase ()).charAt (0); // Converts the 1 character String to lowercase
    }


    //Sorts by a single category in either ascending or descending order
    static void sortCategory (String[] database, String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, int[] count, BufferedReader stdin) throws IOException, InterruptedException
    {
	boolean swapped; // Is true if a swap has occured  at all through one cycle through the entries, false otherwise
	String temp; // A String used to swap values
	char response; // Stores the user's choice from the menu

	System.out.print ("\nWould you like to sort in ascending [1] or descending order [2]? "); // Asks if the user would like to sort in ascending or descending order
	do // Loops until the user enters 1/2
	{
	    response = validateChar (stdin).charAt (0); // Recives the user's input and validates it is a char
	    if (response != '1' && response != '2')
	    {
		System.out.print ("That is not a valid choice, please try again: "); // Prompts the user to enter valid input
	    }
	} // Loops until the user enters 1/2
	while (response != '1' && response != '2');

	/* This sorting algorithm checks if the category values in the entries can be converted to double. Because the user can create custom categories,
	   either string or double data can be entered in the database. However, doubles do not sort properly with the compareTo method, so they must
	   be compared numerically to yield the best results. */

	switch (response)
	{
	    case '1': // Sorts in ascending order using modified bubble sort
		for (int i = 0 ; i < count [0] - 1 ; i++)
		{
		    swapped = false;
		    for (int j = 1 ; j < count [0] - 1 - i ; j++)
		    {
			// Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
			if (doubleTest (c1 [j]) && doubleTest (c1 [j + 1]) && c1 [j].compareTo (c1 [j + 1]) != 0)
			{
			    // Using double values, checks if the two entries should be swapped
			    if (Double.parseDouble (c1 [j]) > Double.parseDouble (c1 [j + 1]))
			    {
				swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
			    }
			}
			// If the entry category value cannot be converted to a double, compares using compareTo()
			else if (!doubleTest (c1 [j]) && !doubleTest (c1 [j + 1]) && c1 [j].compareTo (c1 [j + 1]) != 0)
			{
			    // Using String values, checks if the two entries should be swapped
			    if (c1 [j].compareTo (c1 [j + 1]) > 0)
			    {
				swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
			    }
			}

			else // The two entries must be equal if it reaches here, begins sorting based on second priority
			{
			    // Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
			    if (doubleTest (c2 [j]) && doubleTest (c2 [j + 1])) //
			    {
				// Using double values, checks if the two entries should be swapped
				if (Double.parseDouble (c2 [j]) > Double.parseDouble (c2 [j + 1]) && c2 [j].compareTo (c2 [j + 1]) != 0)
				{
				    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				}
			    }
			    // If the entry category value cannot be converted to a double, compares using compareTo()
			    else if (!doubleTest (c2 [j]) && !doubleTest (c2 [j + 1]) && c2 [j].compareTo (c2 [j + 1]) != 0)
			    {
				// Using String values, checks if the two entries should be swapped
				if (c2 [j].compareTo (c2 [j + 1]) > 0)
				{
				    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				}
			    }

			    else  // The two entries must be equal if it reaches here, begins sorting based on third priority
			    {
				// Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
				if (doubleTest (c3 [j]) && doubleTest (c3 [j + 1])) //
				{
				    // Using double values, checks if the two entries should be swapped
				    if (Double.parseDouble (c3 [j]) > Double.parseDouble (c3 [j + 1]) && c3 [j].compareTo (c3 [j + 1]) != 0)
				    {
					swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				    }
				}
				// If the entry category value cannot be converted to a double, compares using compareTo()
				else if (!doubleTest (c3 [j]) && !doubleTest (c3 [j + 1]) && c3 [j].compareTo (c3 [j + 1]) != 0)
				{
				    // Using String values, checks if the two entries should be swapped
				    if (c3 [j].compareTo (c3 [j + 1]) > 0)
				    {
					swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				    }
				}

				else // The two entries must be equal if it reaches here, begins sorting based on fourth priority
				{
				    // Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
				    if (doubleTest (c4 [j]) && doubleTest (c4 [j + 1])) //
				    {
					// Using double values, checks if the two entries should be swapped
					if (Double.parseDouble (c4 [j]) > Double.parseDouble (c4 [j + 1]) && c4 [j].compareTo (c4 [j + 1]) != 0)
					{
					    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					}
				    }
				    // If the entry category value cannot be converted to a double, compares using compareTo()
				    else if (!doubleTest (c4 [j]) && !doubleTest (c4 [j + 1]) && c4 [j].compareTo (c4 [j + 1]) != 0)
				    {
					// Using String values, checks if the two entries should be swapped
					if (c4 [j].compareTo (c4 [j + 1]) > 0)
					{
					    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					}
				    }

				    else // The two entries must be equal if it reaches here, begins sorting based on fifth priority
				    {
					// Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
					if (doubleTest (c5 [j]) && doubleTest (c5 [j + 1]) && c5 [j].compareTo (c5 [j + 1]) != 0) //
					{
					    // Using double values, checks if the two entries should be swapped
					    if (Double.parseDouble (c5 [j]) > Double.parseDouble (c5 [j + 1]))
					    {
						swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					    }
					}
					// If the entry category value cannot be converted to a double, compares using compareTo()
					else if (!doubleTest (c5 [j]) && !doubleTest (c5 [j + 1]) && c5 [j].compareTo (c5 [j + 1]) != 0)
					{
					    // Using String values, checks if the two entries should be swapped
					    if (c5 [j].compareTo (c5 [j + 1]) > 0)
					    {
						swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					    }
					}
				    }
				}
			    }
			}
		    }
		    if (!swapped) // If the entries are alrady sorted, exit the loop
		    {
			i = count [0] - 1;
		    }
		}
		break;
	    case '2': // Sorts in descending order using modified bubble sort
		for (int i = 0 ; i < count [0] - 1 ; i++)
		{
		    swapped = false;
		    for (int j = 1 ; j < count [0] - 1 - i ; j++)
		    {
			// Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
			if (doubleTest (c1 [j]) && doubleTest (c1 [j + 1]) && c1 [j].compareTo (c1 [j + 1]) != 0) //
			{
			    // Using double values, checks if the two entries should be swapped
			    if (Double.parseDouble (c1 [j]) < Double.parseDouble (c1 [j + 1]))
			    {
				swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
			    }
			}
			// If the entry category value cannot be converted to a double, compares using compareTo()
			else if (!doubleTest (c1 [j]) && !doubleTest (c1 [j + 1]) && c1 [j].compareTo (c1 [j + 1]) != 0)
			{
			    // Using String values, checks if the two entries should be swapped
			    if (c1 [j].compareTo (c1 [j + 1]) < 0)
			    {
				swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
			    }
			}

			else // The two entries must be equal if it reaches here, begins sorting based on second priority
			{
			    // Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
			    if (doubleTest (c2 [j]) && doubleTest (c2 [j + 1])) //
			    {
				// Using double values, checks if the two entries should be swapped
				if (Double.parseDouble (c2 [j]) < Double.parseDouble (c2 [j + 1]) && c2 [j].compareTo (c2 [j + 1]) != 0)
				{
				    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				}
			    }
			    // If the entry category value cannot be converted to a double, compares using compareTo()
			    else if (!doubleTest (c2 [j]) && !doubleTest (c2 [j + 1]) && c2 [j].compareTo (c2 [j + 1]) != 0)
			    {
				// Using String values, checks if the two entries should be swapped
				if (c2 [j].compareTo (c2 [j + 1]) < 0)
				{
				    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				}
			    }

			    else // The two entries must be equal if it reaches here, begins sorting based on third priority
			    {
				// Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
				if (doubleTest (c3 [j]) && doubleTest (c3 [j + 1])) //
				{
				    // Using double values, checks if the two entries should be swapped
				    if (Double.parseDouble (c3 [j]) < Double.parseDouble (c3 [j + 1]) && c3 [j].compareTo (c3 [j + 1]) != 0)
				    {
					swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				    }
				}
				// If the entry category value cannot be converted to a double, compares using compareTo()
				else if (!doubleTest (c3 [j]) && !doubleTest (c3 [j + 1]) && c3 [j].compareTo (c3 [j + 1]) != 0)
				{
				    // Using String values, checks if the two entries should be swapped
				    if (c3 [j].compareTo (c3 [j + 1]) < 0)
				    {
					swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
				    }
				}

				else // The two entries must be equal if it reaches here, begins sorting based on fourth priority
				{
				    // Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
				    if (doubleTest (c4 [j]) && doubleTest (c4 [j + 1])) //
				    {
					// Using double values, checks if the two entries should be swapped
					if (Double.parseDouble (c4 [j]) < Double.parseDouble (c4 [j + 1]) && c4 [j].compareTo (c4 [j + 1]) != 0)
					{
					    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					}
				    }
				    // If the entry category value cannot be converted to a double, compares using compareTo()
				    else if (!doubleTest (c4 [j]) && !doubleTest (c4 [j + 1]) && c4 [j].compareTo (c4 [j + 1]) != 0)
				    {
					// Using String values, checks if the two entries should be swapped
					if (c4 [j].compareTo (c4 [j + 1]) < 0)
					{
					    swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					}
				    }

				    else // The two entries must be equal if it reaches here, begins sorting based on fifth priority
				    {
					// Checks if the category values from 2 adjacent entries can be converted to doubles and are not equal
					if (doubleTest (c5 [j]) && doubleTest (c5 [j + 1]) && c5 [j].compareTo (c5 [j + 1]) != 0) //
					{
					    // Using double values, checks if the two entries should be swapped
					    if (Double.parseDouble (c5 [j]) < Double.parseDouble (c5 [j + 1]))
					    {
						swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					    }
					}
					// If the entry category value cannot be converted to a double, compares using compareTo()
					else if (!doubleTest (c5 [j]) && !doubleTest (c5 [j + 1]) && c5 [j].compareTo (c5 [j + 1]) != 0)
					{
					    // Using String values, checks if the two entries should be swapped
					    if (c5 [j].compareTo (c5 [j + 1]) < 0)
					    {
						swapped = swapCategoryValues (c1, c2, c3, c4, c5, j); // Swaps the adjacent values and sets swapped to true
					    }
					}
				    }
				}
			    }
			}
		    }
		    if (!swapped) // If the entries are alrady sorted, exit the loop
		    {
			i = count [0] - 1;
		    }
		}
		break;
	    default:
		break;
	}

	System.out.print ("\nSorting"); // Loading screen text message
	for (int i = 0 ; i < 5 ; i++)
	{
	    Thread.sleep (300);
	    System.out.print (".");
	}
	System.out.println();

	viewDatabase (c1, c2, c3, c4, c5, count); // Displays the sorted values

	System.out.println ("\nSuccessfully Sorted!"); // Confirmatory message
	pressEnter ();
    }


    // Allows the user to sort the database by category and in ascending or descending order
    static void sortDatabase (String[] database, String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, BufferedReader stdin, int[] count, String[] categoryTitles) throws IOException, InterruptedException
    {
	char response; // Stores the user's choice from the menu

	clearScreen ();

	printMenu (null, categoryTitles, 4); // Prints menu of ID 4 (category1, category2, category3, category4, category5)
	do // Loops until the user enters a valid menu choice
	{
	    response = validateChar (stdin).charAt (0);
	    switch (response)
	    {
		case '1': // Sorts by the first category
		    sortCategory (database, c1, c2, c3, c4, c5, count, stdin);
		    break;
		case '2': // Sorts by the second category
		    sortCategory (database, c2, c1, c3, c4, c5, count, stdin);
		    break;
		case '3': // Sorts by the third category
		    sortCategory (database, c3, c1, c2, c4, c5, count, stdin);
		    break;
		case '4': // Sorts by the fourth category
		    sortCategory (database, c4, c1, c2, c3, c5, count, stdin);
		    break;
		case '5': // Sorts by the fifth category
		    sortCategory (database, c5, c1, c2, c3, c4, count, stdin);
		    break;
		case 'e': // Returns back to the previos menu
		    break;
		default:
		    System.out.print ("That is not a valid choice, please try again: "); // Prompts the user to enter valid input and repeats the loop
		    response = ' ';
		    break;
	    }
	}
	while (response == (' ')); // Loops until the user enters a valid menu choice
    }


    static void swapTwoValues (String[] arr, int j)  //Swaps 2 values in memory
    {
	String temp; // Temporary String for use in swapping 2 values in memory

	temp = arr [j];
	arr [j] = arr [j + 1];
	arr [j + 1] = temp;
    }


    static boolean swapCategoryValues (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, int arrayLocation)  // Swaps an entry in each category with the next entry
    {
	swapTwoValues (c1, arrayLocation); // Swaps the adjacent entries in category 1
	swapTwoValues (c2, arrayLocation); // Swaps the adjacent entries in category 2
	swapTwoValues (c3, arrayLocation); // Swaps the adjacent entries in category 3
	swapTwoValues (c4, arrayLocation); // Swaps the adjacent entries in category 4
	swapTwoValues (c5, arrayLocation); // Swaps the adjacent entries in category 5

	return true; // Tells the category sorting method that a swap has occured
    }


    static String validateChar (BufferedReader stdin) throws IOException //Asks the user for a string and will only return a string of length 1
    {
	String str; // Strores the user's input
	do // Loops until a String that can be converted to a char is entered
	{
	    str = stdin.readLine (); // Receives the input from the user
	    if (str.length () != 1 || str.equals ("")) // If the input is not valid, prompt the user to try again
	    {
		System.out.print ("That is not a valid choice, please try again: ");
	    }
	}
	while (str.length () != 1 || str.equals (""));

	return str; // Returns the validated String
    }


    static String validateEntryNumber (int[] count, BufferedReader stdin) throws IOException // Receives a number from the user and validates if it is a existing entry number
    {
	String response; // Stores the user's choice
	boolean validNumber = false; // Boolean to check if the entry number exists in the current database

	do // Loops until a valid string that is a valid entry number is enterd
	{
	    response = stdin.readLine (); // Receives input from the user
	    if (response.equals (""))
	    {
		response = " ";
	    }
	    else if (integerTest (response)) // If the input can be converted into an int
	    {
		if (Integer.parseInt (response) == 0) // If the user inputs 0, returns to the previos menu
		{
		    return response;
		}
		else if (Integer.parseInt (response) < 1 || Integer.parseInt (response) > (count [0] - 1)) // Checks if the input is a between 1 and the number of entries
		{
		    System.out.print ("That is not a valid choice, please try again: ");
		}
		else
		{
		    validNumber = true; // If the number is a valid, set validNumber to true
		}
	    }
	    else
	    {
		System.out.print ("That is not a valid choice, please try again: "); // Prompts the user to enter a valid input
	    }
	}
	while (!integerTest (response) || !validNumber); // Loops until a valid string that is a valid entry number is enterd

	return response; // Returns the validated String that can be converted to an int
    }


    //Checks if the database name and password match according to a text file containing existing credentials
    static boolean validateNamePassword (String dbName, String dbPass) throws IOException
    {
	BufferedReader npReader = new BufferedReader (new FileReader ("namespasswords.ssf")); // Declares the file reader object to read names/passwords
	String line; // Stores the line currently being read in the file

	while ((line = npReader.readLine ()) != null && !line.trim ().equals ("")) // Loops until the end of the file
	{
	    if (line.equals (dbName)) // If the name is found, checks the next line for the password
	    {
		if ((line = npReader.readLine ()).equals (dbPass)) // If the next line is the correct password, returns true
		{
		    npReader.close ();
		    return true;
		}
		else
		{
		    line = npReader.readLine (); // If the password is not correct, moves onto the next line
		}
	    }
	    npReader.readLine (); // Reads the next line if the name is not found on the current line
	}
	npReader.close ();

	return false; // returns false if the name is never found
    }


    //Validates an entry to not be empty, if it is, sets the string to "N/A"
    static void validateString (BufferedReader stdin, int count, String[] category, int titleNum, String message) throws IOException
    {
	System.out.print (message);
	do // Loops until a valid category entry of 25 or less characters is entered
	{
	    category [count] = stdin.readLine (); // Recieves the input from the user
	    if ((category [count].length ()) >= 21) // Prompts the user to enter valid input if the length of the inuput exceeds 25 characters
	    {
		System.out.print ("That entry exceeds the 21 character limit, please try again: ");
	    }
	    else if (category [count].equals ("")) // If the user enters a blank space, the category entry is automatically set to N/A
	    {
		System.out.println ("The entry has been set to N/A"); // Tells the user that the entry has been set to N/A
		category [count] = "N/A"; // Sets the entry category to N/A
	    }
	}
	while (category [count] == null || (category [count].length ()) >= 25); // Loops until a valid category entry of 25 or less characters is entered
    }


    static void viewDatabase (String[] c1, String[] c2, String[] c3, String[] c4, String[] c5, int[] count) throws IOException //Prints the database for the user to view
    {
	System.out.println ();
	printLongLine ('*', 'O'); // Prints the top line
	for (int i = 0 ; i < count [0] ; i++) // Prints all the stuff in between
	{
	    if (i > 0) // Prints entry numbers for the first column
	    {
		if (i <= 9)
		{
		    printPaddedEntry ("" + i, true, false); // Less padding for the entry numbers
		}
		else if (i > 9)
		{
		    printPaddedEntry ("" + i, true, true);
		}
	    }
	    else // Prints a '#' for the first row
	    {
		printPaddedEntry ("#", true, false); // Prints a pound symbol
	    }
	    printPaddedEntry (c1 [i], false, false); // Prints all 5 of the padded category entries
	    printPaddedEntry (c2 [i], false, false);
	    printPaddedEntry (c3 [i], false, false);
	    printPaddedEntry (c4 [i], false, false);
	    printPaddedEntry (c5 [i], false, false);
	    System.out.print ("   |\n"); // Ends the row
	    if (i < count [0] - 1) // Prints sperating lines for each entry except for the last entry
		printLongLine ('-', '|'); // Prints a line seperating each row
	}
	printLongLine ('*', 'O'); // Prints the bottom lines
    }


    //Writes a string to a file and makes a new line after
    static void writeLine (BufferedWriter writer, String line) throws IOException
    {
	writer.write (line); // Writes whatever line needs to be written
	writer.newLine (); // Creates a new line
    }


    static void writeNPFile (String[] names, String[] pws, int npCounter) throws IOException // Writes the names and passwords from arrays to namespasswords.ssf
    {
	BufferedWriter npWriter = new BufferedWriter (new FileWriter ("namespasswords.ssf")); // Writes the new list of names and passwords
	for (int i = 0 ; i < npCounter - 1 ; i++) // Cycles through all the existing names and passwords
	{
	    writeLine (npWriter, names [i]); // Writes the name database on the first line
	    writeLine (npWriter, pws [i]); // Writes the accompanying password under the database name
	}
	npWriter.close (); // Closes the writer to save the changes
    }


    public static void main (String str[]) throws IOException, InterruptedException // Where methods are called and all the magic happens
    {
	BufferedReader stdin = new BufferedReader (new InputStreamReader (System.in)); // Accepts user keyboard input for the program

	final int MAX = 100; // Maximum number of entries in the array is 99 (MAX = 100 because the categories take up one space)
	String[] namePass = new String [2]; // Array that stores the current database name and password
	String[] names = new String [50], pws = new String [50]; // Stores the names and passwords inside namespasswords.ssf
	// Arrays that store the entries in each category
	String[] category1 = new String [MAX], category2 = new String [MAX], category3 = new String [MAX], category4 = new String [MAX], category5 = new String [MAX];
	String[] categoryTitles = new String [5]; // Stores the category names of the database
	String[] database = new String [1]; // Stores name of the current database - Includes .ssf file extension
	int[] count = new int [1]; // Stores the current amount of entries in the database
	boolean[] emptyArray = new boolean [1];  // Boolean to check if the array is empty
	boolean credentials, exit = false; // Booleans to check if credentials are correct and if the user wants to exit
	char choice1, choice2; // Stores the first and second menu choices

	//openReadMe (); // Opens the readme.txt file for the user to read
	openingScreen (); // Plays the program opening sequence

	while (!exit) // Loops program until user chooses to exit (exit = true)
	{
	    choice1 = loginMenu (stdin); //Receive input from the user for the first menu choice
	    do // Loops until user selects a database, creates a new one or exits the program
	    {
		switch (choice1) // Switch statement that handles the first menu (use existing DB, create new DB, exit program)
		{
		    case 'a': // Log into an existing database
			printDatabases (names, pws); // Prints all the exisiting databases
			do // Loops until the user enters in a valid name and password
			{
			    login (namePass, stdin); // Ask the user to input the name and password
			    credentials = validateNamePassword (namePass [0], namePass [1]); // Validates the user's input
			    credentialsResponse (credentials); // Prints the appropriate response
			}
			while (!credentials); // Loops until the user enters in a valid name and password

			database [0] = namePass [0].trim () + ".ssf"; // Sets the current database String to the full file name
			count [0] = readDatabaseFile (database, category1, category2, category3, category4, category5); // Sets the initial amount of entries in the database
			clearScreen ();

			while (database [0] != null) // Loops until database is changed or exited (database [0] = null)
			{
			    BufferedReader reader = new BufferedReader (new FileReader (database [0])); //Declaration of file reader object
			    printMenu (database, null, 1); // Prints the main menu for the program
			    choice2 = databaseMenuSelection (stdin).charAt (0); // Prompts the user to make a selection from the menu
			    setCategoryTitles (categoryTitles, reader); // Fills the array containing the category name

			    switch (choice2) // Switch statement that handles the second menu ()
			    {
				case '1': // Prints out the collection
				    checkEmpty (count, emptyArray); // Checks the current amount of entries in the database
				    if (!emptyArray [0])
				    {
					// Runs the method that displays the database
					viewDatabase (category1, category2, category3, category4, category5, count);
				    }
				    else
				    {
					System.out.println ("\nThe database is empty! "); // Tells the user the database is empty and returns to the menu
				    }
				    pressEnter ();
				    clearScreen ();
				    break;
				case '2': // Allows the user to edit entries in the collection
				    // Runs the method that allows the user to edit the database entries
				    editDatabase (category1, category2, category3, category4, category5, database, categoryTitles, count, stdin, emptyArray, MAX);
				    clearScreen ();
				    break;
				case '3': // Allows the user to search for a specific entry
				    checkEmpty (count, emptyArray); // Checks the current amount of entries in the database
				    if (!emptyArray [0])
				    {
					// Runts the method that allows the user to search the database for entries
					searchDatabase (database, category1, category2, category3, category4, category5, stdin, count, categoryTitles);
				    }
				    else
				    {
					System.out.println ("\nThe database is empty!"); // Tells the user the database is empty and returns to the menu
					pressEnter ();
				    }
				    clearScreen ();
				    break;
				case '4': // Allows the user to sort the collection by a category (ascending, descending) and save if requested
				    checkEmpty (count, emptyArray); // Checks the current amount of entries in the database
				    if (!emptyArray [0])
				    {
					// Runs the method that allows the user to sort the database by category
					sortDatabase (database, category1, category2, category3, category4, category5, stdin, count, categoryTitles);
				    }
				    else
				    {
					System.out.println ("\nThe database is empty!"); // Tells the user the database is empty and returns to the menu
					pressEnter ();
				    }
				    clearScreen ();
				    break;
				case '5': // Allows the user to create a backup of the collection with the date
				    checkEmpty (count, emptyArray); // Checks the current amount of entries in the database
				    if (!emptyArray [0])
				    {
					// Runs the method that creates a database
					createBackup (category1, category2, category3, category4, category5, database, categoryTitles, names, pws, namePass, count, stdin);
				    }
				    else
				    {
					System.out.println ("\nThe database is empty!"); // Tells the user the database is empty and returns to the menu
				    }
				    pressEnter ();
				    clearScreen ();
				    break;
				case '6': // Allows the user to change the name or password if the current database
				    reader.close ();
				    // Runs the method that allows the user to change the database name or password
				    changeCredentials (category1, category2, category3, category4, category5, database, names, pws, namePass, count, stdin);
				    clearScreen ();
				    break;
				case '7': // Allows the user to clear the entire database
				    checkEmpty (count, emptyArray); // Checks the current amount of entries in the database
				    if (!emptyArray [0])
				    {
					emptyDatabase (category1, category2, category3, category4, category5, count, stdin); // Runs the method to clear all entries
				    }
				    else
				    {
					System.out.println ("\nThe database is already empty!"); // Tells the user the database is empty and returns to the menu
					pressEnter ();
				    }
				    clearScreen ();
				    break;
				case '8': // Allows the user to delete the database
				    reader.close ();
				    if (deleteDatabase (database, names, pws, namePass, stdin)) // If the database was successfully deleted, exit the database
				    {
					exitMessage (false); // Prints program exit message
					database [0] = null; // Sets the current database to null
					clearScreen ();
				    }
				    else // If the database was not deleted because it was cancelled, nothing happens
				    {
					clearScreen ();
				    }
				    break;
				case 'e': // Saves database changes and exits the current database
				    saveDatabase (database, category1, category2, category3, category4, category5, count); // Writes the database arrays to the file
				    exitMessage (false); // Prints program exit message
				    reader.close (); // Closes the reader object
				    database [0] = null; // Sets the current database to null
				    clearScreen ();
				    break;
				case 'E': // Saves database changes and exits the window
				    saveDatabase (database, category1, category2, category3, category4, category5, count); // Writes the database arrays to the file
				    exitMessage (true);  // Prints program exit message
				    reader.close (); // Closes the reader object
				    database [0] = null; // Sets the current database to null
				    exit = true; // Tells the program to close
				    break;
				default:
				    System.out.print ("Incorrect input, please try again: "); // Prompts the user to enter a valid choice from the menu
				    break;
			    }
			}
			break;
		    case 'b': // Creates a new database with customizable name, password, and categorie names
			createDatabase (categoryTitles, names, pws, stdin); // Runs method to create a database
			break;
		    case 'c': // Exits the program
			exitMessage (true); // Prints program exit message
			database [0] = null; // Sets the current database to null
			exit = true; // Tells the program to close
			break;
		    default:
			break;
		}
	    }
	    while (database [0] != null); // Loops until user selects a database, creates a new one or exits the program
	}
    }
}

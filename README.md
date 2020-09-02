# local-database

**| ~ Super Cool Database Manager ~ |\
| ~     Davis Liu - June 2019     ~ |**

**What is This?**\
The Super Cool Database Manager is a text-based and menu-driven database manager written in java that is able to create, edit and manage custom local databases.

**|~ Provided Default Databases: ~|**

<hr/>

Name: potato\
Description: General potato collection\
Password: pommedeterre\
Number of Entries Provided: 50

Categories: 		 	
1. Type (Species)
2. Length (cm)
3. Weight (lb)
4. Date Added (YYYY/MM/DD)
5. Notes

<hr/>

Name: potato2\
Description: Potato-producing countries\
Password: kartoffel\
Number of Entries Provided: 10\

Categories: 		 	
1. Country
2. Rank
3. Num. of Species
4. Continent
5. Favourite Potato

<hr/>

Name: potato3\
Description: Potato farm\
Password: patata\
Number of Entries Provided: 30	

Categories: 		 	
1. Species
2. Age (Days)
3. Quality (SABCD)
4. Plant Date (YYYY/MM/DD) 
5. Plot Location
 
<hr/>

**|~ Important Notes: ~|**

1. Do not manually delete database files from the source folder. Safely delete the database files through the database manager program.

2. If the provided databases (potato.ssf) or namespasswords.ssf is accidentally deleted, a backup copy of the 4 files are available in the folder names "potato database backup files." 

3. .SSF files by default may not be opened by the user. However, for inspection and marking purposes, the files may be opened by right cicking and selecting "open with" and choosing notepad or another text editing program.

4. If the user wishes to view the password to a database because they forgot their password, the namespasswords.ssf file may be opened. The correct password will be located under the name of the database they would like to access.

5. Any changes to the database that result from using options 2, 4, or 7 will not save until the user exits from the current database or exits from the program using choices 'e' or 'E' in the database menu. 

6. For any confusions as to how each option in the program functions, refer to the database option descriptions below.

**|~ Database Option Descriptions ~|**

A. Open Existing Database
- Prints all existing database names from namespasswords.ssf
- Prompts the user to login using the name of their database and the password

B. Create New Database
- Creates a new database file and adds its name and password to namespasswords.ssf
- The user can customize the database name, duplicates are not permitted
- The user can customize the password (min. 5 characters)
- The user can customize the 5 categories that each of its entries will be based on
- New databases will contain 0 entries by default

C. Exit 
- Terminates the program and exits the program window

1. View Collection
- Prints the database for the user to view in an organized manner

2. Edit Database 
   a. Add Entry
   	- Allows the user to add an entry into the database 
	- Max 21 characters per category in the entry
	- Blank input sets category in the entry to "N/A"
   b. Delete Entry
   	- Deletes a specific entry from the database	
   c. Change Entry
   	- Allows the user to change a single entry of choice in the database
	- Max 21 characters per category in the entry
	- Blank input sets category in the entry to "N/A"

3. Search for Entry
- Allows the user to search for a value under any of the five categories
- Exact value of category entry must be entered for the search to work
- All the results are printed in a nice table for the user

4. Sort by Category
- Allows the user to sort the database by any of the five categories
- The user may choose to sort in ascending or descending order
- The sorted database is displayed for the user to view

Sort Order: 
The first priority for sorting is always the category the user chooses. With duplicate values in the first category, the entries will be sorted according to the smallest category number that is not the category of first priority, then the next smallest category number that is not the category of first priority. 

Example:\
User chooses to sort with category 3 as the highest priority:\
Sort Priority 1: Category 3\
Sort Priority 2: Category 1\
Sort Priority 3: Category 2\
Sort Priority 4: Category 4\
Sort Priority 5: Category 5

5. Creates Backup
- Creates a new .ssf file named "Backup X of [database name]"
- The contents of the backup file will be identical to the contents of the current database file and the password will the same as the original file's password

6. Change Database Name/Password
- If the user wishes, they may change the name or password of the current database
- The name must not overlap with an existing database name
- The password must still be min. 5 characters long
- The user will be booted out of the database and returned to the program menu after a successful credential change. The user may then log onto their database with the new name/password.

7. Empty Database
- Clears all the entries from the current database

8. Delete Database
- Deletes the current database for good
- No backsies when the process is completed

e. Save and Exit Current Database
- Saves all the changes that have been made to the entries in the database and returns the user back to the program menu

E. Save and Exit Program
- Saves all the changes that have been made to the entries in the database and terminates the program window



~ Database Format ~

Databases are saved as .ssf files (Super Secret File), which are plaintext files with a custom extension. For every database file, the first five lines are reserved for the category titles. Each set of five lines following the category titles represent each entry in the database. Max size of each database is 99 entries.

Example: 

Name <-- Category title 1\
Age <-- Category title 2\
Grade <-- Category title 3\
School <-- Category title 4\
ID Number <-- Category title 5\
Davis <-- Name of entry 1\
16 <-- Age of entry 1\
11 <-- Grade of entry 1\
MDHS<-- School of entry 1\
340986892 <-- ID of entry 1\
Sally <-- Name of entry 2\
15 <-- Age of entry 2\
10 <-- Grade of entry 2\
MDHS <-- School of entry 2\
078922903 <-- ID of entry 2\
Dylan <-- Name of entry 3\
18 <-- Age of entry 2\
12 <-- Grade of entry 3\
Markville S.S. <-- School of entry 3\
078229001 <-- ID of entry 3\

~ namespasswords.ssf Format ~

All the existing database names and their passwords are stored and read from the file namespasswords.ssf. The file is strictly used for storing the names and passwords of databases managed by this program. Maximum number of saved databases is 50;

Example:

potato <-- DB name\
pommedeterre <-- DB password\
spaghetti <-- DB name\
meatballs <-- DB password\
dog <-- DB name\
woofwoof <-- DB password

The project is best run from IntelliJ. It is loaded into the program by opening IntelliJ and navigating to *File* -> *New* -> *Project from Existing Sources...*, selecting the *Project2* directory, and following the prompts. (You may have to select a JDK to use; the current version on CSE machines is Java 8 Update 151 as of 11/13/18.)

It can then be run by right-clicking on the *Driver* class contained in the *src* directory and clicking on the *Run 'Driver.main()'* option.

You will then be greeted with a terminal that requests an input file. Input a full path to the input file in order to read it in.

You will then be asked for an output directory. Provide the full path to an output directory.  The audit file will be automatically generated.

(The *lib* directory is included only because, without it, IntelliJ requires junit4.2 to be manually added to the classpath in order to compile.)

Another way to run the project is through the command line. In order to do this you must be in the src directory. Here are the following commands to compile the code and run it.

javac voting_system/Driver.java

java voting_system.Driver

There are 3 ways to run this program
1. Provide a file in the command line argument
2. Provide no command line arguments and input file through text prompts
3. Use a GUI to input the election File

Example providing command line argument

java voting_system.Driver ../testing/testIRVInput.csv

Would you like to use the command line or a GUI?
Type one: cmd, gui
cmd

Please type the path to your output directory:
Input file taken from command line argument

Please provide the path of the directory you'd like your audit file to be placed in:
../testing/

The program will be ran and display the table for the IRV election

Example just using the text prompt

java voting_system.Driver

Would you like to use the command line or a GUI?
Type one: cmd, gui
cmd

Please type the path to your input file:

../testing/testIRVInput.csv

Please type the path to your output directory:

../testing/

This will run the election with this file and output the audit file into the testing directory.

Example using the GUI

java voting_system.Driver

Would you like to use the command line or a GUI?
Type one: cmd, gui
gui

Type fe for file explorer or sp to type in a path:
fe

A GUI will be displayed where you can select your election File
Program will run and produce output file.

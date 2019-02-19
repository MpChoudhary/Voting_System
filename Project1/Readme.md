The project is best run from IntelliJ. It is loaded into the program by opening IntelliJ and navigating to *File* -> *New* -> *Project from Existing Sources...*, selecting the *Project1* directory, and following the prompts. (You may have to select a JDK to use; the current version on CSE machines is Java 8 Update 151 as of 11/13/18.)

It can then be run by right-clicking on the *Driver* class contained in the *src* directory and clicking on the *Run 'Driver.main()'* option.

You will then be greeted with a terminal that requests an input file. Input a full path to the input file in order to read it in.

You will then be asked for an output directory. Provide the full path to an output directory.  The audit file will be automatically generated.

(The *lib* directory is included only because, without it, IntelliJ requires junit4.2 to be manually added to the classpath in order to compile.)

Another way to run the project is through the command line. In order to do this you must be in the src directory. Here are the following commands to compile the code and run it.

javac voting_system/Driver.java

java voting_system.Driver

A Prompt will pop up asking for an input file and ask for an output directory for the audit file. Here is an example running it with testIRVInput.csv in the testing directory.

Please type the path to your input file:

../testing/testIRVInput.csv

Please type the path to your output directory:

../testing/

This will run the election with this file and output the audit file into the testing directory.

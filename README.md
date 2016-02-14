# Cell Society
Duke CompSci 308 Cell Society Project

**Names:** Blake Kaplan, Austin Wu, Amy Zhao

**Date Started:** 1/30/16

**Date Finished:** 2/13/16

**Team Roles:**

* Blake Kaplan - XML Generation and Parsing
* Austin Wu - User Interface and Graphics
* Amy Zhao - Game Rules and Functionality

**Resources Used:**

* StackOverflow for miscellaneous questions and errors
* Oracle Java documentation for general information
* Tutorials Point for help with DOM XML Generation and Parsing

**Starting the Project:**

To start the simulation, run Main.java, load a file in the GUI, and press start or step through with the step button.

Adjust the appearance to your liking using the config button.

Use the built in file generator to create your own XML file with your preferred abundance percentages of cell states.

XML generation can also be done directly from the XMLGenerator Class. You must provide the `public void generateFile(int row, int col, String rules, String fileName, String gridType)` method with the necessary information and pass a map with your desired state abundances into the XMLGenerator constructor.


**Testing the Project:**

We have included pregenerated test files in our data folder

* Game of Life
  * GameOfLifeSt30.xml
  * GameOfLifeTor30.xml
  * GameOfLifeSt80.xml
  * GameOfLifeTor80.xml
  * GliderSt20.xml
  * GliderTor20.xml (Good example of Toroidal Grid)
  * GameOfLifeInfTest.xml

* Fire
  * FireSt30.xml
  * FireTor30.xml
  * FireSt80.xml
  * FireTor80.xml
  * FireSt30BadParams.xml (Demonstrates Improper Parameters Error Checking)
  * FireSt30OutOfBounds.xml (Demonstrates Cell Out of Bounds Error Checking)
  * FireInfTest.xml

* Predator Prey
  * PredatorPreySt30.xml
  * PredatorPreyTor30.xml
  * PredatorPreySt80.xml
  * PredatorPreyTor80.xml
  * PredatorPreyInfTest.xml

* Segregation
  * SegregationSt30.xml
  * SegregationTor30.xml
  * SegregationSt80.xml
  * SegregationTor80.xml
  * SegregationInfTest.xml

* Foraging Ants
  * ForagingAnts2.xml
  * ForagingAnts3.xml
  * ForagingAntsSt7.xml
  * ForagingAntsTor7.xml
  * ForagingAntsInf7.xml

* Sugar Scape

* Other
  * WrongFileType.txt (Demonstrates Wrong File Type Error Checking)

**Resource Files Required:**
* Controller.properties
* ErrorMessages.properties
* View.properties
* Grid.properties
* Rules.properties
* FireRules.properties
* Controller.properties
* GameOfLife.properties
* SugarScapeRules.properties
* SegregationRules.properties
* ForagingAntsRules.properties
* PredatorPreyRules.properties
* SugarScapeMigrationRules.properties
* SugarScapeReproductionRules.properties

**Interesting Information:** None

**Known Bugs:** File generation for ForagingAnts requires non-automated editing to add a home cell

**Extra Features:**
* Toroidal Grid
* Infinite Grid
* XML Error Checking
* XML File Generation from GUI
* Configure Simulation Appearance from GUI
* Weighted States XML File Generation
* Configure a Cell's State and Characteristics Live from GUI

**Impressions:** We found this assignment to be reasonable. At the beginning of each sprint, we clearly defined each team member's responsibilities and stayed on track. We were always communicating about pull requests and bugs that we were facing. This made the overall project experience much more enjoyable. We are very satisfied with how our project came out.

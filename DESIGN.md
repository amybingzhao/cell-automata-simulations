CS308 Cell Society - Team 21 Design
===================

### Introduction

* Problem: Trying to simulate different cell automata models
* Primary Design Goals: Should be able to accommodate different types of simulations
* Primary Architecture:
    - Closed: Rules for simulations, states for specific simulations
    -  Open: Which simulations it supports, UI elements

### Overview

![UML](images/UML.jpg)

### User Interface
![Mockup](images/ui_mockup.jpeg)
(Sorry about the orientation)
* The user interface will have five buttons along the bottom of the screen:
    * Start
    * Stop
    * Step
    * Speed Up
    * Slow Down
    * Load XML
* The user interface will also have each cell displayed as a grid above the row of buttons. The grid will have a fixed total size but fill itself with evenly sized and spaced cells.
* The XML load button will allow us to load an xml file describing the initial state of a game, which set of rules we are using, and then prepares the game to be run once "start" or "step" is pressed.
* Whenever the XML load button is pressed, it will stop the game having the same effect as the stop button, and then load a new XML. The user interface will also say at all times what is the current game and set of rules loaded.

### Design Details
**Use Cases**

1. Apply the rules to a middle cell:
  * `Grid.get(row,col)` to get the cell
  * `Grid.getNeighbors(Cell myCell)`
  * Check the cell against the rule
  * If the cell needs to be updated, add to list in Grid
  * Apply `Grid.updateState(Cell myCell)` to all cells in the list
  * Update the cell's state if necessary

2. Apply the rules to an edge cell
  * `Grid.get(row,col)` to get the cell
  * `Grid.getNeighbors(Cell myCell)` (The logic in getNeighbors will implement bounds checking)
  * Check the cell against the rule
  * If the cell needs to be updated, add to list in Grid
  * Apply `Grid.updateState(Cell myCell)` to all cells in the list
  * Update the cell's state if necessary

3. Move to the next generation
  * `Simulation.step()` (The simulation loop)
  * Calls `Grid.updateState()` which loops through the list of cells that need to be updated and updates their states
  * Calls `Grid.render()` which returns a UI group that contains all the nodes to be presented in the UI

4. Set a simulation parameter
  * Simulation passes file name to XML parser
  * XML parser parses and identifies game type and parameters
  * The parser returns a Rule and State object corresponding to the simulation
    - E.g. `FireRules(probCatch)`

5. Switch Simulations
  * Stop the simulation loop
  * Call `Simulation.init()` again, which will repeat the initial loading sequence

### Design Considerations
* When discussing design, we considered the following different design decisions:
    * We spent a lot of time trying to decide how to encapsulate the information of the rules and represent it. We ultimately decided to create a rule and state subclass for each set of rules. Each rule subclass would hold the logic for that game, and each state subclass would the possible states for that set of rules.
    * We decided to track cell location through a grid class, and track cell state as two String instance variables in the Cell class, one as the current state and one as the future state, as well as a list of cell "to be modified" in the Grid class.
    * We decided that loading a new XML file would stop the current game's execution, populate the Grid, Rule, and State instance variables in the Simulation class to the new information.

### Team Responsibilities
* Amy: Basic skeleton/interface of Rule, State, Grid, Cell
* Blake: XML parser and writer
* Austin: UI and button functionality- Simulation class

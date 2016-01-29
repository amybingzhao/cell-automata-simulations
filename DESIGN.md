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

### Team Responsibilities

# Pair Refactoring
## NetIDs: atw15 and jcj26

We chose to refactor the respondToMouse method in the CSView class because it was overly long and complicated and involved creating a UI dialog box element. This piece of functionality should and now does exist on its own.

I created the new method createStatePicker() that creates a dialog box to pick the new state.

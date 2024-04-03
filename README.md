# CPSC 210 Project

## Game Party Finder and Tracker

A *bulleted* list:
- Members of a group have *roles* that indicate which games they play. 
  - If someone wants to play a *certain multiplayer game*, it sends a status request to the people who have roles in that game
  - This application will help users **find game parties** in a 'chat room' like environment and **track win-rates** with players
- People who have a *group of friends* that play multiplayer games will use this to conveniently find parties without having to go through extensive communication to do so
- This project is of interest to me due to my own personal experience of having very convoluted and confusing conversations of who wants to play a certain game. Even when implementing a callout system it was still hard to reach people and confirm attendance.

## User Stories
- As a user, I want to be able to assign myself any number of games
- As a user, I want to be able to create a new game party
- As a user, I want to be able to see a list of who is in a game party
- As a user, I want to be able to be able to reduce the required size of the game party
- As a user, I want to be able to load previous states (gathered enough people or not) of game parties, win rates of players, existing players and games, if I choose to
- As a user, I want to be able to save current game parties, win rates from completed parties, existing players and games, if I choose to

## Instructions for Grader
- You can generate new roles (games) in a person by clicking menu item "Add Role"
- You can delete roles from a given person by clicking menu item "Delete Role"
- You can locate my visual component by first loading my application and seeing a graphical video game icon
- You can save the state of my application by clicking the save button
- You can reload the state of my application by clicking the load button

## Phase 4: Task 2
Person, aaron added to Game Party Finder.
Viewed roles of aaron
Person, nic added to Game Party Finder.
Viewed roles of aaron
Viewed roles of nic
Game, league added to Game Party Finder.
Viewed roles of aaron
Viewed roles of nic
Game, valorant added to Game Party Finder.
Viewed roles of aaron
Viewed roles of nic
league has been added as a role to aaron
Viewed roles of aaron
Viewed roles of nic
valorant has been added as a role to aaron
Viewed roles of aaron
Viewed roles of nic
valorant has been added as a role to nic
Viewed roles of aaron
Viewed roles of nic
valorant role has been deleted from aaron
Viewed roles of aaron
Viewed roles of nic

## Phase 4: Task 3
I would refactor the JSON Array returning objects where I'm converting different ArrayLists into JSONArrays. (i.e. lists of Person/GameParty/Game)
Since the methods are largely duplicated of looping through the ArrayList and putting them into a new JSONArray. I would do this since it removes duplicity within the code and reduces
propensity for errors.
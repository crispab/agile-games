@1.0
Feature: Game Execution
  The game has an execution phase where each player has three goals to reach, a first
  player and second player to tap on the shoulder and finally return to their starting
  position. The players are assigned their goals during the assignment phase.

  Scenario: A player gets immediately a third target, the starting point
    Given the player is at position 3,1
    Then the player has end goal to reach 3,1

  Scenario: As the facilitator starts the assignment, players gets two other players as goal
    Given a game is in phase "gathering"
    And the player is at position 1,1 named "Jill"
    And a player named "Caren"
    And a player named "Betty"
    When the facilitator starts the assignments
    Then player named "Jill" has two goals "Caren" and "Betty"
    And player named "Caren" has two goals "Jill" and "Betty"
    And player named "Betty" has two goals "Caren" and "Jill"

  Scenario: When a player moves close to their first goal player, the first goal is reached.
    Given a game is in phase "assignment"
    And the player is at position 1,1 named "P"
    And a player named "A"
    And a player named "B"
    And the board looks as:
    And 0 "| | | | | |"
    And 1 "| |P| |A| |"
    And 2 "| | | | |B|"
    And player named "P" is assigned the two goals "A" and "B"
    And all players has done their estimations
    When the facilitator starts the game
    And the player moves in direction "right"
    Then the board should look like:
    And 0 "| | | | | |"
    And 1 "| | |P|A| |"
    And 2 "| | | | |B|"
    Then the player has reached the first goal

  Scenario: As a player has done the first goal and is next to their second goal, that goal is accomplished.
    Given a game is in phase "gathering"
    And the player is at position 3,2 named "P"
    And a player named "C"
    And a player named "D"
    And the board looks as:
    And 0 "| | | |C| |"
    And 1 "| | | | |D|"
    And 2 "| | | |P| |"
    When the facilitator starts the assignments
    Then player named "P" has two goals "C" and "D"
    When all players has done their estimations
    And the facilitator starts the game
    And the player moves in direction "up"
    Then the player has reached the first goal
    And the player has reached the second goal
    And the board should look like:
    And 0 "| | | |C| |"
    And 1 "| | | |P|D|"
    And 2 "| | | | | |"

  Scenario: When a player has done their two goals and returned to the starting position, they are done.
    Given a game is in phase "gathering"
    And the player is at position 3,2 named "P"
    And a player named "A"
    And a player named "B"
    And the board looks as:
    And 0 "| | | |A| |"
    And 1 "| | | | |B|"
    And 2 "| | | |P| |"
    When the facilitator starts the assignments
    Then player named "P" has two goals "A" and "B"
    When all players has done their estimations
    And the facilitator starts the game
    And the player moves in direction "up"
    And the player moves in direction "down"
    Then the player is done
    And the board should look like:
    And 0 "| | | |A| |"
    And 1 "| | | | |B|"
    And 2 "| | | |P| |"
@1.0
Feature: Estimation
  A key part of the games is that players are to estimate the number of steps they will take to reach
  each one of their goal. Not until everyone has done estimation, the execution can start. When the
  players moves during execution, their steps are counted.

  Scenario: Game execution waits for estimation
    Given a game is in phase "estimation"
    And a player named "Alice"
    But "Alice" has not estimated all their goals
    When the facilitator starts the game
    Then the game is in phase "estimation"

  Scenario: When players move, their steps are counted
    Given a game is in phase "estimation"
    And the player is at position 1,1 named "P"
    And a player named "A"
    And a player named "B"
    And the board looks as:
    And 0 "| | | | | |"
    And 1 "| |P| |A| |"
    And 2 "| | | | |B|"
    And player named "P" is assigned the two goals "A" and "B"
    And all players have done their estimations
    When the facilitator starts the game
    And the player moves in direction "right"
    Then the player has reached the first goal
    And the player's steps for the first goal are 1
    When the player moves in direction "down"
    And the player moves in direction "right"
    Then the player has reached the second goal
    And the player's steps for the second goal are 2
    When the player moves in direction "left" 2 times
    And the player moves in direction "up"
    Then the board should look like:
    And 0 "| | | | | |"
    And 1 "| |P| |A| |"
    And 2 "| | | | |B|"
    Then the player has reached the end goal
    And the player's steps for the end goal are 3
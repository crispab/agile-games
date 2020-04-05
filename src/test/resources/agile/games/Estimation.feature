@1.0
Feature: Estimation
  A key part of the games is that players are to estimate the number of steps they will take to reach
  each one of their goal. Not until everyone has done estimation, the execution can start. When the
  players moves during execution, their steps are counted.

  Scenario: Game execution waits for estimation
    Given a game is in phase "assignment"
    And a player named "Alice"
    But "Alice" has not estimated all their goals
    When the facilitator starts the game
    Then the game is in phase "assignment"


  Scenario: When players move, their steps are counted
    Given a game is in phase "gathering"
    And the player is at position 1,1
    And a player named "Alice"
    And the facilitator starts the assignments
    When all players has done their estimations
    And the facilitator starts the game
    And the player moves in direction "up"
    Then the player's steps for the first goal are 1
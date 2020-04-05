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

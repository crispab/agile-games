Feature: Game Execution of Tap the Shoulder
  The game has an execution phase where each player has three objectives to reach, a first
  player and second player to tap on the shoulder and finally return to their starting position.

  Scenario: A player gets immediately a third target, the starting point
    Given a player at position 3,8
    And the player moves in direction "right"
    Then the player has end goal to reach 3,8

  Scenario: When the facilitator starts the assignment, players gets two other players as goal
    Given a player at position 5,5 named "Jill"
    And another player at position 6,6 named "Caren"
    And another player at position 7,7 named "Betty"
    When the facilitator starts the assignments
    Then player named "Jill" has two goals "Caren" and "Betty"
    And player named "Caren" has two goals "Jill" and "Betty"
    And player named "Betty" has two goals "Caren" and "Jill"
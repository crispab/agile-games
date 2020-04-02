@1.0
Feature: Game Flow
  The game has the following phases: gathering, assignment, executing and reporting. A game
  is created when someone chose to facilitate. The game starts in phase gathering.
  During the gathering, the game waits for the players to gather.

  The facilitator decides when enough have gathered by initiating assignment.
  where the game assigns each player two other players as their goals.

  On the initiative of the facilitator, the execution phase is started.
  The execution phase is when the players try to reach their targets and return to
  their starting position.

  When all players are done, the game automatically enters the reporting phase
  and the summary report is available.

  Scenario: Facilitator creates a new game
    Given a user
    When the user chose to facilitate a game
    Then the user gets the role "facilitator"
    And the game is in phase "gathering"

  Scenario: Player joins a game
    Given a game is in phase "gathering"
    And a user
    When the user chose to join the game
    Then the user gets the role "player"

  Scenario: Facilitator starts the assignment
    Given a game is in phase "gathering"
    When the facilitator starts the assignments
    Then the game is in phase "assignment"

  Scenario: A game in phase assignment is started
    Given a game is in phase "assignment"
    When the facilitator starts the game
    Then the game is in phase "executing"

  Scenario: Players are all done
    Given a game is in phase "executing"
    And there are 10 players in the game
    When all players of the game has reached their end goal
    Then the game is in phase "reporting"

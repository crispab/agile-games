Feature: Game Flow for Tap the Shoulder
  The game has the following phases: gathering, assignment, executing and reporting.
  During the gathering, the game waits for the players to gather. When enough have gathered,
  the assignment takes place where each player is assigned two other players as their targets.
  The execution phase begins next and the players try to reach their targets and return to
  their starting position. When all are done, the reporting phase is the end where the summary
  is available

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

Feature: Player's Goals State
  A player has three goals. Reach two other players and return to their starting position. These goals have
  four states each: no goal set, assigned, estimated and accomplished.

  A goal state could be visualised as a bar with three slots that are filled depending on how far the player
  has come. OOO = no goal set, XOO = assigned, XXO = estimated and XXX = accomplished.

  This gives a quick overview over each players progress.

  Scenario: Player is assigned a first goal
    Given a game is in phase "gathering"
    And a player named "Alice"
    And a player named "Bertram"
    And a player named "Cecil"
    And the player named "Alice" first goal is in state "no goal set"
    When the facilitator starts the estimations
    Then the player named "Alice" first goal is in state "assigned"
    And the player named "Alice" second goal is in state "assigned"
    And the player named "Alice" end goal is in state "assigned"

  Scenario: Player has estimated their goals
    Given a game is in phase "estimation"
    And a player named "Bertram"
    When "Bertram" has done their estimation
    Then the player named "Bertram" first goal is in state "estimated"
    And the player named "Bertram" second goal is in state "estimated"
    And the player named "Bertram" end goal is in state "estimated"

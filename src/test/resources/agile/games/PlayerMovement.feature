@1.0
Feature: Player movement rules
  A game that is on phase execution allows the player to move across the board.
  The player can not move off the board or move to a square that is occupied by
  another player.

  Scenario: Player move within the board
    Given a board with dimensions 10,10
    And a player at position 5,5
    When the player moves in direction "up"
    Then the player is on position 5,4

  Scenario: Player at the edge of the board
    Given a board with dimensions 10,10
    And a player at position 5,9
    When the player moves in direction "down"
    Then the player is on position 5,9

  Scenario: Player can't move to an occupied square
    Given a board with dimensions 10,10
    And a player at position 5,8
    And another player at position 5,9
    When the player moves in direction "down"
    Then the player is on position 5,8


module PlayerDecodeTest exposing (all)

import Expect
import Json.Decode exposing (decodeString)
import Message exposing (Player, PlayerEndGoal, PlayerGoal, PlayerGoalState(..), PlayerGoals, PlayerTapGoal, decodeGoals, decodePlayer, decodePlayerEndGoal, decodePlayerGoal, decodePlayerTapGoal)
import Test exposing (..)


all : Test
all =
    describe "Test of Player decoding."
        [ test "Player Goal" <|
            \_ ->
                decodeString decodePlayerGoal givenPlayerGoal
                    |> Expect.equal expectedPlayerGoal
        , test "Player tap goal" <|
            \_ ->
                decodeString decodePlayerTapGoal givenPlayerTapGoal
                    |> Expect.equal expectedPlayerTapGoal
        , test "Player end goal" <|
            \_ ->
                decodeString decodePlayerEndGoal givenPlayerEndGoal
                    |> Expect.equal expectedPlayerEndGoal
        , test "Player goals" <|
            \_ ->
                decodeString decodeGoals givenPlayerGoals
                    |> Expect.equal (Ok <| expectedPlayerGoals)
        , test "Player" <|
            \_ ->
                decodeString decodePlayer givenPlayer
                    |> Expect.equal (Ok <| expectedPlayer)
        ]


givenPlayerGoal : String
givenPlayerGoal =
    """
    {
      "state": "ASSIGNED",
      "estimation": 0,
      "steps": 0
    }
    """


expectedPlayerGoal : Result e PlayerGoal
expectedPlayerGoal =
    Ok (PlayerGoal ASSIGNED 0 0)


givenPlayerTapGoal : String
givenPlayerTapGoal =
    """
    {
      "targetPlayerId": "some id2",
      "goal": {
        "state": "ESTIMATED",
        "estimation": 10,
        "steps": 0
      }
    }
    """


expectedPlayerTapGoal : Result e PlayerTapGoal
expectedPlayerTapGoal =
    Ok (PlayerTapGoal "some id2" (PlayerGoal ESTIMATED 10 0))


givenPlayerEndGoal : String
givenPlayerEndGoal =
    """
    {
      "targetX": 5,
      "targetY": 4,
      "goal": {
        "state": "ESTIMATED",
        "estimation": 10,
        "steps": 0
      }
    }
    """


expectedPlayerEndGoal : Result e PlayerEndGoal
expectedPlayerEndGoal =
    Ok (PlayerEndGoal 5 4 (PlayerGoal ESTIMATED 10 0))


givenPlayerGoals : String
givenPlayerGoals =
    """
           {
            "goal1": {
              "goal": {
                "state": "NO_GOAL_SET",
                "estimation": 0,
                "steps": 0
              }
            },
            "goal2": {
              "targetPlayerId": "some id2",
              "goal": {
                "state": "ESTIMATED",
                "estimation": 10,
                "steps": 0
              }
            },
            "endGoal": {
              "targetX": 5,
              "targetY": 3,
              "goal": {
                "state": "ASSIGNED",
                "estimation": 0,
                "steps": 0
              }
            }
          }    
    """


expectedPlayerGoals : PlayerGoals
expectedPlayerGoals =
    { goal1 = PlayerTapGoal "" (PlayerGoal NO_GOAL_SET 0 0)
    , goal2 = PlayerTapGoal "some id2" (PlayerGoal ESTIMATED 10 0)
    , endGoal = PlayerEndGoal 5 3 (PlayerGoal ASSIGNED 0 0)
    }


givenPlayer : String
givenPlayer =
    """
    {
      "id": "some id1",
      "name": "Alice",
      "avatar": "aliceAvatar.png",
      "goals": {
        "goal1": {
          "goal": {
            "state": "NO_GOAL_SET",
            "estimation": 0,
            "steps": 0
          }
        },
        "goal2": {
          "targetPlayerId": "some id2",
          "goal": {
            "state": "ESTIMATED",
            "estimation": 10,
            "steps": 0
          }
        },
        "endGoal": {
          "targetX": 5,
          "targetY": 3,
          "goal": {
            "state": "ASSIGNED",
            "estimation": 0,
            "steps": 0
          }
        }
      }
    }
    """


expectedPlayer : Player
expectedPlayer =
    Player "some id1" "Alice" "aliceAvatar.png" expectedPlayerGoals

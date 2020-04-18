module MessageTest exposing (all)

import Dict exposing (Dict)
import Expect
import Json.Decode exposing (decodeString)
import Message exposing (GamePhase(..), GameState, Message(..), Player, PlayerGoal, PlayerGoalState(..), PlayerGoals, PlayerRef, Square, decodeMessage, gameStateMessageDecoder)
import Test exposing (..)


all : Test
all =
    describe "Test of message Json decoding."
        [ test "Session start" <|
            \_ ->
                decodeMessage givenSessionStart
                    |> Expect.equal expectedSessionStart
        , test "Joined" <|
            \_ ->
                decodeMessage givenJoined
                    |> Expect.equal expectedJoined
        , test "Facilitate" <|
            \_ ->
                decodeMessage givenFacilitate
                    |> Expect.equal expectedFacilitate
        , test "Left" <|
            \_ ->
                decodeMessage givenLeft
                    |> Expect.equal expectedLeft
        , test "Initial game state without Players" <|
            \_ ->
                decodeMessage givenGameStateWithNoPlayers
                    |> Expect.equal expectedGameStateWithNoPlayers
        , test "Game state with Players" <|
            \_ ->
                decodeString gameStateMessageDecoder givenGameStateWithPlayers
                    |> Expect.equal (Ok <| expectedGameStateWithPlayers)
        , test "Something failed" <|
            \_ ->
                decodeMessage givenFailMessage
                    |> Expect.equal expectedFailMessage
        , test "It was ok" <|
            \_ ->
                decodeMessage givenOkMessage
                    |> Expect.equal expectedOkMessage
        ]


givenSessionStart =
    """
      {
        "sessionStart" : {
          "userSessionId": "9a6359fe-d1b8-42a8-85f6-6e333cef8a40"
        }
      }
    """


expectedSessionStart : Message.Message
expectedSessionStart =
    SessionStart "9a6359fe-d1b8-42a8-85f6-6e333cef8a40"


givenJoined =
    """
      {
        "joined": {
          "playerId": "some id",
          "playerName": "some name",
          "playerAvatar": "some avatar"
        }
      }
    """


expectedJoined : Message.Message
expectedJoined =
    Joined
        { playerId = "some id"
        , playerName = "some name"
        , playerAvatar = "some avatar"
        }


givenFacilitate =
    """
      {
        "facilitate": {
          "gameSessionCode": "123 456"
        }
      }
      """


expectedFacilitate : Message.Message
expectedFacilitate =
    Facilitate
        { gameSessionCode = "123 456"
        }


givenLeft =
    """
      {
        "left": "123 456"
      }
      """


expectedLeft : Message.Message
expectedLeft =
    Left "123 456"


givenGameStateWithNoPlayers =
    """
      {
        "gameState" : {
          "phase" : "GATHERING",
          "board": [
              [
                {
                }
              ],
              [
                {
                },
                { }
              ]
            ]
        }
      }
    """


expectedGameStateWithNoPlayers : Message
expectedGameStateWithNoPlayers =
    State expectedGameStateInfoNoPlayers


expectedGameStateInfoNoPlayers : GameState
expectedGameStateInfoNoPlayers =
    { players = Dict.fromList []
    , phase = Gathering
    , board =
        [ [ { player = Nothing } ]
        , [ { player = Nothing }
          , { player = Nothing }
          ]
        ]
    }


givenGameStateWithPlayers =
    """
   {
     "gameState": {
       "players": [
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
               "targetX": 0,
               "targetY": 2,
               "goal": {
                 "state": "ASSIGNED",
                 "estimation": 0,
                 "steps": 0
               }
             }
           }
         }
       ],
       "phase": "GATHERING",
       "board": [
         [
           {
             "player": {
               "id" : "alice id",
               "name": "Alice",
               "avatar": "circle.png"
             }
           }
         ],
         [
           {
             "player": null
           }
         ]
       ]
     }
   }
    """


expectedGameStateWithPlayers : Message
expectedGameStateWithPlayers =
    State expectedGameStateInfo


expectedGameStateInfo : GameState
expectedGameStateInfo =
    { players =
        Dict.fromList
            [ ( "some id1", Player "some id1" "Alice" "aliceAvatar.png" playerGoals )
            ]
    , phase = Gathering
    , board =
        [ [ { player = Just (PlayerRef "alice id" "Alice" "circle.png") } ]
        , [ { player = Nothing }
          ]
        ]
    }


playerGoals : PlayerGoals
playerGoals =
    PlayerGoals
        { targetPlayerId = "", goal = { state = NO_GOAL_SET, estimation = 0, steps = 0 } }
        { targetPlayerId = "some id2", goal = { state = ESTIMATED, estimation = 10, steps = 0 } }
        { targetX = 0, targetY = 2, goal = { state = ASSIGNED, estimation = 0, steps = 0 } }


givenFailMessage =
    """
      {
        "fail" : {
          "failMessage" : "This has failed"
        }
      }
    """


expectedFailMessage : Message
expectedFailMessage =
    FailMessage { failMessage = "This has failed" }


givenOkMessage =
    """
      {
        "ok" : {
          "okMessage" : "This has oked"
        }
      }
    """


expectedOkMessage : Message
expectedOkMessage =
    OkMessage { okMessage = "This has oked" }

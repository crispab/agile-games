module MessageTest exposing (all)

import Dict
import Expect
import Message exposing (GamePhase(..), GameState, Message(..), Player, decodeMessage)
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
        , test "Initial game state without Players" <|
            \_ ->
                decodeMessage givenGameStateWithNoPlayers
                    |> Expect.equal expectedGameStateWithNoPlayers
        , test "Game state with Players" <|
            \_ ->
                decodeMessage givenGameStateWithPlayers
                    |> Expect.equal expectedGameStateWithPlayers
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
          "playerName": "some name",
          "playerAvatar": "some avatar"
        }
      }
    """


expectedJoined : Message.Message
expectedJoined =
    Joined
        { playerName = "some name"
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
        "gameState" : {
          "players" : [
               {
                  "id": "some id1",
                  "name": "Alice",
                  "avatar": "aliceAvatar.png"
                },
                {
                  "id": "some id2",
                  "name": "Miriam",
                  "avatar": "miriamAvatar.png"
                },
                {
                  "id": "some id3",
                  "name": "Eve",
                  "avatar": "eveAvatar.png"
                }
          ],
          "phase" : "GATHERING",
          "board": [
              [
                {
                  "player": {
                    "id": "some id1",
                    "name": "Alice",
                    "avatar": "aliceAvatar.png"
                  }
                }
              ],
              [
                {
                  "player": null
                },
                { }
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
            [ ( "some id1", Player "some id1" "Alice" "aliceAvatar.png" )
            , ( "some id2", Player "some id2" "Miriam" "miriamAvatar.png" )
            , ( "some id3", Player "some id3" "Eve" "eveAvatar.png" )
            ]
    , phase = Gathering
    , board =
        [ [ { player = Just (Player "some id1" "Alice" "aliceAvatar.png") } ]
        , [ { player = Nothing }
          , { player = Nothing }
          ]
        ]
    }



{- -}


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

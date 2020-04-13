module Command exposing (facilitate, gotoPhase, join, move, resume)

import Json.Encode as Encode
import Message exposing (GamePhase(..))
import Model exposing (GameSessionCode, UserSessionId, userSessionId2String)
import Msg exposing (Direction(..))


facilitate : UserSessionId String -> Encode.Value
facilitate id =
    Encode.object
        [ ( "commandType", Encode.string "FACILITATE" )
        , ( "userSessionId", Encode.string <| userSessionId2String id )
        ]


join : UserSessionId String -> String -> String -> Encode.Value
join userId gameCode playerName =
    Encode.object
        [ ( "commandType", Encode.string "JOIN" )
        , ( "userSessionId", Encode.string <| userSessionId2String userId )
        , ( "parameters"
          , Encode.object
                [ ( "gameSessionId", Encode.string gameCode )
                , ( "playerName", Encode.string playerName )
                ]
          )
        ]


resume : String -> Encode.Value
resume session =
    Encode.object
        [ ( "commandType", Encode.string "RESUME" )
        , ( "userSessionId", Encode.string session )
        ]


move : UserSessionId String -> Direction -> Encode.Value
move session direction =
    Encode.object
        [ ( "commandType", Encode.string "MOVE" )
        , ( "userSessionId", Encode.string <| userSessionId2String session )
        , ( "parameters"
          , Encode.object
                [ ( "DIRECTION", encodeDirection direction )
                ]
          )
        ]


gotoPhase : GamePhase -> Encode.Value
gotoPhase gamePhase =
    let
        phaseCommand =
            case gamePhase of
                Gathering ->
                    "PHASE_GATHERING"

                Assignment ->
                    "PHASE_ASSIGNMENT"

                Executing ->
                    "PHASE_EXECUTING"

                Reporting ->
                    "PHASE_REPORTING"

                UnknownPhase ->
                    "PHASE_GATHERING"
    in
    Encode.object
        [ ( "commandType", Encode.string phaseCommand )
        ]


encodeDirection : Direction -> Encode.Value
encodeDirection direction =
    Encode.string <|
        case direction of
            Up ->
                "UP"

            Down ->
                "DOWN"

            Left ->
                "LEFT"

            Right ->
                "RIGHT"

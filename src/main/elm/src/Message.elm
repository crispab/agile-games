module Message exposing
    ( GamePhase(..)
    , GameState
    , MessageResponse
    , MessageResponseStatus(..)
    , MessageType(..)
    , decodeMessage
    )

import Dict exposing (Dict)
import Json.Decode exposing (Decoder, decodeString, dict, errorToString, list, map, string, succeed)
import Json.Decode.Pipeline exposing (optional, required)


type alias MessageResponse =
    { status : MessageResponseStatus
    , parameters : Dict String String
    }


type MessageResponseStatus
    = OK MessageType
    | FAIL String
    | STATE GameState


type MessageType
    = SessionStart
    | FacilitateMessage
    | JoinedMessage
    | Unknown


type alias GameState =
    { phase : GamePhase
    , players : List String
    }


type GamePhase
    = Gathering
    | Assignment
    | Executing
    | Reporting
    | UnknownPhase


type alias MessageResponseDto =
    { status : String
    , messageType : String
    , message : String
    , parameters : Dict String String
    }


type alias GameStateMessageDto =
    { gameState : GameState
    }


decodeMessage : String -> MessageResponse
decodeMessage string =
    case decodeString stateDecoder string of
        Ok g ->
            MessageResponse (STATE g.gameState) Dict.empty

        Err _ ->
            case decodeString messageResponseDecoder string of
                Ok m ->
                    dto2Response m

                Err error ->
                    MessageResponse (FAIL (errorToString error)) Dict.empty


messageResponseDecoder : Decoder MessageResponseDto
messageResponseDecoder =
    succeed MessageResponseDto
        |> required "status" string
        |> optional "messageType" string ""
        |> optional "message" string ""
        |> optional "parameters" (dict string) Dict.empty


stateDecoder : Decoder GameStateMessageDto
stateDecoder =
    succeed GameStateMessageDto
        |> required "gameState" gameStateDecoder


gameStateDecoder : Decoder GameState
gameStateDecoder =
    succeed GameState
        |> required "phase" (map stringToPhase string)
        |> required "players" (list string)


stringToPhase : String -> GamePhase
stringToPhase s =
    Dict.fromList
        [ ( "GATHERING", Gathering )
        , ( "ASSIGNMENT", Assignment )
        ]
        |> Dict.get s
        |> Maybe.withDefault UnknownPhase


dto2Response : MessageResponseDto -> MessageResponse
dto2Response dto =
    MessageResponse (stringToStatus dto.status dto.message dto.messageType) dto.parameters


stringToStatus : String -> String -> String -> MessageResponseStatus
stringToStatus status message messageType =
    if status == "OK" then
        OK <| stringToMessageType messageType

    else
        FAIL message


stringToMessageType : String -> MessageType
stringToMessageType messageType =
    Maybe.withDefault Unknown <| Dict.get messageType messageTypesDict


messageTypesDict : Dict String MessageType
messageTypesDict =
    Dict.fromList
        [ ( "SESSION_START", SessionStart )
        , ( "FACILITATE", FacilitateMessage )
        , ( "JOINED", JoinedMessage )
        ]

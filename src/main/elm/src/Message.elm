module Message exposing
    ( GamePhase(..)
    , GameState
    , Message(..)
    , Player
    , Square
    , decodeMessage
    )

import Dict exposing (Dict)
import Json.Decode
    exposing
        ( Decoder
        , andThen
        , decodeString
        , errorToString
        , field
        , list
        , map
        , null
        , nullable
        , oneOf
        , string
        , succeed
        )
import Json.Decode.Pipeline exposing (optional, required)


type Message
    = SessionStart String
    | Joined JoinedInfo
    | Facilitate FacilitateInfo
    | Left String
    | State GameState
    | OkMessage OkInfo
    | FailMessage FailInfo
    | UnknownMessage String


type alias JoinedInfo =
    { playerId : String
    , playerName : String
    , playerAvatar : String
    }


type alias FacilitateInfo =
    { gameSessionCode : String
    }


type alias OkInfo =
    { okMessage : String
    }


type alias FailInfo =
    { failMessage : String
    }


type alias GameState =
    { phase : GamePhase
    , players : Dict String Player
    , board : List (List Square)
    }


type alias Square =
    { player : Maybe Player
    }


type alias Player =
    { id : String
    , name : String
    , avatar : String
    }


type GamePhase
    = Gathering
    | Estimation
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


decodeMessage : String -> Message
decodeMessage s =
    case
        decodeString
            (oneOf
                [ gameStateMessageDecoder
                , sessionStartDecoder
                , okMessageDecoder
                , joinedDecoder
                , facilitateDecoder
                , leftDecoder
                , failMessageDecoder
                ]
            )
            s
    of
        Ok message ->
            message

        Err error ->
            UnknownMessage (errorToString error)


sessionStartDecoder : Decoder Message
sessionStartDecoder =
    field "sessionStart" sessionStartParameter


sessionStartParameter : Decoder Message
sessionStartParameter =
    field "userSessionId" string |> andThen (\s -> succeed (SessionStart s))


joinedDecoder : Decoder Message
joinedDecoder =
    field "joined" joinedInfoDecoder |> andThen (\info -> succeed (Joined info))


joinedInfoDecoder : Decoder JoinedInfo
joinedInfoDecoder =
    succeed JoinedInfo
        |> required "playerId" string
        |> required "playerName" string
        |> required "playerAvatar" string


facilitateDecoder : Decoder Message
facilitateDecoder =
    field "facilitate" facilitateInfoDecoder |> andThen (\info -> succeed (Facilitate info))


facilitateInfoDecoder : Decoder FacilitateInfo
facilitateInfoDecoder =
    succeed FacilitateInfo
        |> required "gameSessionCode" string


leftDecoder : Decoder Message
leftDecoder =
    succeed Left
        |> required "left" string


failMessageDecoder : Decoder Message
failMessageDecoder =
    field "fail" failMessageInfoDecoder |> andThen (\info -> succeed (FailMessage info))


failMessageInfoDecoder : Decoder FailInfo
failMessageInfoDecoder =
    succeed FailInfo
        |> required "failMessage" string


okMessageDecoder : Decoder Message
okMessageDecoder =
    field "ok" okMessageInfoDecoder |> andThen (\info -> succeed (OkMessage info))


okMessageInfoDecoder : Decoder OkInfo
okMessageInfoDecoder =
    succeed OkInfo
        |> required "okMessage" string


gameStateMessageDecoder : Decoder Message
gameStateMessageDecoder =
    field "gameState" gameStateDecoder |> andThen (\info -> succeed (State info))


gameStateDecoder : Decoder GameState
gameStateDecoder =
    succeed GameState
        |> required "phase" (map stringToPhase string)
        |> optional "players" decodePlayerListToDict (Dict.fromList [])
        |> required "board" (list (list squareDecoder))


stringToPhase : String -> GamePhase
stringToPhase s =
    Dict.fromList
        [ ( "GATHERING", Gathering )
        , ( "ESTIMATION", Estimation )
        , ( "EXECUTING", Executing )
        , ( "REPORTING", Reporting )
        ]
        |> Dict.get s
        |> Maybe.withDefault UnknownPhase


squareDecoder : Decoder Square
squareDecoder =
    succeed Square
        |> optional "player" (nullable decodePlayer) Nothing


nullable : Decoder a -> Decoder (Maybe a)
nullable decoder =
    oneOf
        [ null Nothing
        , map Just decoder
        ]


decodePlayerListToDict : Decoder (Dict String Player)
decodePlayerListToDict =
    list decodePlayer |> andThen (\l -> succeed (helper l))


decodePlayer : Decoder Player
decodePlayer =
    succeed Player
        |> required "id" string
        |> required "name" string
        |> required "avatar" string


helper : List Player -> Dict String Player
helper l =
    List.map (\p -> ( p.id, p )) l |> Dict.fromList

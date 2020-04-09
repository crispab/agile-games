module Message exposing (MessageResponse, MessageResponseDto, MessageResponseStatus(..), MessageType(..), decodeMessage, dto2Response, messageResponseDecoder, messageTypesDict, stringToMessageType, stringToStatus)

import Dict exposing (Dict)
import Json.Decode exposing (Decoder, decodeString, dict, errorToString, string, succeed)
import Json.Decode.Pipeline exposing (optional, required)


type MessageResponseStatus
    = OK MessageType
    | FAIL String


type MessageType
    = SessionStart
    | FacilitateMessage
    | JoinedMessage
    | Unknown


type alias MessageResponse =
    { status : MessageResponseStatus
    , parameters : Dict String String
    }


type alias MessageResponseDto =
    { status : String
    , messageType : String
    , message : String
    , parameters : Dict String String
    }


decodeMessage : String -> MessageResponse
decodeMessage string =
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

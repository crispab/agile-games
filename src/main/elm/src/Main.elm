port module Main exposing (MessageResponse, MessageResponseStatus(..), MessageType(..), decodeMessage, main, messageResponseDecoder)

import Bootstrap.Alert as Alert
import Browser
import Command
import Dict exposing (Dict)
import Html exposing (Html)
import Json.Decode
import Json.Decode.Pipeline exposing (optional, required)
import Json.Encode
import Model exposing (Model, Page(..), UserSessionId, gameSessionIdFromString, userSessionIdFromString)
import Msg exposing (Msg(..))
import Page.FacilitatorPage exposing (viewFacilitatorPage)
import Page.LobbyPage exposing (viewLobbyPage)
import Page.PlayerPage exposing (viewPlayerPage)


port websocketIn : (String -> msg) -> Sub msg


port websocketOut : Json.Encode.Value -> Cmd msg


main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


type MessageResponseStatus
    = OK MessageType
    | FAIL String


type MessageType
    = SessionStart
    | FacilitateMessage
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


init : () -> ( Model, Cmd Msg )
init _ =
    ( initialModel, Cmd.none )


initialModel : Model
initialModel =
    { currentPage = LobbyPage
    , code = "x"
    , alertVisibility = Alert.closed
    , userSessionId = userSessionIdFromString ""
    , gameSessionId = gameSessionIdFromString ""
    , errorMessage = ""
    }



-- Update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        WebsocketIn message ->
            let
                decodedMessage =
                    decodeMessage message
            in
            case decodedMessage.status of
                OK messageType ->
                    updateBasedOnType messageType decodedMessage.parameters model

                FAIL errorMessage ->
                    ( { model | alertVisibility = Alert.shown, errorMessage = errorMessage }, Cmd.none )

        ChangeCode code ->
            ( { model | code = code, alertVisibility = Alert.closed }, Cmd.none )

        JoinGame ->
            ( model, websocketOut <| Command.join model.userSessionId )

        DismissAlert _ ->
            ( { model | alertVisibility = Alert.closed }, Cmd.none )

        Facilitate ->
            ( model, websocketOut <| Command.facilitate model.userSessionId )


updateBasedOnType : MessageType -> Dict String String -> Model -> ( Model, Cmd Msg )
updateBasedOnType messageType parameters model =
    let
        alertClosed =
            { model | alertVisibility = Alert.closed }
    in
    case messageType of
        SessionStart ->
            ( { alertClosed
                | userSessionId = userSessionIdFromString <| Maybe.withDefault "" <| Dict.get "USER_SESSION_ID" parameters
              }
            , Cmd.none
            )

        FacilitateMessage ->
            ( { alertClosed
                | gameSessionId = gameSessionIdFromString <| Maybe.withDefault "" <| Dict.get "GAME_SESSION_ID" parameters
                , currentPage = FacilitatorPage
              }
            , Cmd.none
            )

        Unknown ->
            ( alertClosed, Cmd.none )


decodeMessage : String -> MessageResponse
decodeMessage string =
    case Json.Decode.decodeString messageResponseDecoder string of
        Ok m ->
            dto2Response m

        Err error ->
            MessageResponse (FAIL (Json.Decode.errorToString error)) Dict.empty


messageResponseDecoder : Json.Decode.Decoder MessageResponseDto
messageResponseDecoder =
    Json.Decode.succeed MessageResponseDto
        |> required "status" Json.Decode.string
        |> optional "messageType" Json.Decode.string ""
        |> optional "message" Json.Decode.string ""
        |> optional "parameters" (Json.Decode.dict Json.Decode.string) Dict.empty


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
        ]



-- View


view : Model -> Html Msg
view model =
    case model.currentPage of
        LobbyPage ->
            viewLobbyPage model

        FacilitatorPage ->
            viewFacilitatorPage model

        PlayerPage ->
            viewPlayerPage model



-- Subscription


subscriptions : Model -> Sub Msg
subscriptions _ =
    websocketIn WebsocketIn

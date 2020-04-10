port module Main exposing (main)

import Bootstrap.Alert as Alert
import Browser
import Command
import Dict exposing (Dict)
import Html exposing (Html)
import Json.Encode
import Message exposing (GamePhase(..), GameState, MessageResponseStatus(..), MessageType(..), decodeMessage)
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


init : () -> ( Model, Cmd Msg )
init _ =
    ( initialModel, Cmd.none )


initialModel : Model
initialModel =
    { currentPage = LobbyPage
    , code = ""
    , playerName = ""
    , alertVisibility = Alert.closed
    , userSessionId = userSessionIdFromString ""
    , gameSessionId = gameSessionIdFromString ""
    , errorMessage = ""
    , gameState = initialGameState
    }


initialGameState : GameState
initialGameState =
    { phase = Gathering
    , players = []
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

                STATE gameState ->
                    ( { model | gameState = gameState }, Cmd.none )

                FAIL errorMessage ->
                    ( { model | alertVisibility = Alert.shown, errorMessage = errorMessage }, Cmd.none )

        ChangeCode code ->
            ( { model | code = code, alertVisibility = Alert.closed }, Cmd.none )

        ChangePlayerName playerName ->
            ( { model | playerName = playerName, alertVisibility = Alert.closed }, Cmd.none )

        JoinGame ->
            ( model, websocketOut <| Command.join model.userSessionId model.code model.playerName )

        DismissAlert _ ->
            ( { model | alertVisibility = Alert.closed }, Cmd.none )

        Facilitate ->
            ( model, websocketOut <| Command.facilitate model.userSessionId )

        GotoPhase gamePhase ->
            let
                now =
                    model.gameState

                inNewPhase =
                    { now | phase = gamePhase }
            in
            ( { model | gameState = inNewPhase }, Cmd.none )


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

        JoinedMessage ->
            ( { alertClosed
                | currentPage = PlayerPage
              }
            , Cmd.none
            )

        Unknown ->
            ( alertClosed, Cmd.none )



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

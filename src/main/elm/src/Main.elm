port module Main exposing (main)

import Bootstrap.Alert as Alert
import Browser
import Command
import Html exposing (Html)
import Json.Encode
import Message exposing (GamePhase(..), GameState, decodeMessage)
import Model exposing (Model, Page(..), UserSessionId, gameSessionCodeFromString, initialModel, userSessionId2String, userSessionIdFromString)
import Msg exposing (Msg(..))
import Page.FacilitatorPage exposing (viewFacilitatorPage)
import Page.LobbyPage exposing (viewLobbyPage)
import Page.PlayerPage exposing (viewPlayerPage)


port websocketIn : (String -> msg) -> Sub msg


port websocketOut : Json.Encode.Value -> Cmd msg


port setStorage : String -> Cmd msg


main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


init : String -> ( Model, Cmd Msg )
init session =
    ( initialModel session, Cmd.none )



-- Update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        WebsocketIn message ->
            updateBasedOnMessage message model

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
            ( model, websocketOut <| Command.gotoPhase gamePhase )

        Move direction ->
            ( model, websocketOut <| Command.move model.userSessionId direction )

        Leave ->
            ( model, websocketOut <| Command.leave )


updateBasedOnMessage : String -> Model -> ( Model, Cmd Msg )
updateBasedOnMessage messageString model =
    let
        message =
            decodeMessage messageString

        alertClosed =
            { model | alertVisibility = Alert.closed }
    in
    case message of
        Message.SessionStart sessionId ->
            if (String.length <| userSessionId2String model.userSessionId) > 1 then
                ( { alertClosed
                    | userSessionId = userSessionIdFromString ""
                  }
                , websocketOut <| Command.resume <| userSessionId2String model.userSessionId
                )

            else
                ( { alertClosed
                    | userSessionId = userSessionIdFromString <| sessionId
                  }
                , setStorage sessionId
                )

        Message.Joined joinedInfo ->
            ( { alertClosed
                | playerName = joinedInfo.playerName
                , playerAvatar = joinedInfo.playerAvatar
                , currentPage = PlayerPage
              }
            , Cmd.none
            )

        Message.Left code ->
            ( { model
                | alertVisibility = Alert.shown
                , errorMessage = "Left session " ++ code ++ ", but it is still there."
                , currentPage = LobbyPage
              }
            , setStorage ""
            )

        Message.Facilitate facilitateInfo ->
            ( { alertClosed
                | gameSessionCode = gameSessionCodeFromString facilitateInfo.gameSessionCode
                , currentPage = FacilitatorPage
              }
            , Cmd.none
            )

        Message.State gameState ->
            ( { model | gameState = gameState }, Cmd.none )

        Message.UnknownMessage errorMessage ->
            ( { model | errorMessage = errorMessage, alertVisibility = Alert.shown }, Cmd.none )

        Message.OkMessage okInfo ->
            ( { model | errorMessage = okInfo.okMessage, alertVisibility = Alert.shown }, Cmd.none )

        Message.FailMessage failInfo ->
            ( { model | errorMessage = failInfo.failMessage, alertVisibility = Alert.shown }, Cmd.none )



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

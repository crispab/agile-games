port module Main exposing (main)

import Bootstrap.Alert as Alert
import Browser
import Command
import Dict exposing (Dict)
import Html exposing (Html)
import Json.Encode
import Message exposing (GamePhase(..), GameState, MessageResponseStatus(..), MessageType(..), decodeMessage)
import Model exposing (Model, Page(..), UserSessionId, gameSessionIdFromString, initialModel, userSessionId2String, userSessionIdFromString)
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
            ( model, websocketOut <| Command.gotoPhase gamePhase )

        Move direction ->
            ( model, websocketOut <| Command.move model.userSessionId direction )


updateBasedOnType : MessageType -> Dict String String -> Model -> ( Model, Cmd Msg )
updateBasedOnType messageType parameters model =
    let
        alertClosed =
            { model | alertVisibility = Alert.closed }
    in
    case messageType of
        SessionStart ->
            if (String.length <| userSessionId2String model.userSessionId) > 1 then
                ( { alertClosed
                    | userSessionId = userSessionIdFromString ""
                  }
                , websocketOut <| Command.resume <| userSessionId2String model.userSessionId
                )

            else
                let
                    sessionId =
                        Maybe.withDefault "" <| Dict.get "USER_SESSION_ID" parameters
                in
                ( { alertClosed
                    | userSessionId = userSessionIdFromString <| sessionId
                  }
                , setStorage sessionId
                )

        FacilitateMessage ->
            ( { alertClosed
                | gameSessionId = gameSessionIdFromString <| Maybe.withDefault "" <| Dict.get "GAME_SESSION_CODE" parameters
                , currentPage = FacilitatorPage
              }
            , Cmd.none
            )

        JoinedMessage ->
            ( { alertClosed
                | gameSessionId = gameSessionIdFromString <| Maybe.withDefault "" <| Dict.get "GAME_SESSION_CODE" parameters
                , playerAvatar = Maybe.withDefault "?" <| Dict.get "PLAYER_AVATAR" parameters
                , currentPage = PlayerPage
              }
            , Cmd.none
            )

        ResumeMessage ->
            ( { alertClosed
                | playerName = Maybe.withDefault "?" <| Dict.get "PLAYER_NAME" parameters
                , playerAvatar = Maybe.withDefault "?" <| Dict.get "PLAYER_AVATAR" parameters
                , currentPage = pageFromRoomParameter <| Dict.get "ROOM" parameters
                , gameSessionId = gameSessionIdFromString <| Maybe.withDefault "" <| Dict.get "GAME_SESSION_CODE" parameters
              }
            , Cmd.none
            )

        Unknown ->
            ( alertClosed, Cmd.none )


pageFromRoomParameter : Maybe String -> Page
pageFromRoomParameter room =
    if room == Just "Facilitator" then
        FacilitatorPage

    else if room == Just "Player" then
        PlayerPage

    else
        LobbyPage



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

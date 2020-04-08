port module Main exposing (MessageResponse, MessageResponseStatus(..), decodeMessage, main, messageResponseDecoder)

import Array exposing (Array)
import Bootstrap.Alert as Alert
import Bootstrap.Button as Button
import Bootstrap.Card as Card
import Bootstrap.Card.Block as Block
import Bootstrap.Form as Form
import Bootstrap.Form.Input as Input
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col exposing (xs, xs3)
import Browser
import Command
import Dict exposing (Dict)
import Html exposing (Html, div, h1, text)
import Html.Attributes exposing (class, for, style)
import Json.Decode
import Json.Decode.Pipeline exposing (optional, required)
import Json.Encode


port websocketIn : (String -> msg) -> Sub msg


port websocketOut : Json.Encode.Value -> Cmd msg


main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


type Msg
    = WebsocketIn String
    | ChangeCode String
    | JoinGame
    | DismissAlert Alert.Visibility
    | Facilitate


type Page
    = LobbyPage


type alias Model =
    { currentPage : Page
    , code : String
    , alertVisibility : Alert.Visibility
    , errorMessage : String
    }


type MessageResponseStatus
    = OK
    | FAIL String


type alias MessageResponse =
    { status : MessageResponseStatus
    , parameters : Dict String String
    }


type alias MessageResponseDto =
    { status : String
    , message : String
    , parameters : Array KeyValue
    }


type alias KeyValue =
    { key : String
    , value : String
    }


init : () -> ( Model, Cmd Msg )
init _ =
    ( initialModel, Cmd.none )


initialModel : Model
initialModel =
    { currentPage = LobbyPage
    , code = "x"
    , alertVisibility = Alert.closed
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
                OK ->
                    ( { model | alertVisibility = Alert.closed }, Cmd.none )

                FAIL errorMessage ->
                    ( { model | alertVisibility = Alert.shown, errorMessage = errorMessage }, Cmd.none )

        ChangeCode code ->
            ( { model | code = code, alertVisibility = Alert.closed }, Cmd.none )

        JoinGame ->
            ( model, websocketOut Command.join )

        DismissAlert _ ->
            ( { model | alertVisibility = Alert.closed }, Cmd.none )

        Facilitate ->
            ( model, websocketOut Command.facilitate )


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
        |> optional "message" Json.Decode.string ""
        |> optional "parameters" (Json.Decode.array keyValueDecoder) Array.empty


keyValueDecoder : Json.Decode.Decoder KeyValue
keyValueDecoder =
    Json.Decode.succeed KeyValue
        |> required "key" Json.Decode.string
        |> required "value" Json.Decode.string


dto2Response : MessageResponseDto -> MessageResponse
dto2Response dto =
    MessageResponse (stringToStatus dto.status dto.message) (dtoKeyValue2Dict dto.parameters)


dtoKeyValue2Dict : Array KeyValue -> Dict String String
dtoKeyValue2Dict array =
    Array.toList array |> List.map (\kv -> ( kv.key, kv.value )) |> Dict.fromList


stringToStatus : String -> String -> MessageResponseStatus
stringToStatus status message =
    if status == "OK" then
        OK

    else
        FAIL message



-- View


view : Model -> Html Msg
view model =
    case model.currentPage of
        LobbyPage ->
            viewLobby model


viewLobby : Model -> Html Msg
viewLobby model =
    Grid.container []
        [ Grid.row []
            [ Grid.col [ Col.sm ]
                [ viewLobbyHeadline
                , viewJoinOrFacility model
                ]
            ]
        ]


viewLobbyHeadline : Html Msg
viewLobbyHeadline =
    div [ class "jumbotron", style "text-align" "center" ]
        [ h1 [] [ text "Welcome to tap-the-shoulder" ]
        ]


viewJoinOrFacility : Model -> Html Msg
viewJoinOrFacility model =
    Grid.row []
        [ Grid.col []
            [ Card.config [ Card.outlinePrimary ]
                |> Card.headerH4 [] [ text "Join a game" ]
                |> Card.block []
                    [ Block.text [] [ text "Enter the game code to join." ]
                    , Block.custom <| viewAlert model
                    , Block.custom <|
                        viewJoinGameForm model
                    ]
                |> Card.view
            ]
        , Grid.col []
            [ Card.config [ Card.outlineSecondary ]
                |> Card.headerH4 [] [ text "Facilitate a game" ]
                |> Card.block []
                    [ Block.text [] [ text "Create a new game session for other to join." ]
                    , Block.custom <|
                        Button.button
                            [ Button.primary, Button.onClick Facilitate ]
                            [ text "Facilitate" ]
                    ]
                |> Card.view
            ]
        ]


viewAlert : Model -> Html Msg
viewAlert model =
    Alert.config
        |> Alert.danger
        |> Alert.dismissable DismissAlert
        |> Alert.children
            [ text model.errorMessage
            ]
        |> Alert.view model.alertVisibility


viewJoinGameForm : Model -> Html Msg
viewJoinGameForm model =
    Form.form []
        [ Form.group []
            [ Grid.row []
                [ Grid.col [ xs3 ]
                    [ Form.label [ for "code" ] [ text "Enter code:" ] ]
                , Grid.col [ xs ]
                    [ Input.text [ Input.id "code", Input.onInput ChangeCode, Input.value model.code ]
                    ]
                , Grid.col [ xs3 ] [ Button.button [ Button.primary, Button.onClick JoinGame ] [ text "Join" ] ]
                ]
            ]
        ]



-- Subscription


subscriptions : Model -> Sub Msg
subscriptions _ =
    websocketIn WebsocketIn

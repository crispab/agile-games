port module Main exposing (main)

import Bootstrap.Alert as Alert
import Bootstrap.Button as Button
import Bootstrap.Card as Card
import Bootstrap.Card.Block as Block
import Bootstrap.Form as Form
import Bootstrap.Form.Input as Input
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col exposing (xs, xs3)
import Browser
import Html exposing (Html, div, h1, text)
import Html.Attributes exposing (class, for, href, style)


port websocketIn : (String -> msg) -> Sub msg


port websocketOut : String -> Cmd msg


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


type Page
    = LobbyPage


type alias Model =
    { currentPage : Page
    , code : String
    , alertVisibility : Alert.Visibility
    }


init : () -> ( Model, Cmd Msg )
init _ =
    ( initialModel, Cmd.none )


initialModel : Model
initialModel =
    { currentPage = LobbyPage
    , code = "x"
    , alertVisibility = Alert.closed
    }



-- Update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        WebsocketIn message ->
            ( model, Cmd.none )

        ChangeCode code ->
            ( { model | code = code, alertVisibility = Alert.closed }, Cmd.none )

        JoinGame ->
            ( { model | alertVisibility = Alert.shown }, Cmd.none )

        DismissAlert _ ->
            ( { model | alertVisibility = Alert.closed }, Cmd.none )



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
                        Button.linkButton
                            [ Button.primary, Button.attrs [ href "#facilitate" ] ]
                            [ text "Facilitate" ]
                    ]
                |> Card.view
            ]
        ]


viewAlert model =
    Alert.config
        |> Alert.danger
        |> Alert.dismissable DismissAlert
        |> Alert.children
            [ text <| "Found no game with code '" ++ model.code ++ "'!"
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

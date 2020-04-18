module Page.LobbyPage exposing (viewLobbyPage)

import Bootstrap.Button as Button
import Bootstrap.Card as Card
import Bootstrap.Card.Block as Block
import Bootstrap.Form as Form
import Bootstrap.Form.Input as Input
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col exposing (xs, xs3)
import Html exposing (Html, div, h1, text)
import Html.Attributes exposing (class, for, style)
import Model exposing (Model)
import Msg exposing (Msg(..))
import Page.Common exposing (viewAlert)


viewLobbyPage : Model -> Html Msg
viewLobbyPage model =
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
                    [ Block.text [] [ text "Enter your name and the game code to join." ]
                    , Block.custom <| viewAlert model.alertVisibility model.errorMessage
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


viewJoinGameForm : Model -> Html Msg
viewJoinGameForm model =
    Form.form []
        [ Form.group []
            [ Grid.row []
                [ Grid.col [ xs3 ]
                    [ Form.label [ for "playerMame" ] [ text "Enter your player name:" ] ]
                , Grid.col [ xs ]
                    [ Input.text [ Input.id "playerMame", Input.onInput ChangePlayerName, Input.value model.playerName ]
                    ]
                , Grid.col [ xs3 ] []
                ]
            , Grid.row []
                [ Grid.col [ xs3 ]
                    [ Form.label [ for "code" ] [ text "Enter game code:" ] ]
                , Grid.col [ xs ]
                    [ Input.text [ Input.id "code", Input.onInput ChangeCode, Input.value model.code ]
                    ]
                , Grid.col [ xs3 ] [ Button.button [ Button.primary, Button.onClick JoinGame ] [ text "Join" ] ]
                ]
            ]
        ]

module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Button as Button
import Bootstrap.ButtonGroup as ButtonGroup
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Html exposing (Html, div, h1, h4, text)
import Html.Attributes exposing (class, style)
import Message exposing (GamePhase(..), GameState)
import Model exposing (Model)
import Msg exposing (Msg(..))
import Page.Common exposing (playerList)


viewPlayerPage : Model -> Html Msg
viewPlayerPage model =
    Grid.container []
        [ Grid.row []
            [ mainContent model
            , playerList model.gameState.players
            ]
        ]


mainContent : Model -> Grid.Column Msg
mainContent model =
    Grid.col [ Col.sm8 ]
        [ headLine model.code model.playerName
        , phaseRow model.gameState
        ]


headLine : String -> String -> Html Msg
headLine code playerName =
    div [ class "jumbotron", style "text-align" "center" ]
        [ h4 [] [ text code ]
        , h1 [] [ text <| "Player " ++ playerName ]
        ]


phaseRow : GameState -> Html Msg
phaseRow gameState =
    Grid.row []
        [ Grid.col [ Col.sm8 ]
            [ ButtonGroup.buttonGroup []
                [ bGroup gameState Gathering "Gathering"
                , bGroup gameState Assignment "Assignment"
                , bGroup gameState Executing "Executing"
                , bGroup gameState Reporting "Reporting"
                ]
            ]
        ]


bGroup : GameState -> GamePhase -> String -> ButtonGroup.ButtonItem Msg
bGroup gameState phase label =
    if gameState.phase == phase then
        ButtonGroup.button [ Button.primary ] [ text label ]

    else
        ButtonGroup.button [ Button.outlinePrimary, Button.onClick <| GotoPhase phase ] [ text label ]

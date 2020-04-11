module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Button as Button
import Bootstrap.ButtonGroup as ButtonGroup
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Html exposing (Html, div, h1, h4, p, text)
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
        , phaseDependentContent model
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
                , bGroup gameState Assignment "Estimation"
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


phaseDependentContent : Model -> Html Msg
phaseDependentContent model =
    case model.gameState.phase of
        Gathering ->
            gatheringContent model

        Assignment ->
            estimationContent model

        Executing ->
            executingContent model

        Reporting ->
            reportingContent model

        UnknownPhase ->
            div [] [ text "Technical error" ]


gatheringContent : Model -> Html Msg
gatheringContent model =
    Grid.row []
        [ Grid.col [ Col.sm8 ]
            [ h1 [] [ text "Gathering" ]
            , p [] [ text gatheringText ]
            ]
        ]


gatheringText =
    """
    We are gathering players. When enough players have arrived, the facilitator will start the 
    first phase when you are asked to estimate the number of steps it takes to walk across to
    two other players and then back to the starting point, where you are now.
    """


estimationContent : Model -> Html Msg
estimationContent model =
    Grid.row []
        [ Grid.col [ Col.sm8 ]
            [ h1 [] [ text "Estimation" ]
            , p [] [ text estimationText ]
            ]
        ]


estimationText =
    """
    Now you are expected to estimate how many steps you will use to get to your first and second person.
    Fill in the values below.
    When you are ready, press the button.
    """


executingContent : Model -> Html Msg
executingContent model =
    Grid.row []
        [ Grid.col [ Col.sm8 ]
            [ h1 [] [ text "Executing" ]
            , p [] [ text executingText ]
            ]
        ]


executingText =
    """
    Go, Go, Go! Move your avatar by pressing the arrow keys so that you reach your target person.
    """


reportingContent : Model -> Html Msg
reportingContent model =
    Grid.row []
        [ Grid.col [ Col.sm8 ]
            [ h1 [] [ text "Reporting" ]
            , p [] [ text reportingText ]
            ]
        ]


reportingText =
    """
    That was all! Here you can see how it went.
    """

module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Button as Button
import Bootstrap.ButtonGroup as ButtonGroup
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Html exposing (Html, div, h1, h4, img, p, table, td, text, tr)
import Html.Attributes exposing (class, src, style, width)
import Message exposing (GamePhase(..), GameState, Square)
import Model exposing (GameSessionId, Model, gameSessionId2String)
import Msg exposing (Msg(..))
import Page.Common exposing (playerList)


viewPlayerPage : Model -> Html Msg
viewPlayerPage model =
    Grid.container []
        [ Grid.row []
            [ headLine model.gameSessionId model.playerName
            , playerList model.gameState.players
            ]
        , Grid.row []
            [ mainContent model
            ]
        ]


mainContent : Model -> Grid.Column Msg
mainContent model =
    Grid.col [ Col.sm8 ]
        [ phaseRow model.gameState
        , phaseDependentContent model
        ]


headLine : GameSessionId String -> String -> Grid.Column Msg
headLine code playerName =
    Grid.col []
        [ div [ class "jumbotron", style "text-align" "center" ]
            [ h4 [] [ text <| gameSessionId2String code ]
            , h1 [] [ text <| "Player " ++ playerName ]
            ]
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
        [ Grid.col []
            [ h1 [] [ text "Gathering" ]
            , p [] [ text gatheringText ]
            , boardView model
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


boardView : Model -> Html Msg
boardView model =
    table [] (List.map boardRowView model.gameState.board)


boardRowView : List Square -> Html Msg
boardRowView squares =
    tr [] (List.map squareView squares)


squareView : Square -> Html Msg
squareView square =
    td [ style "border" "solid 1px" ]
        [ img [ src <| imgUrl square, width 40 ] []
        ]


imgUrl : Square -> String
imgUrl square =
    case square.player of
        Nothing ->
            "/assets/empty.png"

        Just player ->
            "/assets/avatars/" ++ player.avatar

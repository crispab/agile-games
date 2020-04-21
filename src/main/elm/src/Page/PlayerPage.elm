module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Button as Button
import Bootstrap.Form as Form
import Bootstrap.Form.Input as Input
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col exposing (xs, xs2, xs3)
import Dict
import Html exposing (Html, div, h1, h4, img, p, text)
import Html.Attributes exposing (class, for, src, style, width)
import Message exposing (GamePhase(..), GameState, Player, Square)
import Model exposing (GameSessionCode, Model, gameSessionId2String)
import Msg exposing (Direction(..), Msg(..))
import Page.Common exposing (avatarImg, boardView, imgPrefix, playerList, viewAlert)


viewPlayerPage : Model -> Html Msg
viewPlayerPage model =
    Grid.container []
        [ Grid.row []
            [ headLine model.gameSessionCode model.playerName model.playerAvatar
            , playerList model.gameState.players
            ]
        , Grid.row []
            [ mainContent model
            ]
        ]


mainContent : Model -> Grid.Column Msg
mainContent model =
    Grid.col [ Col.sm8 ]
        [ viewAlert model.alertVisibility model.errorMessage
        , phaseDependentContent model
        ]


headLine : GameSessionCode String -> String -> String -> Grid.Column Msg
headLine code playerName playerAvatar =
    Grid.col []
        [ div [ class "jumbotron", style "text-align" "center" ]
            [ h4 [] [ text <| gameSessionId2String code ]
            , h1 []
                [ text <| "Player " ++ playerName
                , img [ src <| imgPrefix ++ playerAvatar, width 40 ] []
                ]
            , Button.button [ Button.outlineDanger, Button.onClick Leave ] [ text "Leave" ]
            ]
        ]


phaseDependentContent : Model -> Html Msg
phaseDependentContent model =
    case model.gameState.phase of
        Gathering ->
            gatheringContent model

        Estimation ->
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
            , boardView model.gameState.board ( -1, -1 )
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
    let
        maybePlayer =
            Dict.get model.playerId model.gameState.players
    in
    case maybePlayer of
        Nothing ->
            div [] [ text "Found myself I did not" ]

        Just player ->
            Grid.row []
                [ Grid.col [ Col.sm12 ]
                    [ h1 [] [ text "Estimation" ]
                    , p [] [ text estimationText ]
                    , estimationForm model player
                    , boardView model.gameState.board ( -1, -1 )
                    ]
                ]


estimationText =
    """
    Now you are expected to estimate how many steps you will use to get to your first and second person.
    Fill in the values below.
    When you are ready, press the button.
    """


estimationForm : Model -> Player -> Html Msg
estimationForm model player =
    let
        avatar1 =
            Dict.get player.goals.goal1.targetPlayerId model.gameState.players |> Maybe.map .avatar |> Maybe.withDefault ""

        avatar2 =
            Dict.get player.goals.goal2.targetPlayerId model.gameState.players |> Maybe.map .avatar |> Maybe.withDefault ""
    in
    Form.form []
        [ Form.group []
            [ Grid.row []
                [ Grid.col [ xs3 ]
                    [ Form.label [ for "goal1" ] [ avatarImg avatar1 ] ]
                , Grid.col [ xs ]
                    [ Input.text [ Input.id "goal1", Input.onInput EstimateGoal1, Input.value model.estimate1 ]
                    ]
                , Grid.col [ xs3 ] []
                ]
            , Grid.row []
                [ Grid.col [ xs3 ]
                    [ Form.label [ for "goal2" ] [ avatarImg avatar2 ] ]
                , Grid.col [ xs ]
                    [ Input.text [ Input.id "goal2", Input.onInput EstimateGoal2, Input.value model.estimate2 ]
                    ]
                , Grid.col [ xs3 ] []
                ]
            , Grid.row []
                [ Grid.col [ xs3 ]
                    [ Form.label [ for "endGoal" ] [ text "Return to start" ] ]
                , Grid.col [ xs ]
                    [ Input.text [ Input.id "endGoal", Input.onInput EstimateEndGoal, Input.value model.estimateEnd ]
                    ]
                , Grid.col [ xs3 ] [ Button.button [ Button.primary, Button.onClick Estimate ] [ text "Estimate" ] ]
                ]
            ]
        ]


executingContent : Model -> Html Msg
executingContent model =
    let
        myPos =
            case Dict.get model.playerId model.gameState.players of
                Just p ->
                    ( p.goals.endGoal.targetX, p.goals.endGoal.targetY )

                Nothing ->
                    ( -1, -1 )
    in
    Grid.row []
        [ Grid.col [ Col.sm8 ]
            [ h1 [] [ text "Executing" ]
            , p [] [ text executingText ]
            , boardView model.gameState.board myPos
            ]
        , Grid.col [ Col.sm4 ]
            [ arrowKeys
            ]
        ]


arrowKeys : Html Msg
arrowKeys =
    div []
        [ Grid.row []
            [ Grid.col [ xs2 ] []
            , Grid.col [ xs2 ] [ Button.button [ Button.outlinePrimary, Button.onClick (Move Up) ] [ text "U" ] ]
            , Grid.col [ xs2 ] []
            ]
        , Grid.row []
            [ Grid.col [ xs2 ] [ Button.button [ Button.outlinePrimary, Button.onClick (Move Left) ] [ text "L" ] ]
            , Grid.col [ xs2 ] []
            , Grid.col [ xs2 ] [ Button.button [ Button.outlinePrimary, Button.onClick (Move Right) ] [ text "R" ] ]
            ]
        , Grid.row []
            [ Grid.col [ xs2 ] []
            , Grid.col [ xs2 ] [ Button.button [ Button.outlinePrimary, Button.onClick (Move Down) ] [ text "D" ] ]
            , Grid.col [ xs2 ] []
            ]
        ]


executingText =
    """
    Go, Go, Go! Move your avatar by pressing the arrow keys so that you stand next to your target person.
    """


reportingContent : Model -> Html Msg
reportingContent _ =
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

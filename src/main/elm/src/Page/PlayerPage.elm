module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Button as Button
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col exposing (xs2)
import Html exposing (Html, div, h1, h4, img, p, table, td, text, tr)
import Html.Attributes exposing (class, src, style, width)
import Message exposing (GamePhase(..), GameState, Square)
import Model exposing (GameSessionCode, Model, gameSessionId2String)
import Msg exposing (Direction(..), Msg(..))
import Page.Common exposing (playerList)


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
        [ phaseDependentContent model
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
            , boardView model
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
            , boardView model
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


boardView : Model -> Html Msg
boardView model =
    table [] (List.indexedMap boardRowView model.gameState.board)


boardRowView : Int -> List Square -> Html Msg
boardRowView row squares =
    tr [] ([ td [] [ text <| String.fromInt row ] ] ++ List.map squareView squares)


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
            imgPrefix ++ player.avatar


imgPrefix =
    "/assets/avatars/"

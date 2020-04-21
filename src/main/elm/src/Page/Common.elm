module Page.Common exposing (avatarImg, boardView, imgPrefix, playerList, reportingTable, smallAvatar, viewAlert)

import Bootstrap.Alert as Alert
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Bootstrap.ListGroup exposing (li, ul)
import Dict exposing (Dict)
import Html exposing (Html, h4, img, table, tbody, td, text, th, thead, tr)
import Html.Attributes exposing (height, src, style, width)
import Message exposing (Player, PlayerGoalState(..), Square)
import Msg exposing (Msg(..))


playerList : Dict String Player -> Grid.Column msg
playerList players =
    Grid.col [ Col.sm4 ]
        [ h4 [] [ text <| "Players (" ++ (String.fromInt <| Dict.size players) ++ ")" ]
        , viewPlayers players
        ]


viewPlayers : Dict String Player -> Html msg
viewPlayers players =
    Grid.row []
        [ Grid.col []
            [ ul
                (List.map
                    (\player ->
                        li []
                            [ goal1 player
                            , goal2 player
                            , endGoal player
                            , smallAvatar player.avatar
                            , text <| " " ++ player.name
                            ]
                    )
                    (Dict.values players)
                )
            ]
        ]


goal1 : Player -> Html msg
goal1 player =
    img [ src <| goalUrl player.goals.goal1.goal.state, height 14 ] []


goal2 : Player -> Html msg
goal2 player =
    img [ src <| goalUrl player.goals.goal2.goal.state, height 14 ] []


endGoal : Player -> Html msg
endGoal player =
    img [ src <| goalUrl player.goals.endGoal.goal.state, height 14 ] []


goalUrl : PlayerGoalState -> String
goalUrl state =
    let
        n =
            case state of
                NO_GOAL_SET ->
                    1

                ASSIGNED ->
                    2

                ESTIMATED ->
                    3

                COMPLETED ->
                    4

                UNKNOWN_GOAL_STATE ->
                    1
    in
    "/assets/goal" ++ String.fromInt n ++ ".png"


smallAvatar : String -> Html msg
smallAvatar url =
    img [ src <| avatarUrl url, height 14, style "margin-left" "5px" ] []


avatarUrl : String -> String
avatarUrl url =
    "/assets/avatars/" ++ url


boardView : List (List Square) -> ( Int, Int ) -> Html msg
boardView board ( x, y ) =
    table [] (List.indexedMap (boardRowView ( x, y )) board)


boardRowView : ( Int, Int ) -> Int -> List Square -> Html msg
boardRowView ( x, y ) row squares =
    tr [] ([ td [] [ text <| String.fromInt row ] ] ++ List.indexedMap (squareView ( x, y ) row) squares)


squareView : ( Int, Int ) -> Int -> Int -> Square -> Html msg
squareView ( x, y ) row col square =
    if y == row && x == col then
        td [ style "border" "solid 1px" ]
            [ img [ src <| imgUrl square True, width 40 ] []
            ]

    else
        td [ style "border" "solid 1px" ]
            [ img [ src <| imgUrl square False, width 40 ] []
            ]


avatarImg : String -> Html msg
avatarImg str =
    if str == "" then
        img [ src <| "/assets/empty.png", width 40 ] []

    else
        img [ src <| imgPrefix ++ str, width 40 ] []


imgUrl : Square -> Bool -> String
imgUrl square endSquare =
    case square.player of
        Nothing ->
            if endSquare then
                "/assets/endSquare.png"

            else
                "/assets/empty.png"

        Just player ->
            imgPrefix ++ player.avatar


imgPrefix : String
imgPrefix =
    "/assets/avatars/"


viewAlert : Alert.Visibility -> String -> Html Msg
viewAlert alertVisibility errorMessage =
    Alert.config
        |> Alert.danger
        |> Alert.dismissable DismissAlert
        |> Alert.children
            [ text errorMessage
            ]
        |> Alert.view alertVisibility


reportingTable : Dict String Player -> Html msg
reportingTable players =
    table []
        [ thead []
            [ tr []
                [ th [] [ text "Player" ]
                , th [] [ text "Goal 1 E/O" ]
                , th [] [ text "Goal 2 E/O" ]
                , th [] [ text "End goal E/O" ]
                , th [] [ text "Total E/O" ]
                ]
            ]
        , tbody []
            (List.map reportRow <| Dict.values players)
        ]


reportRow : Player -> Html msg
reportRow player =
    let
        g1Estimation =
            player.goals.goal1.goal.estimation

        g2Estimation =
            player.goals.goal2.goal.estimation

        endEstimation =
            player.goals.endGoal.goal.estimation

        totalEstimation =
            g1Estimation + g2Estimation + endEstimation

        g1Steps =
            player.goals.goal1.goal.steps

        g2Steps =
            player.goals.goal2.goal.steps

        endSteps =
            player.goals.endGoal.goal.steps

        totalSteps =
            g1Steps + g2Steps + endSteps
    in
    tr []
        [ td [] [ smallAvatar player.avatar, text <| " " ++ player.name ]
        , td [] [ text <| String.fromInt g1Estimation ++ "/" ++ String.fromInt g1Steps ]
        , td [] [ text <| String.fromInt g2Estimation ++ "/" ++ String.fromInt g2Steps ]
        , td [] [ text <| String.fromInt endEstimation ++ "/" ++ String.fromInt endSteps ]
        , td [] [ text <| String.fromInt totalEstimation ++ "/" ++ String.fromInt totalSteps ]
        ]

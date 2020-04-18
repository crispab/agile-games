module Page.Common exposing (boardView, imgPrefix, playerList)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Bootstrap.ListGroup exposing (li, ul)
import Dict exposing (Dict)
import Html exposing (Html, h4, img, table, td, text, tr)
import Html.Attributes exposing (height, src, style, width)
import Message exposing (Player, PlayerGoalState(..), Square)


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
                            , avatar player.avatar
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


avatar : String -> Html msg
avatar url =
    img [ src <| avatarUrl url, height 14, style "margin-left" "5px" ] []


avatarUrl : String -> String
avatarUrl url =
    "/assets/avatars/" ++ url


boardView : List (List Square) -> Html msg
boardView board =
    table [] (List.indexedMap boardRowView board)


boardRowView : Int -> List Square -> Html msg
boardRowView row squares =
    tr [] ([ td [] [ text <| String.fromInt row ] ] ++ List.map squareView squares)


squareView : Square -> Html msg
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


imgPrefix : String
imgPrefix =
    "/assets/avatars/"

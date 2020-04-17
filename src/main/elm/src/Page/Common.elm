module Page.Common exposing (playerList)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Bootstrap.ListGroup exposing (li, ul)
import Dict exposing (Dict)
import Html exposing (Html, h4, img, text)
import Html.Attributes exposing (height, src, style)
import Message exposing (Player)


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


goal1 _ =
    img [ src <| goalUrl 3, height 14 ] []


goal2 _ =
    img [ src <| goalUrl 2, height 14 ] []


endGoal _ =
    img [ src <| goalUrl 1, height 14 ] []


goalUrl n =
    "/assets/goal" ++ String.fromInt n ++ ".png"


avatar : String -> Html msg
avatar url =
    img [ src <| avatarUrl url, height 14, style "margin-left" "5px" ] []


avatarUrl : String -> String
avatarUrl url =
    "/assets/avatars/" ++ url

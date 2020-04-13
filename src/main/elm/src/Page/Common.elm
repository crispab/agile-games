module Page.Common exposing (playerList, viewPlayerNamesAndAvatars)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Bootstrap.ListGroup exposing (li, ul)
import Html exposing (Html, h4, img, text)
import Html.Attributes exposing (height, src)
import Message exposing (Player)


playerList : List Player -> Grid.Column msg
playerList players =
    Grid.col [ Col.sm4 ]
        [ h4 [] [ text "Players" ]
        , viewPlayerNamesAndAvatars players
        ]


viewPlayerNamesAndAvatars : List Player -> Html msg
viewPlayerNamesAndAvatars players =
    Grid.row []
        [ Grid.col []
            [ ul
                (List.map
                    (\player ->
                        li []
                            [ img [ src <| imgUrl player.avatar, height 14 ] []
                            , text <| " " ++ player.name
                            ]
                    )
                    players
                )
            ]
        ]


imgUrl : String -> String
imgUrl avatar =
    "/assets/avatars/" ++ avatar

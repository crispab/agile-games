module Page.Common exposing (playerList, viewPlayerNames)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Bootstrap.ListGroup exposing (li, ul)
import Html exposing (Html, h4, text)


playerList : List String -> Grid.Column msg
playerList players =
    Grid.col [ Col.sm4 ]
        [ h4 [] [ text "Players" ]
        , viewPlayerNames players
        ]


viewPlayerNames : List String -> Html msg
viewPlayerNames names =
    Grid.row []
        [ Grid.col []
            [ ul
                (List.map (\n -> li [] [ text n ]) names)
            ]
        ]

module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Html exposing (Html, div, h1, text)
import Html.Attributes exposing (class, style)
import Model exposing (Model)
import Msg exposing (Msg(..))


viewPlayerPage : Model -> Html Msg
viewPlayerPage model =
    Grid.container []
        [ Grid.row []
            [ Grid.col [ Col.sm ]
                [ viewHeadLine model.playerName
                ]
            ]
        ]


viewHeadLine : String -> Html Msg
viewHeadLine playerName =
    div [ class "jumbotron", style "text-align" "center" ]
        [ h1 [] [ text <| "Player " ++ playerName ]
        ]

module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Html exposing (Html, div, h1, h4, text)
import Html.Attributes exposing (class, style)
import Model exposing (Model)
import Msg exposing (Msg(..))
import Page.Common exposing (playerList)


viewPlayerPage : Model -> Html Msg
viewPlayerPage model =
    Grid.container []
        [ Grid.row []
            [ Grid.col [ Col.sm8 ]
                [ viewHeadLine model.code model.playerName
                ]
            , playerList model.gameState.players
            ]
        ]


viewHeadLine : String -> String -> Html Msg
viewHeadLine code playerName =
    div [ class "jumbotron", style "text-align" "center" ]
        [ h4 [] [ text code ]
        , h1 [] [ text <| "Player " ++ playerName ]
        ]

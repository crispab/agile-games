module Page.PlayerPage exposing (viewPlayerPage)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Html exposing (Html, div, h1, h4, li, text, ul)
import Html.Attributes exposing (class, style)
import Model exposing (Model)
import Msg exposing (Msg(..))


viewPlayerPage : Model -> Html Msg
viewPlayerPage model =
    Grid.container []
        [ Grid.row []
            [ Grid.col [ Col.sm8 ]
                [ viewHeadLine model.code model.playerName
                ]
            , Grid.col [ Col.sm1 ]
                [ viewPlayerNames model.gameState.players
                ]
            ]
        ]


viewHeadLine : String -> String -> Html Msg
viewHeadLine code playerName =
    div [ class "jumbotron", style "text-align" "center" ]
        [ h4 [] [ text code ]
        , h1 [] [ text <| "Player " ++ playerName ]
        ]


viewPlayerNames : List String -> Html Msg
viewPlayerNames names =
    Grid.row []
        [ Grid.col []
            [ ul []
                (List.map (\n -> li [] [ text n ]) names)
            ]
        ]

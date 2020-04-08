module Page.FacilitatorPage exposing (viewFacilitatorPage)

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Html exposing (Html, div, h1, text)
import Html.Attributes exposing (class, style)
import Model exposing (Model)
import Msg exposing (Msg(..))


viewFacilitatorPage : Model -> Html Msg
viewFacilitatorPage model =
    Grid.container []
        [ Grid.row []
            [ Grid.col [ Col.sm ]
                [ viewHeadLine
                ]
            ]
        ]


viewHeadLine : Html Msg
viewHeadLine =
    div [ class "jumbotron", style "text-align" "center" ]
        [ h1 [] [ text "Facilitator" ]
        ]

module Main exposing (main)

import Browser
import Html exposing (Html, div, text)


main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


type Msg
    = NoOp


type alias Model =
    { hello : String
    }


init : () -> ( Model, Cmd Msg )
init _ =
    ( initialModel, Cmd.none )


initialModel : Model
initialModel =
    { hello = "Hello World" }



-- Update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NoOp ->
            ( { model | hello = "Hello" }, Cmd.none )



-- View


view : Model -> Html Msg
view model =
    div []
        [ text model.hello ]



-- Subscription


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none

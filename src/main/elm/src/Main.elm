port module Main exposing (main)

import Browser
import Html exposing (Html, div, text)



-- JavaScript usage: app.ports.websocketIn.send(response);


port websocketIn : (String -> msg) -> Sub msg



-- JavaScript usage: app.ports.websocketOut.subscribe(handler);


port websocketOut : String -> Cmd msg


main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


type Msg
    = WebsocketIn String
    | Submit String


type alias Model =
    { message : String
    }


init : () -> ( Model, Cmd Msg )
init _ =
    ( initialModel, Cmd.none )


initialModel : Model
initialModel =
    { message = "Hello World" }



-- Update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        WebsocketIn message ->
            ( { model | message = message }, Cmd.none )

        Submit value ->
            ( model
            , websocketOut value
            )



-- View


view : Model -> Html Msg
view model =
    div []
        [ text model.message ]



-- Subscription


subscriptions : Model -> Sub Msg
subscriptions _ =
    websocketIn WebsocketIn

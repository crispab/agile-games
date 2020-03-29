port module Main exposing (ChatMessage, main, messageDecoder)

import Bootstrap.Button as Button
import Bootstrap.Form as Form
import Bootstrap.Form.Input as Input
import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col exposing (xs, xs3)
import Browser
import Html exposing (Html, div, text)
import Html.Attributes exposing (for)
import Json.Decode exposing (errorToString)
import Json.Decode.Pipeline exposing (required)
import Time


port websocketIn : (String -> msg) -> Sub msg


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
    | ChangeMessage String
    | SendMessage


type alias Model =
    { message : String
    , messages : List ChatMessage
    }


type alias ChatMessage =
    { data : String
    , timeStamp : Time.Posix
    }


init : () -> ( Model, Cmd Msg )
init _ =
    ( initialModel, Cmd.none )


initialModel : Model
initialModel =
    { message = ""
    , messages = []
    }



-- Update


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        WebsocketIn message ->
            ( { model | messages = decodeMessage message :: model.messages }, Cmd.none )

        SendMessage ->
            ( { model | message = "" }
            , websocketOut model.message
            )

        ChangeMessage message ->
            ( { model | message = message }, Cmd.none )


decodeMessage : String -> ChatMessage
decodeMessage message =
    case Json.Decode.decodeString messageDecoder message of
        Ok chatMessage ->
            chatMessage

        Err e ->
            ChatMessage (errorToString e) (Time.millisToPosix 0)


messageDecoder : Json.Decode.Decoder ChatMessage
messageDecoder =
    Json.Decode.succeed ChatMessage
        |> required "data" Json.Decode.string
        |> required "timeStamp" decodeTime


decodeTime : Json.Decode.Decoder Time.Posix
decodeTime =
    Json.Decode.int
        |> Json.Decode.andThen
            (\ms ->
                Json.Decode.succeed <| Time.millisToPosix ms
            )



-- View


view : Model -> Html Msg
view model =
    Grid.container []
        [ Grid.row []
            [ Grid.col []
                [ viewMessages model
                , viewFormToAddMember model
                ]
            ]
        ]


viewMessages : Model -> Html Msg
viewMessages model =
    Grid.row []
        [ Grid.col [] <| List.map viewMessage model.messages
        ]


viewMessage : ChatMessage -> Html Msg
viewMessage message =
    div [] [ text message.data ]


viewFormToAddMember : Model -> Html Msg
viewFormToAddMember model =
    Form.form []
        [ Form.group []
            [ Grid.row []
                [ Grid.col [ xs3 ]
                    [ Form.label [ for "message" ] [ text "Enter message:" ] ]
                , Grid.col [ xs ]
                    [ Input.text [ Input.id "message", Input.onInput ChangeMessage, Input.value model.message ]
                    ]
                , Grid.col [ xs3 ] [ Button.button [ Button.primary, Button.onClick SendMessage ] [ text "Send" ] ]
                ]
            ]
        ]



-- Subscription


subscriptions : Model -> Sub Msg
subscriptions _ =
    websocketIn WebsocketIn

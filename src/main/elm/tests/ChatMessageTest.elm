module ChatMessageTest exposing (all, expectedMessage, testMessage)

import Expect
import Json.Decode exposing (decodeString)
import Main exposing (ChatMessage, messageDecoder)
import Test exposing (..)
import Time


all : Test
all =
    describe "Test of chat Json decoding."
        [ test "Single chat info" <|
            \_ ->
                decodeString messageDecoder testMessage
                    |> Expect.equal expectedMessage
        ]


testMessage =
    """
    {"data":"[auser] Joined!","timeStamp":5051.91499995999}
    """


expectedMessage : Result Json.Decode.Error ChatMessage
expectedMessage =
    Ok
        { data = "auser Joined"
        , timeStamp = Time.millisToPosix 1234
        }

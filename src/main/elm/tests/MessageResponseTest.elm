module MessageResponseTest exposing (all, expectedOkMessage, givenOkMessage)

import Expect
import Main exposing (MessageResponse, MessageResponseStatus(..), decodeMessage)
import Test exposing (..)


all : Test
all =
    describe "Test of message Json decoding."
        [ test "Simple OK message" <|
            \_ ->
                decodeMessage givenOkMessage
                    |> Expect.equal expectedOkMessage
        , test "Simple FAIL message" <|
            \_ ->
                decodeMessage givenFailMessage
                    |> Expect.equal expectedFailMessage
        ]


givenOkMessage =
    """
    {"status":"OK"}
    """


expectedOkMessage : MessageResponse
expectedOkMessage =
    { status = OK
    }


givenFailMessage =
    """
    {"status":"FAIL",
    "message":"some fail message"
    }
    """


expectedFailMessage : MessageResponse
expectedFailMessage =
    { status = FAIL "some fail message"
    }

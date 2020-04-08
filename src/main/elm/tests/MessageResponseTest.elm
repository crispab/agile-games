module MessageResponseTest exposing (all, expectedOkMessage, givenOkMessage)

import Dict
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
        , test "OK message with parameters" <|
            \_ ->
                decodeMessage givenOkMessageWithParameters
                    |> Expect.equal expectedOkMessageWithParameters
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
    , parameters = Dict.empty
    }


givenOkMessageWithParameters =
    """
    {
      "status": "OK",
      "parameters": [
        {
          "key": "someKey1",
          "value": "1stValue"
        },
        {
          "key": "someKey2",
          "value": "2ndValue"
        }
      ]
    }
    """


expectedOkMessageWithParameters : MessageResponse
expectedOkMessageWithParameters =
    { status = OK
    , parameters = Dict.fromList [ ( "someKey1", "1stValue" ), ( "someKey2", "2ndValue" ) ]
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
    , parameters = Dict.empty
    }

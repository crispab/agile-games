module Command exposing (facilitate)

import Json.Encode as Encode


facilitate : Encode.Value
facilitate =
    Encode.object
        [ ( "commandType", Encode.string "FACILITATE" )
        ]

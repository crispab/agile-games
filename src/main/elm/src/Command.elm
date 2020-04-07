module Command exposing (facilitate, join)

import Json.Encode as Encode


facilitate : Encode.Value
facilitate =
    Encode.object
        [ ( "commandType", Encode.string "FACILITATE" )
        ]


join : Encode.Value
join =
    Encode.object
        [ ( "commandType", Encode.string "JOIN" )
        ]

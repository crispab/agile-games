module Command exposing (facilitate, join)

import Json.Encode as Encode
import Model exposing (UserSessionId, userSessionId2String)


facilitate : UserSessionId String -> Encode.Value
facilitate id =
    Encode.object
        [ ( "commandType", Encode.string "FACILITATE" )
        , ( "userSessionId", Encode.string <| userSessionId2String id )
        ]


join : UserSessionId String -> Encode.Value
join id =
    Encode.object
        [ ( "commandType", Encode.string "JOIN" )
        , ( "userSessionId", Encode.string <| userSessionId2String id )
        ]

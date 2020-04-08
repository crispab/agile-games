module Command exposing (facilitate, join)

import Json.Encode as Encode
import Model exposing (GameSessionId, UserSessionId, userSessionId2String)


facilitate : UserSessionId String -> Encode.Value
facilitate id =
    Encode.object
        [ ( "commandType", Encode.string "FACILITATE" )
        , ( "userSessionId", Encode.string <| userSessionId2String id )
        ]


join : UserSessionId String -> String -> Encode.Value
join userId gameCode =
    Encode.object
        [ ( "commandType", Encode.string "JOIN" )
        , ( "userSessionId", Encode.string <| userSessionId2String userId )
        , ( "parameters"
          , Encode.object
                [ ( "gameSessionId", Encode.string gameCode )
                ]
          )
        ]

module Command exposing (facilitate, join, resume)

import Json.Encode as Encode
import Model exposing (GameSessionId, UserSessionId, userSessionId2String)


facilitate : UserSessionId String -> Encode.Value
facilitate id =
    Encode.object
        [ ( "commandType", Encode.string "FACILITATE" )
        , ( "userSessionId", Encode.string <| userSessionId2String id )
        ]


join : UserSessionId String -> String -> String -> Encode.Value
join userId gameCode playerName =
    Encode.object
        [ ( "commandType", Encode.string "JOIN" )
        , ( "userSessionId", Encode.string <| userSessionId2String userId )
        , ( "parameters"
          , Encode.object
                [ ( "gameSessionId", Encode.string gameCode )
                , ( "playerName", Encode.string playerName )
                ]
          )
        ]


resume : String -> Encode.Value
resume session =
    Encode.object
        [ ( "commandType", Encode.string "RESUME" )
        , ( "userSessionId", Encode.string session )
        ]

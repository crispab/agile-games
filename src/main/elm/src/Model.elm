module Model exposing (GameSessionId, Model, Page(..), UserSessionId, gameSessionId2String, gameSessionIdFromString, userSessionId2String, userSessionIdFromString)

import Bootstrap.Alert as Alert
import Message exposing (GameState)


type Page
    = LobbyPage
    | FacilitatorPage
    | PlayerPage


type alias Model =
    { currentPage : Page
    , code : String
    , alertVisibility : Alert.Visibility
    , userSessionId : UserSessionId String
    , gameSessionId : GameSessionId String
    , errorMessage : String
    , gameState : GameState
    }


type UserSessionId s
    = UserSessionId String


userSessionId2String : UserSessionId String -> String
userSessionId2String (UserSessionId s) =
    s


userSessionIdFromString : String -> UserSessionId String
userSessionIdFromString s =
    UserSessionId s


type GameSessionId s
    = GameSessionId String


gameSessionId2String : GameSessionId String -> String
gameSessionId2String (GameSessionId s) =
    s


gameSessionIdFromString : String -> GameSessionId String
gameSessionIdFromString s =
    GameSessionId s

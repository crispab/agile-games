module Model exposing (GameSessionCode, Model, Page(..), UserSessionId, gameSessionCodeFromString, gameSessionId2String, initialModel, userSessionId2String, userSessionIdFromString)

import Bootstrap.Alert as Alert
import Dict
import Message exposing (GamePhase(..), GameState)


type Page
    = LobbyPage
    | FacilitatorPage
    | PlayerPage


type alias Model =
    { currentPage : Page
    , code : String
    , playerId : String
    , playerName : String
    , playerAvatar : String
    , estimate1 : String
    , estimate2 : String
    , estimateEnd : String
    , alertVisibility : Alert.Visibility
    , userSessionId : UserSessionId String
    , gameSessionCode : GameSessionCode String
    , errorMessage : String
    , gameState : GameState
    }


type UserSessionId s
    = UserSessionId String


initialModel : String -> Model
initialModel session =
    { currentPage = LobbyPage
    , code = ""
    , playerId = ""
    , playerName = ""
    , playerAvatar = ""
    , estimate1 = ""
    , estimate2 = ""
    , estimateEnd = ""
    , alertVisibility = Alert.closed
    , userSessionId = userSessionIdFromString session
    , gameSessionCode = gameSessionCodeFromString ""
    , errorMessage = ""
    , gameState = initialGameState
    }


initialGameState : GameState
initialGameState =
    { phase = Gathering
    , players = Dict.fromList []
    , board = [ [] ]
    }


userSessionId2String : UserSessionId String -> String
userSessionId2String (UserSessionId s) =
    s


userSessionIdFromString : String -> UserSessionId String
userSessionIdFromString s =
    UserSessionId s


type GameSessionCode s
    = GameSessionId String


gameSessionId2String : GameSessionCode String -> String
gameSessionId2String (GameSessionId s) =
    s


gameSessionCodeFromString : String -> GameSessionCode String
gameSessionCodeFromString s =
    GameSessionId s

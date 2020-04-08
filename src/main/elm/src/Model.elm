module Model exposing (Model, Page(..))

import Bootstrap.Alert as Alert


type Page
    = LobbyPage
    | FacilitatorPage
    | PlayerPage


type alias Model =
    { currentPage : Page
    , code : String
    , alertVisibility : Alert.Visibility
    , userSessionId : String
    , gameSessionId : String
    , errorMessage : String
    }

module Msg exposing (Msg(..))

import Bootstrap.Alert as Alert
import Message exposing (GamePhase)


type Msg
    = WebsocketIn String
    | ChangeCode String
    | ChangePlayerName String
    | JoinGame
    | DismissAlert Alert.Visibility
    | Facilitate
    | GotoPhase GamePhase

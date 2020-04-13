module Msg exposing (Direction(..), Msg(..))

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
    | Move Direction


type Direction
    = Up
    | Down
    | Left
    | Right

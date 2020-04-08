module Msg exposing (Msg(..))

import Bootstrap.Alert as Alert


type Msg
    = WebsocketIn String
    | ChangeCode String
    | JoinGame
    | DismissAlert Alert.Visibility
    | Facilitate

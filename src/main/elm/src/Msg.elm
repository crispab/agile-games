module Msg exposing (Direction(..), Msg(..))

import Bootstrap.Alert as Alert
import Message exposing (GamePhase)


type Msg
    = WebsocketIn String
    | ChangeCode String
    | ChangePlayerName String
    | EstimateGoal1 String
    | EstimateGoal2 String
    | EstimateEndGoal String
    | Estimate
    | JoinGame
    | DismissAlert Alert.Visibility
    | Facilitate
    | GotoPhase GamePhase
    | Move Direction
    | Leave


type Direction
    = Up
    | Down
    | Left
    | Right

package agile.games.api;

import agile.games.tts.GamePhase;
import agile.games.tts.GameSessionCode;

import java.util.List;

public class GameStateMessage implements Message {
    private GameStateInfo gameState;

    public GameStateInfo getGameState() {
        return gameState;
    }

    public void setGameState(GameStateInfo gameState) {
        this.gameState = gameState;
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return gameState.gameSessionCode;
    }

    public static class GameStateInfo {
        private GameSessionCode gameSessionCode;
        private GamePhase phase;
        private List<PlayerDto> players;
        private List<List<SquareDto>> board;

        public GameSessionCode getGameSessionCode() {
            return gameSessionCode;
        }

        public void setGameSessionCode(GameSessionCode gameSessionCode) {
            this.gameSessionCode = gameSessionCode;
        }

        public GamePhase getPhase() {
            return phase;
        }

        public void setPhase(GamePhase phase) {
            this.phase = phase;
        }

        public List<PlayerDto> getPlayers() {
            return players;
        }

        public void setPlayers(List<PlayerDto> players) {
            this.players = players;
        }

        public List<List<SquareDto>> getBoard() {
            return board;
        }

        public void setBoard(List<List<SquareDto>> board) {
            this.board = board;
        }
    }
}

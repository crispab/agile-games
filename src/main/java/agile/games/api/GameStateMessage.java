package agile.games.api;

import agile.games.tts.GamePhase;

import java.util.List;

public class GameStateMessage implements Message {
    private Status status;
    private InnerState gameState;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public InnerState getGameState() {
        return gameState;
    }

    public void setGameState(InnerState gameState) {
        this.gameState = gameState;
    }

    public static class InnerState {
        private GamePhase phase;
        private List<String> players;
        private List<List<SquareDto>> board;

        public GamePhase getPhase() {
            return phase;
        }

        public void setPhase(GamePhase phase) {
            this.phase = phase;
        }

        public List<String> getPlayers() {
            return players;
        }

        public void setPlayers(List<String> players) {
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

package agile.games.tts;

import java.util.Random;

class Board {
    private static final int MAX_ATTEMPTS = 1000;
    private int width;
    private int height;
    private Square[][] squares;
    private Random random = new Random();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.squares = new Square[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.squares[x][y] = new Square();
            }
        }
    }

    public int movePlayerTo(int x, int y, Player player) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        }
        Square square = squares[x][y];
        if (square.getPlayer() != null) {
            return 0;
        }
        if (player.getPosition() != null) {
            Square currentSquare = squares[player.getX()][player.getY()];
            currentSquare.setPlayer(null);
        }
        square.setPlayer(player);
        player.setPosition(new PlayerPosition(x, y));
        return 1;
    }

    public void swapPlayerPosition(Player player1, Player player2) {
        PlayerPosition p1 = player1.getPosition();
        Square square1 = squares[p1.getX()][p1.getY()];
        PlayerPosition p2 = player2.getPosition();
        Square square2 = squares[p2.getX()][p2.getY()];
        square1.setPlayer(player2);
        square2.setPlayer(player1);
        player1.setPosition(p2);
        player2.setPosition(p1);
    }

    public Player getPlayerAt(int x, int y) {
        return squares[x][y].getPlayer();
    }

    public String getRowAsString(int y) {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < width; x++) {
            Player player = squares[x][y].getPlayer();
            result.append("|");
            if (player == null) {
                result.append(" ");
            } else {
                result.append(player.getName().charAt(0));
            }
        }
        result.append("|");
        return result.toString();
    }

    public PlayerPosition getRandomFreeSquare() {
        for (int attempts = 0; attempts < MAX_ATTEMPTS; attempts++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            if (squares[x][y].getPlayer() == null) {
                return new PlayerPosition(x, y);
            }
        }
        return null;
    }
}

package agile.games.tts;

class Board {
    private int width;
    private int height;
    private Square[][] squares;

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

    public void movePlayerTo(int x, int y, Player player) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        Square square = squares[x][y];
        if (square.getPlayer() != null) {
            return;
        }
        if (player.getPosition() != null) {
            Square currentSquare = squares[player.getX()][player.getY()];
            currentSquare.setPlayer(null);
        }
        square.setPlayer(player);
        player.setPosition(new PlayerPosition(x, y));
    }
}

package GamesManager;

public class gameBoardInfo {
    private boolean isActive;
    private int[][] gameBoard;

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public boolean getIsActive()
    {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setGameBoard(int[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
}

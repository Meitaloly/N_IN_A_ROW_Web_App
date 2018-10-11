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

    public int getValue (int row,int col ){
        return this.gameBoard[row][col];
    }

    public void setValue (int row,int col, int val ){
        this.gameBoard[row][col] = val ;
    }

}

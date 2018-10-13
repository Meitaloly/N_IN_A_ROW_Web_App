package GamesManager;

import GameLogic.GameManager;
import GameLogic.Player;
import UserAuthentication.User;

import java.util.ArrayList;
import java.util.Map;

public class gameBoardInfo {
    private boolean isActive;
    private int[][] gameBoard;
    private String gameType;

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameType() {
        return gameType;
    }

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

    public void setValue (int col, int val ){
        for(int row = gameBoard.length-1; row>=0 ;row--){
            if (gameBoard[row][col] ==  0 ){
                gameBoard[row][col] = val ;
            }
        }
    }

}

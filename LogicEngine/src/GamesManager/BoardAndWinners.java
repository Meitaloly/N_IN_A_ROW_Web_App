package GamesManager;

import java.util.ArrayList;
import java.util.List;

public class BoardAndWinners {

    private int[][] gameBoard;
    private List<String> winners;

    public void setGameBoard(int[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public List<String> getWinners() {
        return winners;
    }
}

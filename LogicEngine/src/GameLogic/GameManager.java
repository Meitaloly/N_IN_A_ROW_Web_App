package GameLogic;

import GameLogic.generatedClasses.GameDescriptor;
import GameLogic.generatedClasses.Player;

import java.util.*;

public class GameManager {

    private GameBoard gameBoard;
    //private GameDescriptor desc;
    private Map<Integer, GameLogic.Player> playersInMap;
    private ArrayList<GameLogic.Player> playersByOrder;
    private String variant;
    private boolean activeGame = false;
    private int turnIndex = 0;
    private GameHistory history;

    public void incTurnIndex() {
        turnIndex++;
        if (playersInMap.size() <= turnIndex) {
            turnIndex = 0;
        }
    }

    public void setGameBoard(int rows, int cols, int target)
    {
        gameBoard = new GameBoard();
        gameBoard.setGameBoard(rows, cols, target);
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public GameManager() {
        gameBoard = new GameBoard();
    }

    public void resetGame()
    {
        gameBoard.reset();
        setActiveGame(false);
        resetPlayersData();
        setTurnIndex(0);

    }

    private void resetPlayersData()
    {
        for(GameLogic.Player player : playersByOrder)
        {
            player.setActive();
            player.setTurnCounter(0);
        }
    }

    public void setActiveGame(boolean activeGame) {
        this.activeGame = activeGame;
    }

    public boolean getActiveGame() {
        return activeGame;
    }

        public ArrayList<GameLogic.Player> getPlayersByOrder() {
        return playersByOrder;
    }

    public String getVariant() {
        return variant;
    }

//    public int checkXmlFile(String path) {
//        int res = -1;
//        XFU.setFilePath(path);
//        synchronized (this) {
//            res = XFU.checkXmlFileValidation(gameBoard);
//        }
//        return res;
//
//    }

    //public void setDesc(GameDescriptor desc) {
//        this.desc = desc;
//    }

//    public GameDescriptor getDesc() {
//        return desc;
//    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public ComputerChoice ComputerPlay() {
        Random rand = new Random();
        int choosenCol;
        int randomNum;
        ComputerChoice computerChoice = new ComputerChoice();

        List<Integer> colsToEnterTo = gameBoard.checkAvaliableColForEnter();

        if(!colsToEnterTo.isEmpty())
        {
            computerChoice.setPopout(false);
            if(colsToEnterTo.size() == 1)
            {
                choosenCol =  colsToEnterTo.get(0);
            }
            else {
                randomNum = rand.nextInt(colsToEnterTo.size() - 1);
                choosenCol = colsToEnterTo.get(randomNum);
                int placeInBoard = gameBoard.getBoard()[0][choosenCol];
                while (placeInBoard != -1) {
                    randomNum = rand.nextInt(colsToEnterTo.size() - 1);
                    choosenCol = colsToEnterTo.get(randomNum);
                    placeInBoard = gameBoard.getBoard()[1][choosenCol];
                }
            }
            computerChoice.setChoosenCol(choosenCol);
        }
        else
        {
            if(variant.toUpperCase().equals("POPOUT")) {
                List<Integer> colsToRemoveFrom = gameBoard.checkAvaliableColForRemove(playersByOrder.get(turnIndex).getPlayerSign());
                computerChoice.setPopout(true);

                if (!colsToRemoveFrom.isEmpty()) {
                    if (colsToRemoveFrom.size() == 1) {
                        choosenCol = colsToRemoveFrom.get(0);
                    } else {
                        randomNum = rand.nextInt(colsToRemoveFrom.size() - 1);
                        choosenCol = colsToRemoveFrom.get(randomNum);
                        int placeInBoard = gameBoard.getBoard()[0][choosenCol];
                        while (placeInBoard != playersByOrder.get(turnIndex).getPlayerSign()) {
                            randomNum = rand.nextInt(colsToRemoveFrom.size() - 1);
                            choosenCol = colsToRemoveFrom.get(randomNum);
                            placeInBoard = gameBoard.getBoard()[0][choosenCol];
                        }
                    }
                    computerChoice.setChoosenCol(choosenCol);
                } else {
                    computerChoice.setSucceeded(false);
                }
            }
            else {
                computerChoice.setSucceeded(false);
            }
        }
        return computerChoice;
    }

    public void incCurrPlayerTurn() {
        playersByOrder.get(turnIndex).incTurnCounter();
    }

    public boolean isOnlyOnePlayerLeft()
    {
        boolean res = false;
        int counter = playersByOrder.size();
        for(GameLogic.Player player: playersByOrder) {
            if(!player.isAcive()) {
                counter--;
                if (counter == 1) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    public GameLogic.Player getWinnerPlayer()
    {
        GameLogic.Player winner = null;
        for(GameLogic.Player player: playersByOrder) {
            if(player.isAcive()) {
                winner=  player;
            }
        }
        return winner;
    }

//    public boolean buildPlayersFromFile() {
//        boolean res = true;
//        playersInMap = new HashMap<>();
//        playersInMap.clear();
//        playersByOrder = new ArrayList<>();
//        playersByOrder.clear();
//
//        for (GameLogic.generatedClasses.Player player : desc.getPlayers().getPlayer()) {
//            Integer playerId = Integer.valueOf(player.getId());
//            if (playersInMap.containsKey(playerId)) {
//                playersInMap.clear();
//                playersByOrder.clear();
//                res = false;
//                break;
//            } else {
//                GameLogic.Player newPlayer = new GameLogic.Player();
//                newPlayer.setId((int) player.getId());
//                newPlayer.setName(player.getName());
//                newPlayer.setPlayerType(player.getType());
//                playersInMap.put(newPlayer.getId(), newPlayer);
//                playersByOrder.add(newPlayer);
//            }
//        }
//        if (res) {
//            variant = desc.getGame().getVariant();
//            setColorosToPlayers();
//        }
//        return res;
//    }


    public boolean checkColFullInBoard(int col) {
        return gameBoard.isColFull(col);
    }

    public void setColorosToPlayers() {
        int size = playersInMap.size();
        for (int i = 0; i < size; i++) {
            playersByOrder.get(i).setPlayerColor(setDickColor(i));
            playersByOrder.get(i).setPlayerSign(i);
        }
    }

    private String setDickColor(int index) {
        switch (index) {
            case 0:
                return ("green");

            case 1:
                return "blue";

            case 2:
                return "red";

            case 3:
                return "yellow";

            case 4:
                return "pink";

            case 5:
                return "azure";
        }
        return "noColor";
    }

    public int getNumOfPlayers() {
        return playersInMap.size();
    }

    public int getPlayerId(int i) {
        return playersInMap.get(i).getId();
    }

    public String getPlayerName(int i) {
        return playersInMap.get(i).getName();
    }

    public int getPlayerNumOfTurns(int i) {
        return playersInMap.get(i).getTurnCounter();
    }

    public String getPlayerType(int i) {
        return playersInMap.get(i).getPlayerType();
    }
}
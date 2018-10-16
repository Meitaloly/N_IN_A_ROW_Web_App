package GameLogic;

import GameLogic.generatedClasses.GameDescriptor;
import UserAuthentication.User;

import java.util.*;

public class GameManager {

    private GameBoard gameBoard;
    //private GameDescriptor desc;
    //private Map<Integer, GameLogic.Player> playersInMap;
    private ArrayList<GameLogic.Player> playersByOrder;
    private String variant;
    private boolean activeGame = false;
    private int turnIndex = 0;

    //private GameHistory history;

    public Player getPlayer (String playerName){
        for(Player p:playersByOrder){
            if (p.getName().equals(playerName)){
                return p;
            }
        }
         return null;
    }

    public void incTurnIndex() {
        turnIndex++;
        if (playersByOrder.size() <= turnIndex) {
            turnIndex = 0;
        }
    }

    public String getCurrPlayerName()
    {
        return playersByOrder.get(turnIndex).getName();
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setPlayers(Map<String, User> players)
    {

        playersByOrder = new ArrayList<>();
        int id = 0;
        for(User player : players.values())
        {
            Player currPlayer  = new Player();
            currPlayer.setId(id);
            currPlayer.setName(player.getUserName());
            currPlayer.setTurnCounter(0);
            currPlayer.setPlayerType(player.getType());
            currPlayer.setPlayerColor(setDickColor(id));
            currPlayer.setPlayerSign(id);
            currPlayer.setActive();
            playersByOrder.add(currPlayer);
            id++;
        }
    }

    public boolean checkIfHumanAndInsert(int col)
    {
        boolean res = false;
        if(playersByOrder.get(turnIndex).getPlayerType().toLowerCase().equals("human"))
        {
            if(!checkColFullInBoard(col))
            {
                gameBoard.setSignOnBoard(col,playersByOrder.get(turnIndex));
                incCurrPlayerTurn();
                incTurnIndex();
                res = true;
            }
        }
        return res;
    }

    public boolean checkIfHumanAndPopout(int col)
    {
        boolean res = false;
        if(playersByOrder.get(turnIndex).getPlayerType().toLowerCase().equals("human"))
        {
            if (gameBoard.checkSignAndRemove(col, playersByOrder.get(turnIndex).getPlayerSign()))
            {
                incCurrPlayerTurn();
                incTurnIndex();
                res = true;
            }
        }
        return res;
    }

    public List<String> checkAnyWinners()
    {
        List<Integer> winnerSigns = new ArrayList<>();
        List<String> winners = new ArrayList<>();

        for(int i=0; i<gameBoard.getCols(); i++)
        {
            gameBoard.checkAnyWinner(i,winnerSigns,variant);
        }

        if(!winnerSigns.isEmpty())
        {
            convertSignsToPlayers(winnerSigns,winners);
        }

        return winners;
    }

    public void convertSignsToPlayers(List<Integer> signs, List<String> players)
    {
        for(Integer sign : signs)
        {
            players.add(playersByOrder.get(sign).getName());
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
        playersByOrder.clear();
        //resetPlayersData();
        setTurnIndex(0);
    }

    public void checkConputerTurn()
    {
        if(getPlayersByOrder().get(turnIndex).getPlayerType().toLowerCase().equals("computer")) {
            ComputerChoice computerChoice = ComputerPlay();
            if(computerChoice.getSucceeded())
            {
                if(computerChoice.getPopout())
                {
                    if (gameBoard.checkSignAndRemove(computerChoice.getChoosenCol(), playersByOrder.get(turnIndex).getPlayerSign()))
                    {
                        incCurrPlayerTurn();
                        incTurnIndex();
                    }
                }
                else
                {
                    if(!checkColFullInBoard(computerChoice.getChoosenCol()))
                    {
                        gameBoard.setSignOnBoard(computerChoice.getChoosenCol(),playersByOrder.get(turnIndex));
                        incCurrPlayerTurn();
                        incTurnIndex();
                    }
                }
            }
            else
            {
                System.out.print("gameOver - computer can't play");
            }
        }
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

    public void printArray(){
        for(GameLogic.Player player : playersByOrder)
        {
            System.out.println(player.getName());
            System.out.println(player.getPlayerType());
            System.out.println(player.getId());
            System.out.println(player.getPlayerColor());
        }
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

//    public void setColorosToPlayers() {
//        int size = playersInMap.size();
//        for (int i = 0; i < size; i++) {
//            playersByOrder.get(i).setPlayerColor(setDickColor(i));
//            playersByOrder.get(i).setPlayerSign(i);
//        }
//    }

    private String setDickColor(int index) {
        switch (index) {
            case 0:
                return "pink";

            case 1:
                return "blue";

            case 2:
                return "red";

            case 3:
                return "yellow";

            case 4:
                return "green";

            case 5:
                return "aqua";
        }
        return "noColor";
    }

    public int getNumOfPlayers() {
        return playersByOrder.size();
    }

    public int getPlayerId(int i) {
        return playersByOrder.get(i).getId();
    }

    public String getPlayerName(int i) {
        return playersByOrder.get(i).getName();
    }

    public int getPlayerNumOfTurns(int i) {
        return playersByOrder.get(i).getTurnCounter();
    }

    public String getPlayerType(int i) {
        return playersByOrder.get(i).getPlayerType();
    }
}
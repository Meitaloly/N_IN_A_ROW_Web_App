package GamesManager;

import GameLogic.GameManager;
import UserAuthentication.User;

import java.util.Map;

public class GameInList {
    private String userOwner;
    private int rows;
    private int cols;
    private int target;
    private String variant;
    private String gameName;
    private int numOfPlayersRequired;
    private int currNumOfPlayersInGame = 0;
    private String status = "Not active";
    private GameManager currGameManager;
    private String playerTurn ="";
    private boolean isCreate= false;


    public void printPlayers(){
        if (isCreate) {
            currGameManager.printArray();
        }
    }
    public void setPlayers (Map<String, User> players){currGameManager.setPlayers(players);}

    public boolean isArrayByOrder(){
        return (isCreate);
    }

    public GameInList(String xmlContext)
    {
        userOwner = getValueFromSplitString(xmlContext,"name=\"","\"");
        target = Integer.parseInt(getValueFromSplitString(xmlContext,"<Game target=\"","\""));
        rows = Integer.parseInt(getValueFromSplitString(xmlContext,"<Board rows=\"","\""));
        cols = Integer.parseInt(getValueFromSplitString(xmlContext,"columns=\"","\""));
        variant = getValueFromSplitString(xmlContext,"<Variant>","</Variant>");
        gameName = getValueFromSplitString(xmlContext,"game-title=\"", "\"");
        numOfPlayersRequired = Integer.parseInt(getValueFromSplitString(xmlContext,"total-players=\"","\""));
    }

    public String getPlayerTurn() {
        return  playerTurn;
    }

    public void addPlayersWithColors(Map<String, User> players)
    {
        isCreate = true;
        currGameManager.setPlayers(players);
        setNextPlayerName();
    }


    public GameManager getCurrGameManager() {
        return currGameManager;
    }

    private String getValueFromSplitString(String strToSplit, String firstStr, String SecStr)
    {
        String[] arr = strToSplit.split(firstStr);
        String[] valueArr = arr[1].split(SecStr);
        return valueArr[0];
    }

    public void decNumOfSignedPlayers()
    {
        currNumOfPlayersInGame--;
    }

    public String getVariant() {
        return variant;
    }

    public int getCols() {
        return cols;
    }

    public int getCurrNumOfPlayersInGame() {
        return currNumOfPlayersInGame;
    }

    public int getNumOfPlayersRequired() {
        return numOfPlayersRequired;
    }

    public int getRows() {
        return rows;
    }

    public int getTarget() {
        return target;
    }

    public String getGameName() {
        return gameName;
    }

    public String getStatus() {
        return status;
    }

    public String getUserOwner() {
        return userOwner;
    }

    public void activeGame(){
        currGameManager = new GameManager();
        currGameManager.setActiveGame(true);
        currGameManager.setTurnIndex(0);
        currGameManager.setGameBoard(rows,cols,target);
        currGameManager.setVariant(variant);

    }

    public boolean isActive()
    {
        boolean res = true;
        if(status.equals("Not active"))
        {
            res = false;
        }
        return res;
    }

    public void incNumOfSignedPlayers()
    {
        currNumOfPlayersInGame++;
        if(currNumOfPlayersInGame == numOfPlayersRequired)
        {
            status = "Active";
            activeGame();
        }
    }

    public void setNextPlayerName(){
        playerTurn = currGameManager.getPlayerName(currGameManager.getTurnIndex());
    }

}

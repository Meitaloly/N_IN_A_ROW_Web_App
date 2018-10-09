package GamesManager;

import GameLogic.GameManager;

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
//        currGameManager.setColorosToPlayers();
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

}

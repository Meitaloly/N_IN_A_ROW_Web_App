package GamesManager;

public class GameInList {
    private String userOwner;
    private int rows;
    private int cols;
    private int target;
    private String variant;
    private String gameName;
    private int numOfPlayersRequired;
    private int currNumOfPlayersInGame = 0;

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

    private String getValueFromSplitString(String strToSplit, String firstStr, String SecStr)
    {
        String[] arr = strToSplit.split(firstStr);
        String[] valueArr = arr[1].split(SecStr);
        return valueArr[0];
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

    public String getUserOwner() {
        return userOwner;
    }

}
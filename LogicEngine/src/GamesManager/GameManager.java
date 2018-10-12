package GamesManager;

import UserAuthentication.User;

import java.util.*;

public class GameManager {
    private Map<String, GameInList> gamesList;

    public void setPlayers(String gName, Map<String, User> players){gamesList.get(gName).setPlayers(players);}

    public GameManager() {
        gamesList = new HashMap<>();
    }

    public String checkGameValidation(String xmlString) {
        int validationNumber = -1;

        //enter all game data to GameInList Obj
        GameInList currGame = new GameInList(xmlString);

        if(!checkUniqueGameName(currGame)) {
            validationNumber = 5; // game's name is already exist
        }
        else if (!checkValidNumOfRows(currGame)) {
            validationNumber = 1; // num of rows not good
        }
        else if (!checkValidNumOfCols(currGame)) {
            validationNumber = 2; // cols not good
        }
        else if (!checkRowAndColsBiggerThenTarget(currGame)) {
            validationNumber = 3; // target bigger than rows or cols
        }
        else if (currGame.getTarget() == 0) {
            validationNumber = 4; // target = 0
        }
        else if (!checkValidNumOfPlayers(currGame)) {
            validationNumber = 6; // num of players not in range
        }

        if (validationNumber == -1) {
            addGame(currGame);
        }

        return convertIntToMsgString(validationNumber);
    }

    private String convertIntToMsgString(int num)
    {
        String msg = "";

        switch (num)
        {
            case -1:
                msg = "XML File Loaded successfully!";
                break;
            case 1:
                msg="XML is not good! num of rows is not in range";
                break;
            case 2:
                msg="XML is not good!  num of cols is not in range";
                break;
            case 3:
                msg="XML is not good! target bigger than rows or cols";
                break;
            case 4:
                msg="XML is not good! target = 0";
                break;
            case 5:
                msg="XML is not good! game's name is already exist";
                break;
            case 6:
                msg="XML is not good! num of players not in range";
                break;
        }

        return msg;
    }

    public boolean checkUniqueGameName(GameInList game)
    {
        boolean res = true;

        if(gamesList.containsKey(game.getGameName()))
        {
            res = false;
        }

        return res;
    }

    private boolean checkValidNumOfCols(GameInList game)
    {
        boolean res = false;
        if(game.getCols() >= 6 && game.getCols() <= 30)
        {
            res= true;
        }
        return res;
    }


    private boolean checkValidNumOfRows(GameInList game)
    {
        boolean res = false;
        if(game.getRows() >= 5 && game.getRows()<=50)
        {
            res= true;
        }
        return res;
    }

    public boolean checkRowAndColsBiggerThenTarget(GameInList game)
    {
        boolean res = false;
        if(game.getCols() > game.getTarget() && game.getRows() > game.getTarget())
        {
            res  =true;
        }

        return res;
    }

    public boolean checkValidNumOfPlayers(GameInList game)
    {
        boolean res = true;
        int numOfPlayers = game.getNumOfPlayersRequired();
        if(numOfPlayers < 2 || numOfPlayers > 6) {
            res = false;
        }
        return res;
    }

    private void addGame(GameInList gameToAdd)
    {
        gamesList.put(gameToAdd.getGameName(), gameToAdd);
    }

    public Map<String, GameInList> getGamesList() {
        return gamesList;
    }

    public GameInList getGameInListByName(String gameName)
    {
        return gamesList.get(gameName);
    }
}

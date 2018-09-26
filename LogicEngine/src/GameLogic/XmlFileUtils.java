package GameLogic;

import GameLogic.generatedClasses.GameDescriptor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.math.BigInteger;

public class XmlFileUtils {
    private String path;
    private File xmlFilePath;
    private GameDescriptor desc;
    private BigInteger numOfCols;
    private int numOfRows;
    private BigInteger target;
    private GameManager gameManager;


    public void setGameManager(GameManager gm)
    {
        this.gameManager = gm;
    }

    public void setFilePath(String newPath)
    {
        path = newPath;
    }
    public synchronized int checkXmlFileValidation(GameBoard gameBoard)
    {
        int validationNumber = -1;

        xmlFilePath = new File(path);
        if(!xmlFilePath.exists())
        {
            validationNumber = 0; // file not exist
        }
        else
        {
            if(!checkTypeOfFileExtension())
            {
                validationNumber = 5; // not XML FILE
            }
            else {
                gameManager.setDesc(parseXmltoJaxbMachine());

                if (!checkValidNumOfRows()) {
                    validationNumber = 1; // num of rows not good
                } else if (!checkValidNumOfCols()) {
                    validationNumber = 2; // cols not good
                } else if (!checkRowAndColsBiggerThenTarget()) {
                    validationNumber = 3; // target bigger then rows or cols
                } else if (target.longValue() == 0) {
                    validationNumber = 4; // target = 0
                } else if (!checkValidNumOfPlayers()) {
                    validationNumber = 6; // num of players not in range
                } else if (!checkValidPlayersId()) {
//                    System.out.print("ERROR FILE : "+Thread.currentThread().getName());
                    validationNumber = 7; // 2 player are with the same id
                }
            }
        }

        if(validationNumber == -1)
        {
            gameBoard.setGameBoard(numOfRows, numOfCols.longValue(),target.longValue());
        }

        return validationNumber;
    }

    public boolean checkValidNumOfPlayers()
    {
        boolean res = true;
        GameLogic.generatedClasses.Players players = gameManager.getDesc().getPlayers();
        int numOfPlayers = players.getNumOfPlayers();
        if(numOfPlayers < 2 || numOfPlayers > 6) {
            res = false;
        }
        return res;
    }

    public boolean checkValidPlayersId()
    {
        return gameManager.buildPlayersFromFile();
    }

    public boolean checkTypeOfFileExtension()
    {
        boolean res = false;
        String fileName = xmlFilePath.getName();
        String upperFileName = fileName.toUpperCase();
        int dotIndex = upperFileName.lastIndexOf('.');
        String typeOfFile= upperFileName.substring(dotIndex+1);
        if(typeOfFile.equals("XML"))
        {
            res = true;
        }

        return res;
    }

    public boolean checkRowAndColsBiggerThenTarget()
    {
        boolean res = false;
        target = gameManager.getDesc().getGame().getTarget();
        if(numOfCols.longValue() > target.longValue() && numOfRows > target.longValue())
        {
            res  =true;
        }

        return res;
    }

    private boolean checkValidNumOfCols()
    {
        boolean res = false;
        numOfCols = gameManager.getDesc().getGame().getBoard().getColumns();
        if(numOfCols.longValue() >= 6 && numOfCols.longValue() <= 30)
        {
            res= true;
        }
        return res;
    }


    private boolean checkValidNumOfRows()
    {
        boolean res = false;
        numOfRows = gameManager.getDesc().getGame().getBoard().getRows();
        if(numOfRows>= 5 && numOfRows<=50)
        {
            res= true;
        }
        return res;
    }

    public GameDescriptor parseXmltoJaxbMachine() {
        Unmarshaller jaxbUnmarshaller = null;
        GameDescriptor jaxbDesc = null;
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbDesc = (GameDescriptor) jaxbUnmarshaller.unmarshal(xmlFilePath);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return jaxbDesc;
    }
}

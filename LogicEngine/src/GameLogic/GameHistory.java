package GameLogic;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {
    List<String> history;

    public GameHistory(){
       history = new ArrayList<>();
    }

    public List<String> getHistory() {
        return history;
    }

    public void addToHistory (String numOfPlayer, int theColumn){
        history.add(numOfPlayer + ": " + theColumn);
    }

    public void eraseLastMove (){
        history.remove(history.size() - 1);
    }


    public void resetHistory () {
        history = new ArrayList<>();
    }

    public int getLastMove(){
        String lastMove = (history.get(history.size() - 1));
        int len = lastMove.length();
        int res = lastMove.charAt(len-1)-'0';  //get the value of last char of the string (the column)
        return res;
    }

    public boolean isEmpty(){
        return (history.isEmpty());
    }






}

package GameLogic;

public class ComputerChoice {
    private int choosenCol;
    private Boolean popout;
    private Boolean succeeded = true;

    public ComputerChoice(){}

    public void setChoosenCol(int choosenCol) {
        this.choosenCol = choosenCol;
    }

    public void setPopout(Boolean popout) {
        this.popout = popout;
    }

    public Boolean getPopout() {
        return popout;
    }

    public int getChoosenCol() {
        return choosenCol;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }
}

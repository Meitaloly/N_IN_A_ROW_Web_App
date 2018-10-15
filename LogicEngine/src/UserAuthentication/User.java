package UserAuthentication;

public class User {
    private String userName;
    private String type;
    private String game;

    public User(String name, String playerType)
    {
        userName = name;
        type=playerType;
        game = null;
    }

    public String getGame() {
        return game;
    }

    public String getType() {
        return type;
    }

    public String getUserName() {
        return userName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserGame(String gameName) {
        this.game = gameName;
    }
}

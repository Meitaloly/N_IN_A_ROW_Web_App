package UserAuthentication;

public class User {
    private String userName;
    private String type;

    public User(String name, String playerType)
    {
        userName = name;
        type=playerType;
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
}

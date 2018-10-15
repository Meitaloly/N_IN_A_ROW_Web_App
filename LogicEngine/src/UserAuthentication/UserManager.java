package UserAuthentication;

import java.util.*;

public class UserManager {
    /*
    Adding and retrieving users is synchronized and for that manner - these actions are thread safe
    Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
    of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
     */
    private final Map<String,User> users;

    public UserManager() {
        users = new HashMap<>();
    }

    public synchronized void addUser(String username, String type) {
        User userToAdd = new User(username, type);
        users.put(username, userToAdd);
    }

    public Map<String,User> getUsersOfCurrGame(String gameName)
    {
        Map<String, User> res = new HashMap<>();
        for(User user : users.values())
        {
            System.out.println();
            System.out.println(user.getUserName());
            System.out.println(user.getType());
            System.out.println(user.getGame());
            System.out.println();

           if(user.getGame() != null ) {
                if (user.getGame().equals(gameName)) {
                    res.put(user.getUserName(), user);
                }
           }
        }
        return res;
    }

    public synchronized void removeUser(String username) {
        users.remove(username);
    }

    public synchronized void updateGameName(String uName,String gName){
        User currUser = users.get(uName);
        currUser.setUserGame(gName);
    }

    public synchronized Map<String,User> getUsers() {
        return users;
    }

    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }
}


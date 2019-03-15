package netflix.simulation.entities.managers;

import lombok.Data;
import netflix.simulation.entities.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Data
public class UserManager implements Serializable {
    private static UserManager INSTANCE;
    private volatile Map<String, User> userList = new HashMap<>();


    public static synchronized UserManager getInstance() {
        if (INSTANCE == null) return INSTANCE = new UserManager();
        return INSTANCE;
    }

    /**
     * Invokes payToll method for each user, effectively collecting money from subscription
     */
    public void collectMoneyFromSubscription() {
        userList.values().forEach(User::payToll);
    }

    /**
     * Starts every user thread by submiting it to an executor
     */
    public void startAll() {
        userList.values().forEach(User::startThread);
    }
    /**
     * Kills every user and creates an empty map
     */
    public void killAll() {
        userList.values().forEach(User::kill);
        userList = new HashMap<>();
    }

    //Self explaining methods

    public void addToList(User user) {
        userList.putIfAbsent(user.getID(), user);
    }

    public void newUser() {
        User user = new User();
        userList.putIfAbsent(user.getID(), user);
        this.serialize("runUsers.bin");
    }

    public void removeFromList(String userId) {
        userList.get(userId).kill();
        userList.remove(userId);
    }

    public int getSize() {
        return userList.size();
    }

    public void serialize(String path) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path));
            output.writeObject(INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deserialize(String path) {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
            INSTANCE = (UserManager) input.readObject();
            System.out.println(UserManager.getInstance().getUserList().values());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
        public User getUser(int userID) {
        return userList.getOrDefault(userID, null);
    }
        public void showUsers() {
        userList.values().forEach(System.out::println);
    }
     */

}

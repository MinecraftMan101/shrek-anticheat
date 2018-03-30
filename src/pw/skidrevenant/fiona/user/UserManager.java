package pw.skidrevenant.fiona.user;

import java.util.ArrayList;
import java.util.UUID;

public class UserManager
{
    public ArrayList<User> allUsers;
    
    public UserManager() {
        this.allUsers = new ArrayList<User>();
    }
    
    public User getUser(final UUID uuid) {
        for (final User user : this.allUsers) {
            if (user.getUUID() == uuid) {
                return user;
            }
        }
        return null;
    }
    
    public void add(final User user) {
        this.allUsers.add(user);
    }
    
    public void remove(final User user) {
        this.allUsers.remove(user);
    }
}

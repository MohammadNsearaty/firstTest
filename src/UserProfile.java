import java.util.ArrayList;
import java.util.Date;

public class UserProfile {
    private String userName;
    private String password;
    private String email;
    private Date birthDate;
    private ArrayList<String> userFriends;
    private ArrayList<String> blockList;
    private Date joinDate;
    private ArrayList<Message> currMessages;



    public boolean addFriend(UserProfile friend){return true;};
    public void deleteFriend(String friendName){}
    public boolean blockUser(String name){return true;}
    public boolean unblockUser(String name){return true;}
    public boolean searchInFriendList(String name){return true;}
    public boolean searchInBlockList(String name){return true;}
    public void handleCurrMessages(){}
    public ArrayList<Message> checkTheServer(){return null;}
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}

/**
 * Created by Allen.C on 2016/11/25.
 */
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

public class DicClient implements DicConstants {
    public String username;
    private String password;
    public boolean online;

    public DicClient() {
        online = false;
    }

    public void setData(String user, String pass) {
        username = user;
        password = pass;
    }

    public DicClient(String usernameData, String passwordData) {
        username = usernameData;
        password = passwordData;
        online = false;
    }

    public boolean login(DataOutputStream toServer, DataInputStream fromServer) throws IOException {
        toServer.writeInt(LOGIN);
        toServer.writeInt(username.length());
        toServer.writeInt(password.length());
        toServer.writeChars(username);
        toServer.writeChars(password);
        boolean loginStatus = fromServer.readBoolean();
        if(loginStatus)
            online = true;
        return loginStatus;
    }

    public boolean nameCheck(DataOutputStream toServer, DataInputStream fromServer) throws IOException {
        toServer.writeInt(CHECKUSERNAME);
        toServer.writeInt(username.length());
        toServer.writeChars(username);
        boolean nameExists = fromServer.readBoolean();
        return !nameExists;
    }

    public boolean register(DataOutputStream toServer, DataInputStream fromServer) throws IOException {
        toServer.writeInt(REGISTER);
        toServer.writeInt(username.length());
        toServer.writeInt(password.length());
        toServer.writeChars(username);
        toServer.writeChars(password);
        boolean registerStatus = fromServer.readBoolean();
        return registerStatus;
    }

    public void logout(DataOutputStream toServer) throws IOException {
        toServer.writeInt(LOGOUT);
        toServer.writeInt(username.length());
        toServer.writeChars(username);
        online = false;
    }

    public int[] getLikeNum(DataOutputStream toServer, DataInputStream fromServer, String word) throws  IOException {
        toServer.writeInt(GETRANK);
        toServer.writeInt(word.length());
        toServer.writeChars(word);
        int likeNum[] = new int[NUMOFDICS];
        for (int i = 0; i < likeNum.length; i++)
            likeNum[i] = fromServer.readInt();
        return likeNum;
    }

    public void likeBaidu(DataOutputStream toServer, String word) throws IOException {
        toServer.writeInt(LIKE);
        toServer.writeInt(BAIDU);
        toServer.writeInt(word.length());
        toServer.writeChars(word);
    }

    public void likeYoudao(DataOutputStream toServer, String word) throws IOException {
        toServer.writeInt(LIKE);
        toServer.writeInt(YOUDAO);
        toServer.writeInt(word.length());
        toServer.writeChars(word);
    }

    public void likeBing(DataOutputStream toServer, String word) throws IOException {
        toServer.writeInt(LIKE);
        toServer.writeInt(BING);
        toServer.writeInt(word.length());
        toServer.writeChars(word);
    }

    public Vector <String> viewOnlineUsers(DataOutputStream toServer, DataInputStream fromServer) throws IOException {
        toServer.writeInt(ONLINEUSERS);
        int onlineUserNum = fromServer.readInt();
        Vector <String> onlineUsers = new Vector<>();
        for (int i = 0; i < onlineUserNum; i++) {
            String currentUser = "";
            int len = fromServer.readInt();
            for (int j = 0; j < len; j++)
                currentUser += fromServer.readChar();
            onlineUsers.add(currentUser);
        }
        return onlineUsers;
    }

    public Vector <String> viewOfflineUsers(DataOutputStream toServer, DataInputStream fromServer) throws IOException {
        toServer.writeInt(OFFLINEUSERS);
        int offlineUserNum = fromServer.readInt();
        Vector <String> offlineUsers = new Vector<>();
        for (int i = 0; i < offlineUserNum; i++) {
            String currentUser = "";
            int len = fromServer.readInt();
            for (int j = 0; j < len; j++)
                currentUser += fromServer.readChar();
            offlineUsers.add(currentUser);
        }
        return offlineUsers;
    }

    public void sendWordCard(DataOutputStream toServer, String recipient, String word, String sender) throws IOException {
        toServer.writeInt(SENDWORD);
        toServer.writeInt(recipient.length());
        toServer.writeChars(recipient);
        toServer.writeInt(word.length());
        toServer.writeChars(word);
        toServer.writeInt(sender.length());
        toServer.writeChars(sender);
    }

    public Vector <String> getWordCard(DataOutputStream toServer, DataInputStream fromServer, String user) throws IOException {
        toServer.writeInt(GETCARDS);
        toServer.writeInt(user.length());
        toServer.writeChars(user);
        Vector <String> Cards = new Vector<>();
        int unreadNum = fromServer.readInt();
        for (int i = 0; i < unreadNum; i++) {
            String cardData = "(未读) ";
            int wordLen = fromServer.readInt();
            for (int j = 0; j < wordLen; j++)
                cardData += fromServer.readChar();
            cardData += '.';
            int dicNum = fromServer.readInt();
            switch (dicNum) {
                case 0: cardData += "Baidu"; break;
                case 1: cardData += "Youdao"; break;
                case 2: cardData += "Bing"; break;
                default: System.out.println("获取单词卡信息错误！"); break;
            }
            cardData += ".from ";
            int SenderLen = fromServer.readInt();
            for (int j = 0; j < SenderLen; j++)
                cardData += fromServer.readChar();
            cardData += ".to ";
            cardData += username;
            Cards.add(cardData);
        }
        int readNum = fromServer.readInt();
        for (int i = 0; i < readNum; i++) {
            String cardData = "";
            int wordLen = fromServer.readInt();
            for (int j = 0; j < wordLen; j++)
                cardData += fromServer.readChar();
            cardData += '.';
            int dicNum = fromServer.readInt();
            switch (dicNum) {
                case 0: cardData += "Baidu"; break;
                case 1: cardData += "Youdao"; break;
                case 2: cardData += "Bing"; break;
                default: System.out.println("获取单词卡信息错误！"); break;
            }
            cardData += ".from ";
            int SenderLen = fromServer.readInt();
            for (int j = 0; j < SenderLen; j++)
                cardData += fromServer.readChar();
            cardData += ".to ";
            cardData += username;
            Cards.add(cardData);
        }
        return Cards;
    }

    public void readWordCard(DataOutputStream toServer, String recipient, String word, String sender) throws IOException {
        toServer.writeInt(READCARDS);
        toServer.writeInt(recipient.length());
        toServer.writeChars(recipient);
        toServer.writeInt(word.length());
        toServer.writeChars(word);
        toServer.writeInt(sender.length());
        toServer.writeChars(sender);
    }
}

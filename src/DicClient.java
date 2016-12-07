/**
 * Created by Allen.C on 2016/11/25.
 */
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class DicClient implements DicConstants {
    public String username;
    private String password;
    public boolean online;

    public DicClient(String usernameData, String passwordData) throws IOException {
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
}

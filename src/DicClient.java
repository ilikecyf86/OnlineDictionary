/**
 * Created by Allen.C on 2016/11/25.
 */
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class DicClient implements DicConstants {
    public String username;
    private String password;
    private boolean online;

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
}

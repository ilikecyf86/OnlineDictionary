/**
 * Created by Allen.C on 2016/11/25.
 */
import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class DicClient implements DicConstants {
    public static void main(String[] args) throws IOException {
        DicClient client = new DicClient("QQQ", "qqqqqqqq");
        client.register();
        client.login();
        client.logout();
    }

    public String username;
    private String password;
    private boolean online;
    private Socket socket;
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    public DicClient(String usernameData, String passwordData) throws IOException {
        username = usernameData;
        password = passwordData;
        online = false;
        socket = new Socket("172.26.88.169", 8000);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }

    public boolean login() throws IOException {
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

    public boolean nameCheck() throws IOException {
        toServer.writeInt(CHECKUSERNAME);
        toServer.writeInt(username.length());
        toServer.writeChars(username);
        boolean nameExists = fromServer.readBoolean();
        return !nameExists;
    }

    public boolean register() throws IOException {
        toServer.writeInt(REGISTER);
        toServer.writeInt(username.length());
        toServer.writeInt(password.length());
        toServer.writeChars(username);
        toServer.writeChars(password);
        boolean registerStatus = fromServer.readBoolean();
        return registerStatus;
    }

    public void logout() throws IOException {
        toServer.writeInt(LOGOUT);
        toServer.writeInt(username.length());
        toServer.writeChars(username);
        online = false;
    }
}

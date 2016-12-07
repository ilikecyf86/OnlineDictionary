/**
 * Created by Allen.C on 2016/11/25.
 */
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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

    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost", 8000);
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());
            String username = "user2";
            String password = "password2";
            toServer.writeInt(LOGIN);
            toServer.writeInt(username.length());
            toServer.writeInt(password.length());
            toServer.writeChars(username);
            toServer.writeChars(password);
            boolean success = fromServer.readBoolean();

//get online users example
            System.out.println("get online users");
            toServer.writeInt(ONLINEUSERS);
            ArrayList <String> onlineUsers = new ArrayList<String>();
            int num = fromServer.readInt();
            for(int i = 0; i < num; i++){
                int len = fromServer.readInt();
                //System.out.println(len);
                char[] name = new char[len];
                for(int j = 0; j < len; j++){
                    name[j] = fromServer.readChar();
                }
                String strName = new String(name);
                onlineUsers.add(strName);
            }
            for(int i = 0; i < num; i++){
                System.out.println(onlineUsers.get(i));
            }

//get offline users example
            System.out.println("get offline users");
            toServer.writeInt(OFFLINEUSERS);
            ArrayList <String> offlineUsers = new ArrayList<String>();
            num = fromServer.readInt();
            for(int i = 0; i < num; i++){
                int len = fromServer.readInt();
                //System.out.println(len);
                char[] name = new char[len];
                for(int j = 0; j < len; j++){
                    name[j] = fromServer.readChar();
                }
                String strName = new String(name);
                offlineUsers.add(strName);
            }
            for(int i = 0; i < num; i++){
                System.out.println(offlineUsers.get(i));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

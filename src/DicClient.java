import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by cyf on 2016/11/22.
 */
public class DicClient implements DicConstants{
    public static void main(String[] args){
        try {
            //记录客户端的登录状态，发送请求前需要先判断是否已登录
            boolean logined = false;
            String loginedUsername = null;

            Socket socket = new Socket("localhost", 8000);
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());

            //login example
            String username1 = "username";
            String password1 = "passwd";
            toServer.writeInt(LOGIN);
            toServer.writeInt(username1.length());//username length
            toServer.writeInt(password1.length());//password length
            toServer.writeChars(username1);
            toServer.writeChars(password1);
            boolean successLogin = fromServer.readBoolean();
            if(successLogin){
                logined = true;
                loginedUsername = new String(username1);
            }
            System.out.println("login using username: " + username1 + ", password: " + password1 + "  "+ successLogin);

            //logout example
            String username4 = "username";
            toServer.writeInt(LOGOUT);
            toServer.writeInt(username4.length());
            toServer.writeChars(username4);
            logined = false;
            loginedUsername = null;

            //register example
            String username2 = "user2";
            String password2 = "password2";
            toServer.writeInt(REGISTER);
            toServer.writeInt(username2.length());//username length
            toServer.writeInt(password2.length());//password length
            toServer.writeChars(username2);
            toServer.writeChars(password2);
            boolean successRegister = fromServer.readBoolean();
            System.out.println("register using username: " + username2 + ", password: " + password2 + "  "+ successRegister);

            //check username example
            String username3 = "user1";
            toServer.writeInt(CHECKUSERNAME);
            toServer.writeInt(username3.length());//username length
            toServer.writeChars(username3);
            boolean usernameExists = fromServer.readBoolean();
            System.out.println("check existence of username: " + username3 + "  " + usernameExists);

            //like baidu example
            toServer.writeInt(LIKE);
            toServer.writeInt(BAIDU);
            String word = "car";
            toServer.writeInt(word.length());
            toServer.writeChars(word);

            //get like numbers example
            toServer.writeInt(GETRANK);
            word = "no";
            toServer.writeInt(word.length());
            toServer.writeChars(word);
            int []likes = new int[NUMOFDICS];
            for(int i = 0; i < likes.length; i++){
                likes[i] = fromServer.readInt();
            }
            System.out.print("likes for baidu, youdao and bing: ");
            for(int i = 0; i < likes.length; i++) {
                System.out.print(likes[i] + " ");
            }

            socket.close();
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

}

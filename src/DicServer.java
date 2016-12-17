import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created by cyf on 2016/11/22.
 */


public class DicServer extends JFrame implements DicConstants{
    //建立一个TextArea，显示服务器当前进行的操作，方便DEBUG
    private  JTextArea jta = new JTextArea();
    private Set<String> onlineUsers = new TreeSet<String>();
    public static void main(String[] args){
        new DicServer();
    }

    public DicServer(){
        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);
        setTitle("Dictionary Server");
        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        try{
            ServerSocket serverSocket = new ServerSocket(8000);
            jta.append("Dictionary server started at " + new Date() + "\n");

            int clientNo = 1;
            while(true){
                Socket clientSocket = serverSocket.accept();
                jta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');
                InetAddress inetAddress = clientSocket.getInetAddress();
                jta.append("Client " + clientNo +"'s host name is " + inetAddress.getHostName() + '\n');
                jta.append("Client " + clientNo +"'s IP address is " + inetAddress.getHostAddress() + '\n');
                HandleAClient task = new HandleAClient(clientSocket, clientNo);
                new Thread(task).start();
                clientNo++;
            }
        }
        catch (IOException ex){
            System.err.println(ex);
        }
    }

    class HandleAClient implements Runnable{
        private Socket socket;
        private int num;
        public HandleAClient(Socket socket, int num){
            this.socket = socket;
            this.num = num;
        }

        public void run(){
            try{
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                while(true){
                    int command = inputFromClient.readInt();
                    switch (command) {
                        case LOGIN:
                            int usernameLenLogin = inputFromClient.readInt();
                            int passwordLenLogin = inputFromClient.readInt();
                            //System.out.println(usernameLenLogin);
                            //System.out.println(passwordLenLogin);
                            char[] usernameLoginC = new char[usernameLenLogin];
                            char[] passwordLoginC = new char[passwordLenLogin];
                            for(int i = 0; i < usernameLenLogin; i++){
                                usernameLoginC[i] = inputFromClient.readChar();
                            }
                            for(int i = 0; i < passwordLenLogin; i++){
                                passwordLoginC[i] = inputFromClient.readChar();
                            }
                            String usernameLogin = new String(usernameLoginC);
                            String passwordLogin = new String(passwordLoginC);
                            boolean successLogin = (!onlineUsers.contains(usernameLogin) && DBConnect.login(usernameLogin, passwordLogin));
                            if(successLogin == true)
                                onlineUsers.add(usernameLogin);
                            outputToClient.writeBoolean(successLogin);
                            jta.append("Client No." + num + " login using username: " + usernameLogin + " (" + successLogin +  ")\n");
                            break;
                        case REGISTER:
                            int usernameLenRegister = inputFromClient.readInt();
                            int passwordLenRegister = inputFromClient.readInt();
                            char[] usernameRegisterC = new char[usernameLenRegister];
                            char[] passwordRegisterC = new char[passwordLenRegister];
                            for(int i = 0; i < usernameLenRegister; i++){
                                usernameRegisterC[i] = inputFromClient.readChar();
                            }
                            for(int i = 0; i < passwordLenRegister; i++){
                                passwordRegisterC[i] = inputFromClient.readChar();
                            }
                            String usernameRegister = new String(usernameRegisterC);
                            String passwordRegister = new String(passwordRegisterC);
                            boolean successRegister = DBConnect.register(usernameRegister, passwordRegister);
                            if(successRegister){
                                DBConnect_wordCard.addTable(usernameRegister);
                            }
                            outputToClient.writeBoolean(successRegister);
                            jta.append("Client No." + num + " register using username: " + usernameRegister + " (" + successRegister +  ")\n");
                            break;
                        case CHECKUSERNAME:
                            int usernameLenCheck = inputFromClient.readInt();
                            char[] usernameCheckC = new char[usernameLenCheck];
                            for(int i = 0; i < usernameLenCheck; i++){
                                usernameCheckC[i] = inputFromClient.readChar();
                            }
                            String usernameCheck = new String(usernameCheckC);
                            boolean usernameExists = DBConnect.usernameExists(usernameCheck);
                            outputToClient.writeBoolean(usernameExists);
                            jta.append("Client No." + num + " check username: " + usernameCheck + " (" + usernameExists +  ")\n");
                            break;
                        case LIKE:
                            int type = inputFromClient.readInt();
                            int wordLen = inputFromClient.readInt();
                            char[] wordC = new char[wordLen];
                            for(int i = 0; i < wordLen; i++){
                                wordC[i] = inputFromClient.readChar();
                            }
                            String word = new String(wordC);
                            switch (type){
                                case BAIDU:
                                    DBConnect_likeNum.likeBaidu(word);
                                    jta.append("Client No." + num + " liked: baidu for word: " + word + "\n");
                                    break;
                                case YOUDAO:
                                    DBConnect_likeNum.likeYoudao(word);
                                    jta.append("Client No." + num + " liked: youdao for word: " + word + "\n");
                                    break;
                                case BING:
                                    DBConnect_likeNum.likeBing(word);
                                    jta.append("Client No." + num + " liked: bing for word: " + word + "\n");
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case GETRANK:
                            int wordLenRank = inputFromClient.readInt();
                            char[] wordRankC = new char[wordLenRank];
                            for(int i = 0; i < wordLenRank; i++){
                                wordRankC[i] = inputFromClient.readChar();
                            }
                            String wordRank = new String(wordRankC);
                            int[] result = new int[NUMOFDICS];
                            result = DBConnect_likeNum.getLikes(wordRank);
                            jta.append("Client No." + num + " got the like numbers for word: " + wordRank + "\n");
                            for(int i = 0; i < result.length; i++)
                                outputToClient.writeInt(result[i]);
                            break;
                        case LOGOUT:
                            int usernameLenLogout = inputFromClient.readInt();
                            char[] usernameLogoutC = new char[usernameLenLogout];
                            for(int i = 0; i < usernameLenLogout; i++){
                                usernameLogoutC[i] = inputFromClient.readChar();
                            }
                            String usernameLogout = new String(usernameLogoutC);
                            jta.append("Client No." + num + " logout username:" + usernameLogout + "\n");
                            onlineUsers.remove(usernameLogout);
                            break;
                        case ONLINEUSERS:
                            jta.append("Client No." + num + " get all online usernames\n");
                            int onlineNum = onlineUsers.size();
                            outputToClient.writeInt(onlineNum);
                            Iterator<String> iterator1 = onlineUsers.iterator();
                            while (iterator1.hasNext()) {
                                String tempName = iterator1.next();
                                outputToClient.writeInt(tempName.length());
                                outputToClient.writeChars(tempName);
                            }
                            break;
                        case OFFLINEUSERS:
                            jta.append("Client No." + num + " get all offline usernames\n");
                            ArrayList <String> allUsers = DBConnect.getAllUsers();
                            allUsers.removeAll(onlineUsers);
                            int offlineNum = allUsers.size();
                            outputToClient.writeInt(offlineNum);
                            for(int i = 0; i < offlineNum; i++) {
                                String tempName = allUsers.get(i);
                                outputToClient.writeInt(tempName.length());
                                outputToClient.writeChars(tempName);
                            }
                            break;
                        case SENDWORD:
                            int sendWordReceiverLen = inputFromClient.readInt();
                            char[] sendWordReceiverC = new char[sendWordReceiverLen];
                            for(int i = 0; i < sendWordReceiverLen; i++){
                                sendWordReceiverC[i] = inputFromClient.readChar();
                            }
                            String sendWordReceiver = new String(sendWordReceiverC);

                            int sendWordLen = inputFromClient.readInt();
                            char[] sendWordC = new char[sendWordLen];
                            for(int i = 0; i < sendWordLen; i++){
                                sendWordC[i] = inputFromClient.readChar();
                            }
                            String sendWord = new String(sendWordC);

                            int sendWordSenderLen = inputFromClient.readInt();
                            char[] sendWordSenderC = new char[sendWordSenderLen];
                            for(int i = 0; i < sendWordSenderLen; i++){
                                sendWordSenderC[i] = inputFromClient.readChar();
                            }
                            String sendWordSender = new String(sendWordSenderC);
                            jta.append("Client No." + num + " send word: " + sendWord + ", from: " + sendWordSender + ", to:" + sendWordReceiver + "\n");
                            DBConnect_wordCard.sendWord(sendWord,sendWordSender,sendWordReceiver);
                            break;
                        case GETCARDS:
                            int getCardsLen = inputFromClient.readInt();
                            char[] getCardsUsernameC = new char[getCardsLen];
                            for(int i = 0; i < getCardsLen; i++){
                                getCardsUsernameC[i] = inputFromClient.readChar();
                            }
                            String getCardsUsername = new String(getCardsUsernameC);
                            jta.append("Client No." + num + " get word cards of user: " + getCardsUsername + "\n");

                            ArrayList []newCardsArrayLists = DBConnect_wordCard.getNewCards(getCardsUsername);
                            ArrayList <String> newCards = newCardsArrayLists[0];
                            ArrayList <String> newCardsSenders = newCardsArrayLists[1];
                            ArrayList <Integer> newCardsDicNo = newCardsArrayLists[2];
                            int newCardsSize = newCardsArrayLists[0].size();
                            outputToClient.writeInt(newCardsSize);
                            for(int i = 0; i < newCardsSize; i++){
                                outputToClient.writeInt(newCards.get(i).length());
                                outputToClient.writeChars(newCards.get(i));
                                outputToClient.writeInt(newCardsDicNo.get(i));
                                outputToClient.writeInt(newCardsSenders.get(i).length());
                                outputToClient.writeChars(newCardsSenders.get(i));
                            }

                            ArrayList []oldCardsArrayLists = DBConnect_wordCard.getOldCards(getCardsUsername);
                            ArrayList <String> oldCards = oldCardsArrayLists[0];
                            ArrayList <String> oldCardsSenders = oldCardsArrayLists[1];
                            ArrayList <Integer> oldCardsDicNo = oldCardsArrayLists[2];
                            int oldCardsSize = oldCardsArrayLists[0].size();
                            outputToClient.writeInt(oldCardsSize);
                            for(int i = 0; i < oldCardsSize; i++){
                                outputToClient.writeInt(oldCards.get(i).length());
                                outputToClient.writeChars(oldCards.get(i));
                                outputToClient.writeInt(oldCardsDicNo.get(i));
                                outputToClient.writeInt(oldCardsSenders.get(i).length());
                                outputToClient.writeChars(oldCardsSenders.get(i));
                            }
                            break;
                        case READCARDS:
                            int readCardReceiverLen = inputFromClient.readInt();
                            char[] readCardReceiverC = new char[readCardReceiverLen];
                            for(int i = 0; i < readCardReceiverLen; i++){
                                readCardReceiverC[i] = inputFromClient.readChar();
                            }
                            String readCardReceiver = new String(readCardReceiverC);

                            int readCardLen = inputFromClient.readInt();
                            char[] readCardC = new char[readCardLen];
                            for(int i = 0; i < readCardLen; i++){
                                readCardC[i] = inputFromClient.readChar();
                            }
                            String readCard = new String(readCardC);

                            int readCardSenderLen = inputFromClient.readInt();
                            char[] readCardSenderC = new char[readCardSenderLen];
                            for(int i = 0; i < readCardSenderLen; i++){
                                readCardSenderC[i] = inputFromClient.readChar();
                            }
                            String readCardSender = new String(readCardSenderC);

                            jta.append("Client No." + num + " read word card: " + readCard + ", receiver:" + readCardReceiver + ", sender: " + readCardSender + "\n");
                            DBConnect_wordCard.setReadFlag(readCardReceiver, readCard, readCardSender);
                    }
                }
            }
            catch(SocketException ex){
                //socket.close();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }
    }
}

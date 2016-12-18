import javax.swing.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by cyf on 2016/12/16.
 */
//连接单词卡片数据库
public class DBConnect_wordCard implements DicConstants{
    private static Connection connection;

    //连接数据库
    private static void connectDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/wordcard?useUnicode=true&characterEncoding=utf-8&useSSL=false", "cyf", "3754567");
            //System.out.println("数据库连接成功");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,"数据库连接失败");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
    }

    //断开数据库连接
    private static void disconnectDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //为一个用户新添加一张表格，存放该用户的单词卡片，该函数在用户注册时被调用
    public static void addTable(String username){
        connectDB();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table if not exists " + username + " (word varchar(32), sender varchar(16), readFlag integer, dicNo integer)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
    }

    //在数据库中添加一条记录，由sender发送的单词word，接受者为receiver
    public static void sendWord(String word, String sender, String receiver) {
        connectDB();
        try {
            Statement statement = connection.createStatement();
            //以下几行用于获得该单词目前点赞数最多的词典，将该释义作为单词卡片中的内容
            int[] likeNum = new int[3];
            likeNum = DBConnect_likeNum.getLikes(word);
            int no = 0;
            int max = 0;
            for (int i = 0; i < likeNum.length; i++) {
                if (likeNum[i] > max) {
                    max = likeNum[i];
                    no = i;
                }
            }
            ResultSet rs = statement.executeQuery("select * from " + receiver + " where word = '" + word + "' and sender = '" + sender + "' and dicNo = " + no);
            if(!rs.next())
                statement.executeUpdate(("insert into " + receiver + "(word, sender, readFlag, dicNo) values('" + word + "', '" + sender + "', " + "0" + ", " + no + ")"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectDB();
    }

    //返回某个用户的未读单词卡，单词卡中有3个内容，单词、释义的词典号、发送者
    public static ArrayList[] getNewCards(String username){
        connectDB();
        ResultSet rs;
        ArrayList []arrayLists = new ArrayList[3];
        ArrayList <String> newCards = new ArrayList<String>();
        ArrayList <String> newCardsSenders = new ArrayList<String>();
        ArrayList <Integer> newCardsDicNo = new ArrayList<Integer>();
        arrayLists[0] = newCards;
        arrayLists[1] = newCardsSenders;
        arrayLists[2] = newCardsDicNo;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT word, sender, dicNo FROM " + username + " WHERE readFlag = 0");
            while (rs.next()) {
                arrayLists[0].add(rs.getString(1));
                arrayLists[1].add(rs.getString(2));
                arrayLists[2].add(rs.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectDB();
        return arrayLists;
    }

    //返回某个用户的已读单词卡，单词卡中有3个内容，单词、释义的词典号、发送者
    public static ArrayList[] getOldCards(String username){
        connectDB();
        ResultSet rs;
        ArrayList []arrayLists = new ArrayList[3];
        ArrayList <String> oldCards = new ArrayList<String>();
        ArrayList <String> oldCardsSenders = new ArrayList<String>();
        ArrayList <Integer> oldCardsDicNo = new ArrayList<Integer>();
        arrayLists[0] = oldCards;
        arrayLists[1] = oldCardsSenders;
        arrayLists[2] = oldCardsDicNo;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT word, sender, dicNo FROM " + username + " WHERE readFlag = 1");
            while (rs.next()) {
                arrayLists[0].add(rs.getString(1));
                arrayLists[1].add(rs.getString(2));
                arrayLists[2].add(rs.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectDB();
        return arrayLists;
    }

    //将某个用户的某张单词卡设为已读
    public static void setReadFlag(String receiver, String word, String sender){
        connectDB();

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE " + receiver + " SET readFlag = 1 WHERE word = '" + word + "' AND sender = '" + sender + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectDB();
    }
}

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Created by cyf on 2016/11/21.
 */
//连接存储点赞信息的数据库
public class DBConnect_likeNum {
    private static Connection connection;

    //连接数据库
    private static void connectDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/likeNum?useUnicode=true&characterEncoding=utf-8&useSSL=false", "cyf", "3754567");
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

    //断开连接数据库
    private static void disconnectDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //为某个单词的百度释义点赞
    public static void likeBaidu(String word){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate("create table if not exists likes(dicName VARCHAR(16) not null, num INTEGER DEFAULT 0, PRIMARY KEY (dicName))");
            String dicName = "baidu";
            rs = statement.executeQuery("select " + dicName + " from likes where word = '"+ word +"'");
            //若数据库中没有该单词，则添加一条该单词的记录
            if(rs.next() == false){
                statement.executeUpdate("insert into likes(word, baidu, youdao, bing) values('" + word + "', 1, 0, 0)");
            }
            //若有该单词，则百度的点赞数+1
            else {
                int likeNum = -1;
                likeNum = rs.getInt(dicName);
                //System.out.println(likeNum);
                likeNum++;
                statement.executeUpdate("update likes set " + dicName + " = " + likeNum + " where word = '" + word + "'");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
    }

    //为某个单词的有道释义点赞
    public static void likeYoudao(String word){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate("create table if not exists likes(dicName VARCHAR(16) not null, num INTEGER DEFAULT 0, PRIMARY KEY (dicName))");
            String dicName = "youdao";
            rs = statement.executeQuery("select " + dicName + " from likes where word = '"+ word +"'");
            //若数据库中没有该单词，则添加一条该单词的记录
            if(rs.next() == false){
                statement.executeUpdate("insert into likes(word, baidu, youdao, bing) values('" + word + "', 0, 1, 0)");
            }
            //若有该单词，则有道的点赞数+1
            else {
                int likeNum = -1;
                likeNum = rs.getInt(dicName);
                //System.out.println(likeNum);
                likeNum++;
                statement.executeUpdate("update likes set " + dicName + " = " + likeNum + " where word = '" + word + "'");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
    }

    //为某个单词的必应释义点赞
    public static void likeBing(String word){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate("create table if not exists likes(dicName VARCHAR(16) not null, num INTEGER DEFAULT 0, PRIMARY KEY (dicName))");
            String dicName = "bing";
            rs = statement.executeQuery("select " + dicName + " from likes where word = '"+ word +"'");
            //若数据库中没有该单词，则添加一条该单词的记录
            if(rs.next() == false){
                statement.executeUpdate("insert into likes(word, baidu, youdao, bing) values('" + word + "', 0, 0, 1)");
            }
            //若有该单词，则必应的点赞数+1
            else {
                int likeNum = -1;
                likeNum = rs.getInt(dicName);
                //System.out.println(likeNum);
                likeNum++;
                statement.executeUpdate("update likes set " + dicName + " = " + likeNum + " where word = '" + word + "'");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
    }

    //返回某个单词的三个词典的点赞数，存放在一个长度为3的int数组中，分别对应百度、有道、必应
    public static int[] getLikes(String word){
        String[] dicName = {"baidu", "youdao", "bing"};
        int dicNum = dicName.length;
        int[] likes = new int[dicNum];
        for(int i = 0; i < dicNum; i++)
            likes[i] = 0;
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate("create table if not exists likes(dicName VARCHAR(16) not null, num INTEGER DEFAULT 0, PRIMARY KEY (dicName))");
            rs = statement.executeQuery("select * from likes where word = '" + word + "'");
            while (rs.next()) {
                for(int i = 0; i < dicNum; i++) {
                    likes[i] = rs.getInt(dicName[i]);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
        return likes;
    }

    //测试用main函数
    /*public static void main(String[] args) {
        int[] likes = getLikes();
        for(int i = 0; i < likes.length; i++){
            System.out.println(likes[i]);
        }
    }*/
}

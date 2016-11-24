import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Created by cyf on 2016/11/21.
 */
public class DBConnect_likeNum {
    private static Connection connection;

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

    private static void disconnectDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void likeBaidu(String word){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate("create table if not exists likes(dicName VARCHAR(16) not null, num INTEGER DEFAULT 0, PRIMARY KEY (dicName))");
            String dicName = "baidu";
            rs = statement.executeQuery("select " + dicName + " from likes where word = '"+ word +"'");
            if(rs.next() == false){
                statement.executeUpdate("insert into likes(word, baidu, youdao, bing) values('" + word + "', 1, 0, 0)");
            }
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

    public static void likeYoudao(){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate("create table if not exists likes(dicName VARCHAR(16) not null, num INTEGER DEFAULT 0, PRIMARY KEY (dicName))");
            String dicName = "youdao";
            rs = statement.executeQuery("select num from likes where dicName = '"+ dicName +"'");
            int likeNum = -1;
            while(rs.next())
                likeNum = rs.getInt("num");
            //System.out.println(likeNum);
            likeNum++;
            statement.executeUpdate("update likes set num = " + likeNum + " where dicName = '"+ dicName +"'");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
    }

    public static void likeBing(){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate("create table if not exists likes(dicName VARCHAR(16) not null, num INTEGER DEFAULT 0, PRIMARY KEY (dicName))");
            String dicName = "bing";
            rs = statement.executeQuery("select num from likes where dicName = '"+ dicName +"'");
            int likeNum = -1;
            while(rs.next())
                likeNum = rs.getInt("num");
            //System.out.println(likeNum);
            likeNum++;
            statement.executeUpdate("update likes set num = " + likeNum + " where dicName = '"+ dicName +"'");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
    }

    public static int[] getLikes(String word){
        String[] dicName = {"baidu", "youdao", "bing"};
        int dicNum = dicName.length;
        int[] likes = new int[dicNum];
        for(int i = 0; i < dicNum; i++)
            likes[i] = -1;
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
    /*public static void main(String[] args) {
        int[] likes = getLikes();
        for(int i = 0; i < likes.length; i++){
            System.out.println(likes[i]);
        }
    }*/
}

import javax.swing.*;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by cyf on 2016/11/21.
 */

//连接存储账户信息的数据库
public class DBConnect {

    private static Connection connection;

    //获取连接
    private static void connectDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/dicacc?useUnicode=true&characterEncoding=utf-8&useSSL=false", "cyf", "3754567");
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

    //断开连接
    private static void disconnectDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //用username和password登录，返回登录是否成功的bool值
    public static boolean login(String username, String password) {
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("select username, password from account");
            while(rs.next()){
                String tempUsername = rs.getString(1);
                String tempPassword = rs.getString(2);
                if(tempUsername.equals(username) && tempPassword.equals(password)){
                    disconnectDB();
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"数据库连接错误");
            e.printStackTrace();
        }
        disconnectDB();
        return false;
    }

    //注册时使用，判断是否已存在用户名
    public static boolean usernameExists(String username){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table if not exists account(username varchar(16) not null, password varchar(16) not null, primary key (username))");
            rs = statement.executeQuery("select username from account");
            while(rs.next()){
                String tempUsername = rs.getString(1);
                if(tempUsername.equals(username)){
                    disconnectDB();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectDB();
        return false;
    }

    //用username和password注册一个新账户，返回注册是否成功
    public static boolean register(String username, String password){
        connectDB();
        ResultSet rs;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table if not exists account(username varchar(16) not null, password varchar(16) not null, primary key (username))");
            rs = statement.executeQuery("select username from account");
            while(rs.next()){
                String tempUsername = rs.getString(1);
                if(tempUsername.equals(username)){
                    disconnectDB();
                    return false;
                }
            }
            statement.executeUpdate("insert into account(username, password) values('"+username+"', '"+password+"')");
            disconnectDB();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectDB();
        return false;
    }

    //返回所有用户的用户名
    public static ArrayList <String> getAllUsers() {
        connectDB();
        ResultSet rs;
        ArrayList<String> users = new ArrayList<String>();
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT username FROM account");
            while (rs.next()) {
                String tempUsername = rs.getString(1);
                users.add(tempUsername);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectDB();
        return users;
    }
}

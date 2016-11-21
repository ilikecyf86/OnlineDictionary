import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by cyf on 2016/11/21.
 */
public class DBConnect {

    private static Connection connection;

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

    private static void disconnectDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public static void main(String[] args) throws Exception {
        if(usernameExists("user1") == true)
            System.out.println("user1 exists");
        else
            System.out.println("user1 doesn't exist");
        System.out.println();

        if(login("user1", "password1") == true)
            System.out.println("user1 login success");
        else
            System.out.println("user1 login fail");
        System.out.println();

        if(register("user1", "password1") == true)
            System.out.println("user1 register success");
        else
            System.out.println("user1 register fail");
        System.out.println();

        if(login("user1", "password1") == true)
            System.out.println("user1 login success");
        else
            System.out.println("user1 login fail");
        System.out.println();

        if(login("user2", "password2") == true)
            System.out.println("user2 login success");
        else
            System.out.println("user2 login fail");
    }
}

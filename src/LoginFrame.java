/**
 * Created by Allen.C on 2016/11/20.
 */
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginFrame extends JFrame {
    private JPanel panel = new JPanel();
    private JButton login = new JButton("登陆");
    private JButton register = new JButton("注册");
    private JTextField usernameText = new JTextField(18);
    private JPasswordField passwordText = new JPasswordField(18);

    public Socket socket;
    public DataOutputStream toServer;
    public DataInputStream fromServer;

    public LoginFrame(final DictionaryFrame dicFrame) throws IOException {
        socket = new Socket("172.26.38.90", 8000);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
        System.out.println("成功连接到服务器。");

        panel.setLayout(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createEtchedBorder());

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.setBorder(new TitledBorder("用户名"));
        usernamePanel.add(usernameText);
        panel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.setBorder(new TitledBorder("密　码"));
        passwordPanel.add(passwordText);
        panel.add(passwordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        buttonPanel.add(login);
        buttonPanel.add(register);
        panel.add(buttonPanel);

        add(panel);
        setTitle("登陆&注册");
        setSize(250, 200);
        Rectangle rect = dicFrame.getBounds();
        setLocation(rect.x + 75, rect.y + 200);
        setResizable(false);

        login.addActionListener(e -> {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            if (username.length() <= 0)
                JOptionPane.showMessageDialog(this, "请输入用户名！", "WARNING", JOptionPane.WARNING_MESSAGE);
            else if (password.length() <= 0)
                JOptionPane.showMessageDialog(this, "请输入密码！", "WARNING", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    dicFrame.client.setData(username, password);
                    /* todo:检测用户名密码是否存在且匹配 */
                    boolean loginFlag = dicFrame.client.login(toServer, fromServer);
                    if (loginFlag) {
                        /* todo:登陆成功后的操作 */
                        System.out.println("登陆成功。");
                        dicFrame.loginSucceed();
                        setVisible(false);
                    } else
                        JOptionPane.showMessageDialog(this, "登陆失败！", "ERROR", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                passwordText.setText("");
            }
        });

        register.addActionListener(e -> {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            if (username.length() <= 0)
                JOptionPane.showMessageDialog(this, "请输入用户名！", "WARNING", JOptionPane.WARNING_MESSAGE);
            else if (password.length() <= 0)
                JOptionPane.showMessageDialog(this, "请输入密码！", "WARNING", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    dicFrame.client.setData(username, password);
                    boolean flagUsername = true;
                    /* todo:检测用户名是否符合规范 */
                    boolean checkUsername = judgeUsername(username);
                    if (!checkUsername) {
                        flagUsername = false;
                        JOptionPane.showMessageDialog(this, "用户名不符合规范！", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                    /* todo:检测用户名是否存在 */
                    else if (!dicFrame.client.nameCheck(toServer, fromServer)) {
                        flagUsername = false;
                        JOptionPane.showMessageDialog(this, "用户名已被注册！", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                    if (flagUsername) {
                        /* todo:检测密码是否符合规范 */
                        boolean checkPassword = judgePassword(password);
                        if (!checkPassword)
                            JOptionPane.showMessageDialog(this, "密码不符合规范！", "WARNING", JOptionPane.WARNING_MESSAGE);
                        else {
                            if (!dicFrame.client.register(toServer, fromServer))
                                JOptionPane.showMessageDialog(this, "注册失败！", "ERROR", JOptionPane.ERROR_MESSAGE);
                            else
                                JOptionPane.showMessageDialog(this, "注册成功！", "COMPLETE", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                passwordText.setText("");
            }
        });
    }

    public boolean judgeUsername(String username) {
        return username.length() <= 16;
    }

    public boolean judgePassword(String password) {
        if (password.length() < 6 || password.length() > 16)
            return false;
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= '0' && password.charAt(i) <= '9')
                continue;
            else if (password.charAt(i) >= 'a' && password.charAt(i) <= 'z')
                continue;
            else if (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z')
                continue;
            else
                return false;
        }
        return true;
    }
}

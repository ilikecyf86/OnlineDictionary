/**
 * Created by Allen.C on 2016/12/12.
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class SendFrame extends JFrame {
    private JPanel panel = new JPanel();
    private JList userList = new JList();
    private JScrollPane usersJSP = new JScrollPane(userList);
    private JButton send = new JButton("发送");

    private Vector onlineUsers;
    private Vector offlineUsers;

    public SendFrame(final DictionaryFrame dicFrame) {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEtchedBorder());
        usersJSP.setPreferredSize(new Dimension(180, 660));

        try {
            onlineUsers = dicFrame.client.viewOnlineUsers(dicFrame.loginFrame.toServer, dicFrame.loginFrame.fromServer);
            offlineUsers = dicFrame.client.viewOfflineUsers(dicFrame.loginFrame.toServer, dicFrame.loginFrame.fromServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Vector users = new Vector<String>();
        for (int i = 0; i < onlineUsers.size(); i++)
            users.add(" [在线] " + onlineUsers.elementAt(i));
        for (int i = 0; i < offlineUsers.size(); i++)
            users.add(" [离线] " + offlineUsers.elementAt(i));
        userList.setListData(users);
        panel.add(usersJSP);

        send.setPreferredSize(new Dimension(100, 40));
        panel.add(send);

        add(panel);
        setTitle("选择");
        setSize(200, 750);
        setLocation(645, 100);
        setResizable(false);

        send.addActionListener(e -> {
            int selected[] = userList.getSelectedIndices();
            int size = selected.length;
            System.out.print(size + ": ");
            String nameList[] = new String[size];
            for (int i = 0; i < selected.length; i++) {
                nameList[i] = users.elementAt(selected[i]).toString().substring(6);
                System.out.print(nameList[i] + " ");
            }
            System.out.println();
        });
    }
}

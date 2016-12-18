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

        userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        usersJSP.setPreferredSize(new Dimension(180, 660));

        try {
            onlineUsers = dicFrame.client.viewOnlineUsers(dicFrame.loginFrame.toServer, dicFrame.loginFrame.fromServer);
            offlineUsers = dicFrame.client.viewOfflineUsers(dicFrame.loginFrame.toServer, dicFrame.loginFrame.fromServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Vector <String> users = new Vector<>();
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
        Rectangle rect = dicFrame.getBounds();
        setLocation(rect.x + 400, rect.y);
        setResizable(false);

        send.addActionListener(e -> {
            int selected[] = userList.getSelectedIndices();
            int size = selected.length;
            System.out.print(size + ": ");
            String nameList[] = new String[size];
            for (int i = 0; i < size; i++) {
                nameList[i] = users.elementAt(selected[i]).toString().substring(6);
                System.out.print(nameList[i] + " ");
            }
            System.out.println();

            /* todo:向服务器发出发送单词卡请求 */
            try {
                for (int i = 0; i < size; i++)
                    dicFrame.client.sendWordCard(dicFrame.loginFrame.toServer, nameList[i], dicFrame.word, dicFrame.client.username);
                JOptionPane.showMessageDialog(this, "单词卡发送成功！", "COMPLETE", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
}

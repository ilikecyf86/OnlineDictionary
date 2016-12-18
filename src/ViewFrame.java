/**
 * Created by Allen.C on 2016/12/9.
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class ViewFrame extends JFrame {
    private JPanel panel = new JPanel();
    private JList userList = new JList();
    private JScrollPane usersJSP = new JScrollPane(userList);

    private Vector onlineUsers;
    private Vector offlineUsers;

    public ViewFrame(final DictionaryFrame dicFrame) {
        panel.setBorder(BorderFactory.createEtchedBorder());
        usersJSP.setPreferredSize(new Dimension(180, 705));
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
        /* todo:尝试分别修改在线用户和离线用户的颜色
        userList.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return null;
            }
        });*/
        panel.add(usersJSP);

        add(panel);
        setTitle("用户");
        setSize(200, 750);
        Rectangle rect = dicFrame.getBounds();
        setLocation(rect.x + 400, rect.y);
        setResizable(false);
    }
}

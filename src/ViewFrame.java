/**
 * Created by Allen.C on 2016/12/9.
 */
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ViewFrame extends JFrame {
    private JPanel panel = new JPanel();
    private JList userList = new JList();
    private JScrollPane usersJSP = new JScrollPane(userList);

    public ViewFrame(Vector <String> online, Vector <String> offline) {
        panel.setLayout(new GridLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());

        Vector users = new Vector<String>();
        for (int i = 0; i < online.size(); i++)
            users.add(" [在线] " + online.elementAt(i));
        for (int i = 0; i < offline.size(); i++)
            users.add(" [离线] " + offline.elementAt(i));
        userList.setListData(users);
        /*userList.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return null;
            }
        });*/
        panel.add(usersJSP);

        add(panel);
        setTitle("用户");
        setSize(200, 600);
        setLocation(595, 200);
        setResizable(false);
    }
}

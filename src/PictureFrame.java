/**
 * Created by Allen.C on 2016/12/12.
 */
import javax.swing.*;
import java.awt.*;

public class PictureFrame extends JFrame {
    private JPanel panel = new JPanel();
    private JList picList = new JList();
    private JScrollPane picJSP = new JScrollPane(picList);
    private JPanel picPanel = new JPanel();

    public PictureFrame (final DictionaryFrame dicFrame) {
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        picJSP.setPreferredSize(new Dimension(180, 460));
        panel.add(picJSP);

        picPanel.setPreferredSize(new Dimension(500, 460));
        picPanel.setBorder(BorderFactory.createEtchedBorder());
        panel.add(picPanel);

        add(panel);
        setTitle("我的单词卡");
        setSize(700, 500);
        setLocation(200, 225);
        setResizable(false);
    }
}

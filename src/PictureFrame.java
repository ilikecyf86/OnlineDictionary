/**
 * Created by Allen.C on 2016/12/12.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureFrame extends JFrame {
    private JPanel panel = new JPanel();
    private JList picList = new JList();
    private JScrollPane picJSP = new JScrollPane(picList);
    private JPanel picPanel = new JPanel();

    public PictureFrame (final DictionaryFrame dicFrame) {
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        picList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        /* 生成单词卡 */
        int width = 480;
        int height = 440;
        String str = "路霸爱你呦muamuamua";
        File file = new File("d:/image.jpg");

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = (Graphics2D)bi.getGraphics();
        graph.setBackground(Color.DARK_GRAY);
        graph.clearRect(0, 0, width, height);
        graph.setFont(new Font("微软雅黑", Font.BOLD, 50));
        graph.setPaint(Color.PINK);

        graph.drawString(str, 100, 100);

        try {
            ImageIO.write(bi, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        picList.addListSelectionListener(e -> {
            int selected = picList.getSelectedIndex();
        });
    }
}
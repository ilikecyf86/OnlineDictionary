/**
 * Created by Allen.C on 2016/12/12.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class PictureFrame extends JFrame {
    private JPanel panel = new JPanel();
    private JList picList = new JList();
    private JScrollPane picJSP = new JScrollPane(picList);
    private PicPanel picPanel = new PicPanel();

    private Vector cardData;

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
        Rectangle rect = dicFrame.getBounds();
        setLocation(rect.x - 50, rect.y + 125);
        setResizable(false);

        /* todo:生成单词卡 */
        try {
            cardData = dicFrame.client.getWordCard(dicFrame.loginFrame.toServer, dicFrame.loginFrame.fromServer, dicFrame.client.username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < cardData.size(); i++) {
            String newPicData[] = cardData.elementAt(i).toString().split("\\.");
            if (cardData.elementAt(i).toString().charAt(0) == '(')
                newPicData[0] = newPicData[0].substring(5);
            String meaning = "";
            switch (newPicData[1]) {
                case "Baidu":
                    meaning += OnlineSearch.searchBaidu(newPicData[0]);
                    break;
                case "Youdao":
                    meaning += OnlineSearch.searchYoudao(newPicData[0]);
                    break;
                case "Bing":
                    meaning += OnlineSearch.searchBing(newPicData[0]);
                    break;
            }

            /* todo:如果超过长度就换行 */
            int length = 0;
            for (int j = 0; j < meaning.length(); j++) {
                if (meaning.charAt(j) == '\n') {
                    length = 0;
                    continue;
                }
                length++;
                if (length > 30) {
                    length = 0;
                    meaning = meaning.substring(0, j) + '\n' + meaning.substring(j, meaning.length());
                }
            }

            String meanings[] = meaning.split("\\n");
            int width = 480;
            int height = 440;
            File file;
            if (cardData.elementAt(i).toString().charAt(0) == '(')
                file = new File(cardData.elementAt(i).toString().substring(5) + ".jpg");
            else
                file = new File(cardData.elementAt(i).toString() + ".jpg");

            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graph = (Graphics2D)bi.getGraphics();
            graph.setBackground(Color.DARK_GRAY);
            graph.clearRect(0, 0, width, height);
            graph.setFont(new Font("宋体", Font.BOLD, 15));
            graph.setPaint(Color.WHITE);

            for (int j = 0; j < meanings.length; j++)
                graph.drawString(meanings[j], 5, 20 * (j + 1));

            graph.setFont(new Font("微软雅黑", Font.ITALIC | Font.BOLD, 40));
            graph.setPaint(Color.PINK);
            graph.drawString("__________________单词卡___", 0, 420);

            try {
                ImageIO.write(bi, "jpg", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        picList.setListData(cardData);

        picList.addListSelectionListener(e -> {
            String picAdress;
            if (cardData.elementAt(picList.getSelectedIndex()).toString().charAt(0) == '(') {
                cardData.setElementAt(cardData.elementAt(picList.getSelectedIndex()).toString().substring(5), picList.getSelectedIndex());
                /* todo:向服务器返回单词卡已读 */
                String picData[] = cardData.elementAt(picList.getSelectedIndex()).toString().split("\\.");
                String word = picData[0];
                String sender = picData[2].substring(5);
                try {
                    dicFrame.client.readWordCard(dicFrame.loginFrame.toServer, dicFrame.client.username, word, sender);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            picAdress = cardData.elementAt(picList.getSelectedIndex()).toString();
            picAdress += ".jpg";
            picPanel.setImage(picAdress);
            picPanel.repaint(10, 10, 480, 440);
        });
    }
}

class PicPanel extends JPanel {
    private Image image;

    public PicPanel() {
        image = null;
    }

    public void setImage(String picAddress) {
        image = new ImageIcon(picAddress).getImage();
    }

    public void paintComponent(Graphics g) {
        g.drawImage(image, 10, 10, 480, 440, null);
    }
}
/**
 * Created by Allen.C on 2016/11/18.
 */
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class DictionaryFrame extends JFrame {
    public static void main(String[] args) {
        ;
    }

    private JPanel panel = new JPanel();
    private JLabel title = new JLabel("英汉在线词典", JLabel.CENTER);
    private JTextField input = new JTextField(25);
    private JButton searchButton = new JButton("search");
    private JCheckBox checkBaidu = new JCheckBox("百度");
    private JCheckBox checkYoudao = new JCheckBox("有道");
    private JCheckBox checkBing = new JCheckBox("必应");
    private JCheckBox checkAll = new JCheckBox("全选");
    private JTextArea result1 = new JTextArea(0, 30);
    private JTextArea result2 = new JTextArea(0, 30);
    private JTextArea result3 = new JTextArea(0, 30);
    private JScrollPane resultJSP1 = new JScrollPane(result1);
    private JScrollPane resultJSP2 = new JScrollPane(result2);
    private JScrollPane resultJSP3 = new JScrollPane(result3);
    private JButton like1 = new JButton("❤");
    private JButton like2 = new JButton("❤");
    private JButton like3 = new JButton("❤");

    public DictionaryFrame(DicClient client) {
        panel.setLayout(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEtchedBorder());

        JPanel panelHead = new JPanel();
        panelHead.setLayout(new GridLayout(3, 1));
        panelHead.setBorder(BorderFactory.createEtchedBorder());
        title.setText("欢迎使用英汉在线词典, " + client.username);
        panelHead.add(title);
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
        searchButton.setBackground(Color.CYAN);
        like1.setBackground(Color.PINK);
        like2.setBackground(Color.PINK);
        like3.setBackground(Color.PINK);
        panelInput.add(input);
        panelInput.add(searchButton);
        panelHead.add(panelInput);
        JPanel panelSelect = new JPanel();
        panelSelect.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
        panelSelect.add(checkBaidu);
        panelSelect.add(checkYoudao);
        panelSelect.add(checkBing);
        panelSelect.add(checkAll);
        panelHead.add(panelSelect);
        panel.add(panelHead);

        JPanel panelResult1 = new JPanel();
        panelResult1.setLayout(new BorderLayout());
        panelResult1.setBorder(new TitledBorder("-"));
        panelResult1.add(resultJSP1, BorderLayout.WEST);
        like1.setBackground(Color.PINK);
        panelResult1.add(like1, BorderLayout.EAST);
        panelResult1.setVisible(false);
        panel.add(panelResult1);

        JPanel panelResult2 = new JPanel();
        panelResult2.setLayout(new BorderLayout());
        panelResult2.setBorder(new TitledBorder("-"));
        panelResult2.add(resultJSP2, BorderLayout.WEST);
        like2.setBackground(Color.PINK);
        panelResult2.add(like2, BorderLayout.EAST);
        panelResult2.setVisible(false);
        panel.add(panelResult2);

        JPanel panelResult3 = new JPanel();
        panelResult3.setLayout(new BorderLayout());
        panelResult3.setBorder(new TitledBorder("-"));
        panelResult3.add(resultJSP3, BorderLayout.WEST);
        like3.setBackground(Color.PINK);
        panelResult3.add(like3, BorderLayout.EAST);
        panelResult3.setVisible(false);
        panel.add(panelResult3);

        add(panel);
        setTitle("英汉大词典");
        setSize(400,500);
        setLocation(200,200);
        setResizable(false);

        /* 选中全选则自动勾选其它三个选项，取消全选则全部清空 */
        checkAll.addActionListener(e -> {
            if (checkAll.isSelected()) {
                checkBaidu.setSelected(true);
                checkYoudao.setSelected(true);
                checkBing.setSelected(true);
            }
            else {
                checkBaidu.setSelected(false);
                checkYoudao.setSelected(false);
                checkBing.setSelected(false);
            }
        });

        /* 选中全部三个词典，则自动勾选全选 */
        checkBaidu.addActionListener(e ->  {
            if (checkBaidu.isSelected() && checkYoudao.isSelected() && checkBing.isSelected())
                checkAll.setSelected(true);
            else
                checkAll.setSelected(false);
        });
        checkYoudao.addActionListener(e ->  {
            if (checkBaidu.isSelected() && checkYoudao.isSelected() && checkBing.isSelected())
                checkAll.setSelected(true);
            else
                checkAll.setSelected(false);
        });
        checkBing.addActionListener(e ->  {
            if (checkBaidu.isSelected() && checkYoudao.isSelected() && checkBing.isSelected())
                checkAll.setSelected(true);
            else
                checkAll.setSelected(false);
        });

        searchButton.addActionListener(e -> {
            String word = input.getText();
            if (word.length() <= 0)
                JOptionPane.showMessageDialog(this, "请输入单词！", "ERROR", JOptionPane.WARNING_MESSAGE);
            else if (!(checkBaidu.isSelected() || checkYoudao.isSelected() || checkBing.isSelected()))
                JOptionPane.showMessageDialog(this, "请选择词典！", "ERROR", JOptionPane.WARNING_MESSAGE);
            else {
                String[] dicSortAsLike = { "有道", "必应", "百度", "空" };  //服务器返回按赞数排序的词典
                for (int i = 0; i < 3; i++)
                    if (!checkBaidu.isSelected() && dicSortAsLike[i] == "百度")
                        for (int j = i; j < 3; j++)
                            dicSortAsLike[j] = dicSortAsLike[j + 1];
                for (int i = 0; i < 3; i++)
                    if (!checkYoudao.isSelected() && dicSortAsLike[i] == "有道")
                        for (int j = i; j < 3; j++)
                            dicSortAsLike[j] = dicSortAsLike[j + 1];
                for (int i = 0; i < 3; i++)
                    if (!checkBing.isSelected() && dicSortAsLike[i] == "必应")
                        for (int j = i; j < 3; j++)
                            dicSortAsLike[j] = dicSortAsLike[j + 1];
                //for (int i = 0; i < 3; i++)   System.out.println(dicSortAsLike[i]);

                switch (dicSortAsLike[0]) {
                    case "百度":
                        panelResult1.setBorder(new TitledBorder("百度"));
                        result1.setText(OnlineSearch.searchBaidu(word));
                        result1.setCaretPosition(0);
                        panelResult1.setVisible(true);
                        break;
                    case "有道":
                        panelResult1.setBorder(new TitledBorder("有道"));
                        result1.setText(OnlineSearch.searchYoudao(word));
                        result1.setCaretPosition(0);
                        panelResult1.setVisible(true);
                        break;
                    case "必应":
                        panelResult1.setBorder(new TitledBorder("必应"));
                        result1.setText(OnlineSearch.searchBing(word));
                        result1.setCaretPosition(0);
                        panelResult1.setVisible(true);
                        break;
                    default:
                        panelResult1.setVisible(false);
                        break;
                }
                switch (dicSortAsLike[1]) {
                    case "百度":
                        panelResult2.setBorder(new TitledBorder("百度"));
                        result2.setText(OnlineSearch.searchBaidu(word));
                        result2.setCaretPosition(0);
                        panelResult2.setVisible(true);
                        break;
                    case "有道":
                        panelResult2.setBorder(new TitledBorder("有道"));
                        result2.setText(OnlineSearch.searchYoudao(word));
                        result2.setCaretPosition(0);
                        panelResult2.setVisible(true);
                        break;
                    case "必应":
                        panelResult2.setBorder(new TitledBorder("必应"));
                        result2.setText(OnlineSearch.searchBing(word));
                        result2.setCaretPosition(0);
                        panelResult2.setVisible(true);
                        break;
                    default:
                        panelResult2.setVisible(false);
                        break;
                }
                switch (dicSortAsLike[2]) {
                    case "百度":
                        panelResult3.setBorder(new TitledBorder("百度"));
                        result3.setText(OnlineSearch.searchBaidu(word));
                        result3.setCaretPosition(0);
                        panelResult3.setVisible(true);
                        break;
                    case "有道":
                        panelResult3.setBorder(new TitledBorder("有道"));
                        result3.setText(OnlineSearch.searchYoudao(word));
                        result3.setCaretPosition(0);
                        panelResult3.setVisible(true);
                        break;
                    case "必应":
                        panelResult3.setBorder(new TitledBorder("必应"));
                        result3.setText(OnlineSearch.searchBing(word));
                        result3.setCaretPosition(0);
                        panelResult3.setVisible(true);
                        break;
                    default:
                        panelResult3.setVisible(false);
                        break;
                }
            }
        });

        /* 关闭词典时退出登陆 */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    client.logout();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}

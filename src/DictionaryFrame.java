/**
 * Created by Allen.C on 2016/11/18.
 */
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;

public class DictionaryFrame extends JFrame {
    public static void main(String[] args) {
        DictionaryFrame df = new DictionaryFrame();
        df.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        df.setVisible(true);
    }

    private JPanel panel = new JPanel();
    private JLabel title = new JLabel("欢迎使用英汉在线词典", JLabel.CENTER);
    private JButton viewAll = new JButton("查看用户");
    private JLabel greeting = new JLabel("你好，请", JLabel.CENTER);
    private JButton loginButton = new JButton("登陆");
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

    private String word = null;

    private String[] dicSortAsLike = { "空", "空", "空", "空" };

    private LoginFrame loginFrame = null;
    private boolean ifLogin;

    public DictionaryFrame() {
        panel.setLayout(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEtchedBorder());

        JPanel panelHead = new JPanel();
        panelHead.setLayout(new GridLayout(4, 1));
        panelHead.setBorder(BorderFactory.createEtchedBorder());
        panelHead.add(title);

        JPanel panelLineTwo = new JPanel(new GridLayout(1, 2));
        JPanel panelView = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelView.add(viewAll);
        panelLineTwo.add(panelView);
        JPanel panelLogin = new JPanel();
        panelLogin.add(greeting);
        panelLogin.add(loginButton);
        panelLineTwo.add(panelLogin);
        panelHead.add(panelLineTwo);

        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        searchButton.setBackground(Color.CYAN);
        panelInput.add(input);
        panelInput.add(searchButton);
        panelHead.add(panelInput);

        JPanel panelSelect = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
        panelSelect.add(checkBaidu);
        panelSelect.add(checkYoudao);
        panelSelect.add(checkBing);
        panelSelect.add(checkAll);
        panelHead.add(panelSelect);

        panel.add(panelHead);

        JPanel panelResult1 = new JPanel(new BorderLayout());
        panelResult1.setBorder(new TitledBorder("结果栏1"));
        panelResult1.add(resultJSP1, BorderLayout.WEST);
        like1.setBackground(Color.WHITE);
        panelResult1.add(like1, BorderLayout.EAST);
        resultJSP1.setVisible(false);
        like1.setVisible(false);
        panel.add(panelResult1);

        JPanel panelResult2 = new JPanel(new BorderLayout());
        panelResult2.setBorder(new TitledBorder("结果栏2"));
        panelResult2.add(resultJSP2, BorderLayout.WEST);
        like2.setBackground(Color.WHITE);
        panelResult2.add(like2, BorderLayout.EAST);
        resultJSP2.setVisible(false);
        like2.setVisible(false);
        panel.add(panelResult2);

        JPanel panelResult3 = new JPanel(new BorderLayout());
        panelResult3.setBorder(new TitledBorder("结果栏3"));
        panelResult3.add(resultJSP3, BorderLayout.WEST);
        like3.setBackground(Color.WHITE);
        panelResult3.add(like3, BorderLayout.EAST);
        resultJSP3.setVisible(false);
        like3.setVisible(false);
        panel.add(panelResult3);

        ifLogin = true;

        add(panel);
        setTitle("英汉大词典");
        setSize(400,600);
        setLocation(200,200);
        setResizable(false);

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

        /* 搜索部分 */
        searchButton.addActionListener(e -> {
            word = input.getText();
            if (word.length() <= 0)
                JOptionPane.showMessageDialog(this, "请输入单词！", "WARNING", JOptionPane.WARNING_MESSAGE);
            else if (!judgeWord(word)) {
                JOptionPane.showMessageDialog(this, "输入错误！请重新输入！", "WARNING", JOptionPane.WARNING_MESSAGE);
                input.setText("");
            }
            else if (!(checkBaidu.isSelected() || checkYoudao.isSelected() || checkBing.isSelected()))
                JOptionPane.showMessageDialog(this, "请选择词典！", "WARNING", JOptionPane.WARNING_MESSAGE);
            else {
                like1.setBackground(Color.WHITE);
                like2.setBackground(Color.WHITE);
                like3.setBackground(Color.WHITE);
                /* 请求服务器返回按赞数排序的词典集合 */
                if (ifLogin) {
                    try {
                        int likeNum[] = loginFrame.client.getLikeNum(loginFrame.toServer, loginFrame.fromServer, word);
                        DicLikeNum dic[] = { new DicLikeNum("百度", likeNum[0]), new DicLikeNum("有道", likeNum[1]), new DicLikeNum("必应", likeNum[2]) };
                        for (int i = 0; i < 3; i++) {
                            int max = i;
                            for (int j = i + 1; j < 3; j++)
                                if (dic[j].likeNum > dic[max].likeNum)
                                    max = j;
                            if (max != i) {
                                DicLikeNum temp = dic[i];
                                dic[i] = dic[max];
                                dic[max] = temp;
                            }
                        }
                        dicSortAsLike[0] = dic[0].dicName;
                        dicSortAsLike[1] = dic[1].dicName;
                        dicSortAsLike[2] = dic[2].dicName;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else {
                    dicSortAsLike[0] = "百度";
                    dicSortAsLike[1] = "有道";
                    dicSortAsLike[2] = "必应";
                }
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
                for (int i = 0; i < 3; i++)
                    System.out.print(dicSortAsLike[i] + " ");
                System.out.println();

                switch (dicSortAsLike[0]) {
                    case "百度":
                        panelResult1.setBorder(new TitledBorder("百度"));
                        result1.setText(OnlineSearch.searchBaidu(word));
                        result1.setCaretPosition(0);
                        resultJSP1.setVisible(true);
                        like1.setVisible(true);
                        break;
                    case "有道":
                        panelResult1.setBorder(new TitledBorder("有道"));
                        result1.setText(OnlineSearch.searchYoudao(word));
                        result1.setCaretPosition(0);
                        resultJSP1.setVisible(true);
                        like1.setVisible(true);
                        break;
                    case "必应":
                        panelResult1.setBorder(new TitledBorder("必应"));
                        result1.setText(OnlineSearch.searchBing(word));
                        result1.setCaretPosition(0);
                        resultJSP1.setVisible(true);
                        like1.setVisible(true);
                        break;
                    default:
                        panelResult1.setBorder(new TitledBorder("结果栏1"));
                        resultJSP1.setVisible(false);
                        like1.setVisible(false);
                        break;
                }
                switch (dicSortAsLike[1]) {
                    case "百度":
                        panelResult2.setBorder(new TitledBorder("百度"));
                        result2.setText(OnlineSearch.searchBaidu(word));
                        result2.setCaretPosition(0);
                        resultJSP2.setVisible(true);
                        like2.setVisible(true);
                        break;
                    case "有道":
                        panelResult2.setBorder(new TitledBorder("有道"));
                        result2.setText(OnlineSearch.searchYoudao(word));
                        result2.setCaretPosition(0);
                        resultJSP2.setVisible(true);
                        like2.setVisible(true);
                        break;
                    case "必应":
                        panelResult2.setBorder(new TitledBorder("必应"));
                        result2.setText(OnlineSearch.searchBing(word));
                        result2.setCaretPosition(0);
                        resultJSP2.setVisible(true);
                        like2.setVisible(true);
                        break;
                    default:
                        panelResult2.setBorder(new TitledBorder("结果栏2"));
                        resultJSP2.setVisible(false);
                        like2.setVisible(false);
                        break;
                }
                switch (dicSortAsLike[2]) {
                    case "百度":
                        panelResult3.setBorder(new TitledBorder("百度"));
                        result3.setText(OnlineSearch.searchBaidu(word));
                        result3.setCaretPosition(0);
                        resultJSP3.setVisible(true);
                        like3.setVisible(true);
                        break;
                    case "有道":
                        panelResult3.setBorder(new TitledBorder("有道"));
                        result3.setText(OnlineSearch.searchYoudao(word));
                        result3.setCaretPosition(0);
                        resultJSP3.setVisible(true);
                        like3.setVisible(true);
                        break;
                    case "必应":
                        panelResult3.setBorder(new TitledBorder("必应"));
                        result3.setText(OnlineSearch.searchBing(word));
                        result3.setCaretPosition(0);
                        resultJSP3.setVisible(true);
                        like3.setVisible(true);
                        break;
                    default:
                        panelResult3.setBorder(new TitledBorder("结果栏3"));
                        resultJSP3.setVisible(false);
                        like3.setVisible(false);
                        break;
                }
            }
        });

        /* 登陆部分，如果未登录就登录，如果登录了就退出 */
        loginButton.addActionListener(e -> {
            try {
                loginFrame = new LoginFrame();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loginFrame.setVisible(true);
        });

        /* 点赞部分 */
        like1.addActionListener(e -> {
            if (!ifLogin)
                JOptionPane.showMessageDialog(this, "请先登陆。", "FAILED", JOptionPane.INFORMATION_MESSAGE);
            else {
                if (like1.getBackground() == Color.WHITE) {
                    like1.setBackground(Color.PINK);
                    switch (dicSortAsLike[0]) {
                        case "百度": try { loginFrame.client.likeBaidu(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        case "有道": try { loginFrame.client.likeYoudao(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        case "必应": try { loginFrame.client.likeBing(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        default: break;
                    }
                }
                else
                    System.out.println("再喜欢也只能点赞一次~");
            }
        });
        like2.addActionListener(e -> {
            if (!ifLogin)
                JOptionPane.showMessageDialog(this, "请先登陆。", "FAILED", JOptionPane.INFORMATION_MESSAGE);
            else {
                if (like2.getBackground() == Color.WHITE) {
                    like2.setBackground(Color.PINK);
                    switch (dicSortAsLike[1]) {
                        case "百度": try { loginFrame.client.likeBaidu(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        case "有道": try { loginFrame.client.likeYoudao(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        case "必应": try { loginFrame.client.likeBing(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        default: break;
                    }
                }
                else
                    System.out.println("再喜欢也只能点赞一次~");
            }
        });
        like3.addActionListener(e -> {
            if (!ifLogin)
                JOptionPane.showMessageDialog(this, "请先登陆。", "FAILED", JOptionPane.INFORMATION_MESSAGE);
            else {
                if (like3.getBackground() == Color.WHITE) {
                    like3.setBackground(Color.PINK);
                    switch (dicSortAsLike[2]) {
                        case "百度": try { loginFrame.client.likeBaidu(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        case "有道": try { loginFrame.client.likeYoudao(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        case "必应": try { loginFrame.client.likeBing(loginFrame.toServer, word); } catch (IOException e1) { e1.printStackTrace(); } break;
                        default: break;
                    }
                }
                else
                    System.out.println("再喜欢也只能点赞一次~");
            }
        });

        /* 查看用户 */
        viewAll.addActionListener(e -> {
            if (!ifLogin)
                JOptionPane.showMessageDialog(this, "请先登陆。", "FAILED", JOptionPane.INFORMATION_MESSAGE);
            else {
                ;
            }
        });

        /* 关闭程序时用户下线 */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (ifLogin) {
                    try {
                        loginFrame.client.logout(loginFrame.toServer);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean judgeWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
                continue;
            else if (word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                continue;
            else
                return false;
        }
        return true;
    }
}

class DicLikeNum {
    public String dicName;
    public int likeNum;
    public DicLikeNum(String name, int num) {
        dicName = name;
        likeNum = num;
    }
}

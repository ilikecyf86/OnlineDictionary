/**
 * Created by Allen.C on 2016/11/18.
 */
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Vector;

public class Dictionary {
    public static void main(String[] args) {
        DictionaryFrame df = new DictionaryFrame();
        df.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        df.setVisible(true);
    }
}

class DictionaryFrame extends JFrame {
    private JPanel panel = null;
    private JLabel title = new JLabel("汉英在线词典", JLabel.CENTER);
    private JTextField input = null;
    private JButton searchButton = new JButton("search");
    private JCheckBox checkBaidu = new JCheckBox("百度");
    private JCheckBox checkYoudao = new JCheckBox("有道");
    private JCheckBox checkBing = new JCheckBox("必应");
    private JCheckBox checkAll = new JCheckBox("全选");
    private JTextArea result1 = new JTextArea();
    private JTextArea result2 = new JTextArea();
    private JTextArea result3 = new JTextArea();
    private JScrollPane resultJSP1 = new JScrollPane(result1);
    private JScrollPane resultJSP2 = new JScrollPane(result2);
    private JScrollPane resultJSP3 = new JScrollPane(result3);

    public DictionaryFrame() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JPanel panelHead = new JPanel();
        panelHead.setLayout(new GridLayout(3, 1));
        panelHead.add(title);
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
        input = new JTextField(25);
        searchButton.setBackground(Color.CYAN);
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
        panelResult1.setLayout(new GridLayout(1, 2));
        panelResult1.setBorder(new TitledBorder("-"));
        panelResult1.add(resultJSP1);
        panel.add(panelResult1);

        JPanel panelResult2 = new JPanel();
        panelResult2.setLayout(new GridLayout(1, 2));
        panelResult2.setBorder(new TitledBorder("-"));
        panelResult2.add(resultJSP2);
        panel.add(panelResult2);

        JPanel panelResult3 = new JPanel();
        panelResult3.setLayout(new GridLayout(1, 2));
        panelResult3.setBorder(new TitledBorder("-"));
        panelResult3.add(resultJSP3);
        panel.add(panelResult3);

        add(panel);
        setSize(400,500);
        setLocation(200,200);
        setResizable(false);
        setTitle("E-C Dic OL v1.0");

        if (checkAll.isSelected()) {
            /* 目标：选中全选则自动勾选其它三个选项，取消则全部清空 */
        }
        if (checkBaidu.isSelected() && checkYoudao.isSelected() && checkBing.isSelected()) {
            /* 目标：选中全部三个词典，则自动勾选全选 */
        }

        searchButton.addActionListener(e -> {
            String word = input.getText();
            if (word.length() > 0) {
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
                        break;
                    case "有道":
                        panelResult1.setBorder(new TitledBorder("有道"));
                        result1.setText(OnlineSearch.searchYoudao(word));
                        break;
                    case "必应":
                        panelResult1.setBorder(new TitledBorder("必应"));
                        result1.setText(OnlineSearch.searchBing(word));
                        break;
                    default:
                        panelResult1.setBorder(new TitledBorder("-"));
                        result1.setText("");
                        break;
                }
                switch (dicSortAsLike[1]) {
                    case "百度":
                        panelResult2.setBorder(new TitledBorder("百度"));
                        result2.setText(OnlineSearch.searchBaidu(word));
                        break;
                    case "有道":
                        panelResult2.setBorder(new TitledBorder("有道"));
                        result2.setText(OnlineSearch.searchYoudao(word));
                        break;
                    case "必应":
                        panelResult2.setBorder(new TitledBorder("必应"));
                        result2.setText(OnlineSearch.searchBing(word));
                        break;
                    default:
                        panelResult2.setBorder(new TitledBorder("-"));
                        result2.setText("");
                        break;
                }
                switch (dicSortAsLike[2]) {
                    case "百度":
                        panelResult3.setBorder(new TitledBorder("百度"));
                        result3.setText(OnlineSearch.searchBaidu(word));
                        break;
                    case "有道":
                        panelResult3.setBorder(new TitledBorder("有道"));
                        result3.setText(OnlineSearch.searchYoudao(word));
                        break;
                    case "必应":
                        panelResult3.setBorder(new TitledBorder("必应"));
                        result3.setText(OnlineSearch.searchBing(word));
                        break;
                    default:
                        panelResult3.setBorder(new TitledBorder("-"));
                        result3.setText("");
                        break;
                }
            }
        });
    }
}

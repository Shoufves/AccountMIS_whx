package gui.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import gui.listener.IncomeListener;
import util.ColorUtil;
import util.GUIUtil;

public class IncomePanel extends WorkingPanel {
    static { GUIUtil.useLNF(); }
    public static IncomePanel instance = new IncomePanel();

    JLabel lAmount = new JLabel("\u91d1\u989d(\uffe5)");
    JLabel lSource = new JLabel("\u6765\u6e90");
    JLabel lComment = new JLabel("\u5907\u6ce8");
    JLabel lDate = new JLabel("\u65e5\u671f");

    public JTextField tfAmount = new JTextField("0");
    public JComboBox<String> cbSource = new JComboBox<>(
            new String[]{"\u5de5\u8d44", "\u517c\u804c", "\u7406\u8d22", "\u5176\u4ed6"});
    public JTextField tfComment = new JTextField();
    public JXDatePicker datepick = new JXDatePicker(new Date());

    JButton bSubmit = new JButton("\u8bb0\u4e00\u7b14\u6536\u5165");

    public IncomePanel() {
        GUIUtil.setColor(ColorUtil.grayColor, lAmount, lSource, lComment, lDate);
        GUIUtil.setColor(ColorUtil.blueColor, bSubmit);

        JPanel pInput = new JPanel();
        int gap = 40;
        pInput.setLayout(new GridLayout(4, 2, gap, gap));
        pInput.add(lAmount);
        pInput.add(tfAmount);
        pInput.add(lSource);
        pInput.add(cbSource);
        pInput.add(lComment);
        pInput.add(tfComment);
        pInput.add(lDate);
        pInput.add(datepick);

        JPanel pSubmit = new JPanel();
        pSubmit.add(bSubmit);

        this.setLayout(new BorderLayout());
        this.add(pInput, BorderLayout.NORTH);
        this.add(pSubmit, BorderLayout.CENTER);

        addListener();
    }

    @Override
    public void updateData() {
        tfAmount.setText("0");
        tfComment.setText("");
        cbSource.setSelectedIndex(0);
        datepick.setDate(new Date());
        tfAmount.grabFocus();
    }

    @Override
    public void addListener() {
        bSubmit.addActionListener(new IncomeListener());
    }
}

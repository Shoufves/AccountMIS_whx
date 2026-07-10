package gui.panel;
 
import java.awt.BorderLayout;
 
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
 
import gui.listener.ToolBarListener;
import util.CenterPanel;
import util.GUIUtil;
 
public class MainPanel extends JPanel {
    static {
        GUIUtil.useLNF();
    }
 
    public static MainPanel instance = new MainPanel();
    public JToolBar tb = new JToolBar();
    public JButton bSpend = new JButton();
    public JButton bRecord = new JButton();
    public JButton bCategory = new JButton();
    public JButton bReport = new JButton();
    public JButton bConfig = new JButton();
    public JButton bBackup = new JButton();
    public JButton bRecover = new JButton();
    public JButton bHistory = new JButton();
 
    public CenterPanel workingPanel;
 
    private MainPanel() {
 
        GUIUtil.setImageIcon(bSpend, "home.png", "\u6d88\u8d39\u4e00\u89c8");
        GUIUtil.setImageIcon(bRecord, "record.png", "\u8bb0\u4e00\u7b14");
        GUIUtil.setImageIcon(bCategory, "category2.png", "\u6d88\u8d39\u5206\u7c7b");
        GUIUtil.setImageIcon(bReport, "report.png", "\u6708\u6d88\u8d39\u62a5\u8868");
        GUIUtil.setImageIcon(bConfig, "config.png", "\u8bbe\u7f6e");
        GUIUtil.setImageIcon(bBackup, "backup.png", "\u5907\u4efd");
        GUIUtil.setImageIcon(bRecover, "restore.png", "\u6062\u590d");
        GUIUtil.setImageIcon(bHistory, "history.png", "\u5386\u53f2\u8bb0\u5f55");
 
        tb.add(bSpend);
        tb.add(bRecord);
        tb.add(bCategory);
        tb.add(bReport);
        tb.add(bConfig);
        tb.add(bBackup);
        tb.add(bRecover);
        tb.add(bHistory);
        tb.setFloatable(false);
 
        workingPanel = new CenterPanel(0.8);
 
        setLayout(new BorderLayout());
        add(tb, BorderLayout.NORTH);
        add(workingPanel, BorderLayout.CENTER);
         
        addListener();
    }
 
    private void addListener() {
        ToolBarListener listener = new ToolBarListener();
         
        bSpend.addActionListener(listener);
        bRecord.addActionListener(listener);
        bCategory.addActionListener(listener);
        bReport.addActionListener(listener);
        bConfig.addActionListener(listener);
        bBackup.addActionListener(listener);
        bRecover.addActionListener(listener);
        bHistory.addActionListener(listener);
         
    }
 
    public static void main(String[] args) {
        GUIUtil.showPanel(MainPanel.instance, 1);
    }
}

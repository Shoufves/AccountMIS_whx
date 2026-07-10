package gui.panel;
 
import java.awt.FlowLayout;
import javax.swing.JButton;
 
import gui.listener.BackupListener;
import util.ColorUtil;
import util.GUIUtil;
 
public class BackupPanel extends WorkingPanel {
    static {
        GUIUtil.useLNF();
    }
 
    public static BackupPanel instance = new BackupPanel();
    JButton bBackup = new JButton("备份");
    public JButton bExportCSV = new JButton("导出CSV");
 
    public BackupPanel() {
        GUIUtil.setColor(ColorUtil.blueColor, bBackup, bExportCSV);
        this.setLayout(new FlowLayout());
        this.add(bBackup);
        this.add(bExportCSV);
        addListener();
    }
 
    public static void main(String[] args) {
        GUIUtil.showPanel(BackupPanel.instance);
    }
 
    @Override
    public void updateData() {
 
    }
 
    @Override
    public void addListener() {
        BackupListener listener = new BackupListener();
        bBackup.addActionListener(listener);
        bExportCSV.addActionListener(listener);
    }
 
}
package gui.panel;
 
import static util.GUIUtil.showPanel;
 
import java.awt.BorderLayout;
import java.awt.Image;
import java.util.List;
 
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
import entity.CategorySpend;
import entity.Record;
import service.ReportService;
import util.ChartUtil;
import util.ColorUtil;
 
public class ReportPanel extends WorkingPanel {
    public static ReportPanel instance = new ReportPanel();
 
    JLabel lBar = new JLabel();
    JLabel lPie = new JLabel();
 
    public ReportPanel() {
        JPanel pPie = new JPanel(new BorderLayout());
        JLabel lTitle = new JLabel("\u672c\u6708\u5404\u5206\u7c7b\u6d88\u8d39\u5360\u6bd4", JLabel.CENTER);
        lTitle.setForeground(ColorUtil.blueColor);
        lTitle.setFont(new java.awt.Font("\u5fae\u8f6f\u96c5\u9ed1", java.awt.Font.BOLD, 14));
        pPie.add(lTitle, BorderLayout.NORTH);
        pPie.add(lPie, BorderLayout.CENTER);
 
        this.setLayout(new BorderLayout(8, 8));
        List<Record> rs = new ReportService().listThisMonthRecords();
        List<CategorySpend> cs = new ReportService().listCategorySpend();
        lBar.setIcon(new ImageIcon(ChartUtil.getImage(rs, 400, 120)));
        lPie.setIcon(new ImageIcon(ChartUtil.getPieImage(cs, 400, 130)));
        this.add(lBar, BorderLayout.NORTH);
        this.add(pPie, BorderLayout.SOUTH);
        addListener();
    }
 
    public static void main(String[] args) {
        showPanel(ReportPanel.instance);
    }
 
    @Override
    public void updateData() {
        List<Record> rs = new ReportService().listThisMonthRecords();
        List<CategorySpend> cs = new ReportService().listCategorySpend();
        lBar.setIcon(new ImageIcon(ChartUtil.getImage(rs, 400, 120)));
        lPie.setIcon(new ImageIcon(ChartUtil.getPieImage(cs, 400, 130)));
    }
 
    @Override
    public void addListener() {
 
    }
}

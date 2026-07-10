package gui.panel;
 
import static util.GUIUtil.showPanel;
 
import java.awt.BorderLayout;
import java.awt.Font;
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
        // 顶部标题
        JLabel lMainTitle = new JLabel("\u6708\u5ea6\u6536\u652f\u62a5\u8868", JLabel.CENTER);
        lMainTitle.setForeground(ColorUtil.blueColor);
        lMainTitle.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));
 
        // 饼图面板（分类消费占比）
        JPanel pPie = new JPanel(new BorderLayout());
        JLabel lPieTitle = new JLabel("\u672c\u6708\u5404\u5206\u7c7b\u6d88\u8d39\u5360\u6bd4", JLabel.CENTER);
        lPieTitle.setForeground(ColorUtil.blueColor);
        lPieTitle.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
        pPie.add(lPieTitle, BorderLayout.NORTH);
        pPie.add(lPie, BorderLayout.CENTER);
 
        // 柱状图面板（标题 + 收支双柱图）
        JPanel pBar = new JPanel(new BorderLayout());
        pBar.add(lMainTitle, BorderLayout.NORTH);
        pBar.add(lBar, BorderLayout.CENTER);
 
        this.setLayout(new BorderLayout(8, 4));
        List<Record> spendRecords = new ReportService().listThisMonthRecords();
        List<Record> incomeRecords = new ReportService().listThisMonthIncomeRecords();
        List<CategorySpend> cs = new ReportService().listCategorySpend();
        lBar.setIcon(new ImageIcon(ChartUtil.getIncomeExpenseImage(spendRecords, incomeRecords, 400, 100)));
        lPie.setIcon(new ImageIcon(ChartUtil.getPieImage(cs, 400, 120)));
        this.add(pBar, BorderLayout.NORTH);
        this.add(pPie, BorderLayout.SOUTH);
        addListener();
    }
 
    public static void main(String[] args) {
        showPanel(ReportPanel.instance);
    }
 
    @Override
    public void updateData() {
        List<Record> spendRecords = new ReportService().listThisMonthRecords();
        List<Record> incomeRecords = new ReportService().listThisMonthIncomeRecords();
        List<CategorySpend> cs = new ReportService().listCategorySpend();
        lBar.setIcon(new ImageIcon(ChartUtil.getIncomeExpenseImage(spendRecords, incomeRecords, 400, 100)));
        lPie.setIcon(new ImageIcon(ChartUtil.getPieImage(cs, 400, 120)));
    }
 
    @Override
    public void addListener() {
 
    }
}

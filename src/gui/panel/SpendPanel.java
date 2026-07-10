package gui.panel;
 
import static util.GUIUtil.setColor;
import static util.GUIUtil.showPanel;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
 
import javax.swing.JLabel;
import javax.swing.JPanel;
 
import gui.page.SpendPage;
import service.SpendService;
import util.CircleProgressBar;
import util.ColorUtil;
 
public class SpendPanel extends WorkingPanel {
    public static SpendPanel instance = new SpendPanel();
 
    JLabel lMonthSpend = new JLabel("\u672c\u6708\u6d88\u8d39");
    JLabel lTodaySpend = new JLabel("\u4eca\u65e5\u6d88\u8d39");
    JLabel lAvgSpendPerDay = new JLabel("\u65e5\u5747\u6d88\u8d39");
    JLabel lMonthLeft = new JLabel("\u672c\u6708\u5269\u4f59");
    JLabel lDayAvgAvailable = new JLabel("\u65e5\u5747\u53ef\u7528");
    JLabel lMonthLeftDay = new JLabel("\u8ddd\u79bb\u6708\u672b");
    JLabel lMonthIncome = new JLabel("\u672c\u6708\u6536\u5165");
 
    JLabel vMonthSpend = new JLabel("\uffe52300");
    JLabel vTodaySpend = new JLabel("\uffe525");
    JLabel vAvgSpendPerDay = new JLabel("\uffe5120");
    JLabel vMonthAvailable = new JLabel("\uffe52084");
    JLabel vDayAvgAvailable = new JLabel("\uffe5389");
    JLabel vMonthLeftDay = new JLabel("15\u5929");
    JLabel vMonthIncome = new JLabel("\uffe50");
 
    CircleProgressBar bar;
 
    public SpendPanel() {
        this.setLayout(new BorderLayout());
        bar = new CircleProgressBar();
        bar.setBackgroundColor(ColorUtil.blueColor);
 
        setColor(ColorUtil.grayColor, lMonthSpend, lTodaySpend, lAvgSpendPerDay, lMonthLeft, lDayAvgAvailable,
                lMonthLeftDay, vAvgSpendPerDay, vMonthAvailable, vDayAvgAvailable, vMonthLeftDay, lMonthIncome);
        setColor(ColorUtil.blueColor, vMonthSpend, vTodaySpend, vMonthIncome);
 
        vMonthSpend.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 20));
        vTodaySpend.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 20));
        vMonthIncome.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 20));
 
        this.add(center(), BorderLayout.CENTER);
        this.add(south(), BorderLayout.SOUTH);
 
    }
 
    private JPanel center() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(west(), BorderLayout.WEST);
        p.add(east());
        return p;
    }
 
    private Component east() {
        return bar;
    }
 
    private Component west() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(6, 1));
        p.add(lMonthSpend);
        p.add(vMonthSpend);
        p.add(lMonthIncome);
        p.add(vMonthIncome);
        p.add(lTodaySpend);
        p.add(vTodaySpend);
        return p;
    }
 
    private JPanel south() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 4));
 
        p.add(lAvgSpendPerDay);
        p.add(lMonthLeft);
        p.add(lDayAvgAvailable);
        p.add(lMonthLeftDay);
        p.add(vAvgSpendPerDay);
        p.add(vMonthAvailable);
        p.add(vDayAvgAvailable);
        p.add(vMonthLeftDay);
 
        return p;
    }
 
    public static void main(String[] args) {
        showPanel(SpendPanel.instance);
    }
 
    @Override
    public void updateData() {
        SpendPage spend = new SpendService().getSpendPage();
        vMonthIncome.setText(spend.monthIncome);
        vMonthIncome.setForeground(ColorUtil.blueColor);
        vMonthSpend.setText(spend.monthSpend);
        vTodaySpend.setText(spend.todaySpend);
        vAvgSpendPerDay.setText(spend.avgSpendPerDay);
        vMonthAvailable.setText(spend.monthAvailable);
        vDayAvgAvailable.setText(spend.dayAvgAvailable);
        vMonthLeftDay.setText(spend.monthLeftDay);
 
        bar.setProgress(spend.usagePercentage);
        if (spend.isOverSpend) {
            vMonthAvailable.setForeground(ColorUtil.warningColor);
            vMonthSpend.setForeground(ColorUtil.warningColor);
            vTodaySpend.setForeground(ColorUtil.warningColor);
 
        } else {
            vMonthAvailable.setForeground(ColorUtil.grayColor);
            vMonthSpend.setForeground(ColorUtil.blueColor);
            vTodaySpend.setForeground(ColorUtil.blueColor);
        }
        bar.setForegroundColor(ColorUtil.getByPercentage(spend.usagePercentage));
        addListener();
 
    }
 
    @Override
    public void addListener() {
        // TODO Auto-generated method stub
 
    }
}

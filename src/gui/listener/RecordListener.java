package gui.listener;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
 
import javax.swing.JOptionPane;
 
import dao.RecordDAO;
import entity.Category;
import entity.Record;
import gui.panel.CategoryPanel;
import gui.panel.MainPanel;
import gui.panel.RecordPanel;
import gui.panel.SpendPanel;
import service.ConfigService;
import service.IncomeService;
import service.RecordService;
import util.GUIUtil;
 
public class RecordListener implements ActionListener {
 
    @Override
    public void actionPerformed(ActionEvent e) {
        RecordPanel p = RecordPanel.instance;

        if (p.isIncomeMode()) {
            // -------- 收入模式 --------
            if (!GUIUtil.checkZero(p.tfSpend, "\u91d1\u989d"))
                return;
            int amount = Integer.parseInt(p.tfSpend.getText());
            String source = (String) p.cbSource.getSelectedItem();
            String comment = p.tfComment.getText();
            Date d = p.datepick.getDate();
            new IncomeService().add(amount, source, comment, d);
            JOptionPane.showMessageDialog(p, "\u6dfb\u52a0\u6210\u529f");
            MainPanel.instance.workingPanel.show(SpendPanel.instance);
            return;
        }

        // -------- 支出模式（原有逻辑）--------
        if (0 == p.cbModel.cs.size()) {
            JOptionPane.showMessageDialog(p, "\u6682\u65e0\u6d88\u8d39\u5206\u7c7b\uff0c\u65e0\u6cd5\u6dfb\u52a0\uff0c\u8bf7\u5148\u589e\u52a0\u6d88\u8d39\u5206\u7c7b");
            MainPanel.instance.workingPanel.show(CategoryPanel.instance);
            return;
        }
         
        if (!GUIUtil.checkZero(p.tfSpend, "\u82b1\u8d39\u91d1\u989d"))
            return;
        int spend = Integer.parseInt(p.tfSpend.getText());
        Category c = p.getSelectedCategory();
        String comment = p.tfComment.getText();
        Date d = p.datepick.getDate();
        new RecordService().add(spend, c, comment, d);

        // 检查是否超预算
        int budget = new ConfigService().getIntBudget();
        if (budget > 0) {
            int monthSpend = 0;
            for (Record r : new RecordDAO().listThisMonth())
                monthSpend += r.getSpend();
            if (monthSpend > budget) {
                int exceeded = monthSpend - budget;
                JOptionPane.showMessageDialog(p, "警告：本月消费已超出预算 " + exceeded + " 元！",
                        "\u9884\u7b97\u8d85\u652f\u63d0\u9192", JOptionPane.WARNING_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(p, "\u6dfb\u52a0\u6210\u529f");
        MainPanel.instance.workingPanel.show(SpendPanel.instance);
    }
 
}
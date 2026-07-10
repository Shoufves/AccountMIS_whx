package gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import dao.IncomeDAO;
import dao.RecordDAO;
import entity.Income;
import entity.Record;
import gui.model.HistoryTableModel;
import gui.panel.HistoryPanel;

public class HistoryListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        HistoryPanel p = HistoryPanel.instance;
        Object src = e.getSource();

        if (src == p.bSearch) {
            p.page = 1;
            p.loadData();
        } else if (src == p.bPrev) {
            p.page--;
            p.loadData();
        } else if (src == p.bNext) {
            p.page++;
            p.loadData();
        } else if (src == p.bEdit) {
            editRow(p);
        } else if (src == p.bDelete) {
            deleteRow(p);
        }
    }

    private void editRow(HistoryPanel p) {
        HistoryTableModel.Row r = p.getSelectedRow();
        if (r == null) {
            JOptionPane.showMessageDialog(p, "\u8bf7\u5148\u9009\u62e9\u4e00\u6761\u8bb0\u5f55");
            return;
        }

        if (r.isIncome) {
            editIncome(p, r);
        } else {
            editExpense(p, r);
        }
    }

    private void editIncome(HistoryPanel p, HistoryTableModel.Row r) {
        String amtStr = JOptionPane.showInputDialog(p,
                "\u4fee\u6539\u91d1\u989d\uff08\u5f53\u524d\uff1a" + r.amount + "\uff09:", r.amount);
        if (amtStr == null || amtStr.trim().isEmpty()) return;
        int amount;
        try {
            amount = Integer.parseInt(amtStr.trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(p, "\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(p, "\u8bf7\u8f93\u5165\u6709\u6548\u6574\u6570");
            return;
        }

        String oldComment = (r.comment == null) ? "" : r.comment;
        String comment = JOptionPane.showInputDialog(p, "\u4fee\u6539\u5907\u6ce8:", oldComment);
        if (comment == null) return;

        Income inc = new Income();
        inc.id = r.incomeId;
        inc.amount = amount;
        inc.source = r.catName;
        inc.comment = comment;
        inc.date = new java.sql.Date(r.date.getTime());
        new IncomeDAO().update(inc);
        JOptionPane.showMessageDialog(p, "\u4fee\u6539\u6210\u529f");
        p.loadData();
    }

    private void editExpense(HistoryPanel p, HistoryTableModel.Row r) {
        String spendStr = JOptionPane.showInputDialog(p,
                "\u4fee\u6539\u91d1\u989d\uff08\u5f53\u524d\uff1a" + r.amount + "\uff09:", r.amount);
        if (spendStr == null || spendStr.trim().isEmpty()) return;
        int spend;
        try {
            spend = Integer.parseInt(spendStr.trim());
            if (spend <= 0) {
                JOptionPane.showMessageDialog(p, "\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(p, "\u8bf7\u8f93\u5165\u6709\u6548\u6574\u6570");
            return;
        }
        String oldComment = (r.comment == null) ? "" : r.comment;
        String comment = JOptionPane.showInputDialog(p, "\u4fee\u6539\u5907\u6ce8:", oldComment);
        if (comment == null) return;

        Record rec = new Record();
        rec.id = r.expenseId;
        rec.spend = spend;
        rec.comment = comment;
        new RecordDAO().update(rec);
        JOptionPane.showMessageDialog(p, "\u4fee\u6539\u6210\u529f");
        p.loadData();
    }

    private void deleteRow(HistoryPanel p) {
        HistoryTableModel.Row r = p.getSelectedRow();
        if (r == null) {
            JOptionPane.showMessageDialog(p, "\u8bf7\u5148\u9009\u62e9\u4e00\u6761\u8bb0\u5f55");
            return;
        }
        String itemDesc = r.isIncome
                ? "\u6536\u5165\uff08\u6765\u6e90:" + r.catName + "\uff09"
                : "\u652f\u51fa\uff08\u5206\u7c7b:" + r.catName + "\uff09";
        int confirm = JOptionPane.showConfirmDialog(p,
                "\u786e\u8ba4\u5220\u9664\u8be5\u8bb0\u5f55\uff1f\uff08" + itemDesc + " \uffe5" + r.amount + "\uff09",
                "\u786e\u8ba4\u5220\u9664", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        if (r.isIncome) {
            new IncomeDAO().delete(r.incomeId);
        } else {
            new RecordDAO().delete(r.expenseId);
        }
        JOptionPane.showMessageDialog(p, "\u5220\u9664\u6210\u529f");
        if (p.htm.getRowCount() == 1 && p.page > 1)
            p.page--;
        p.loadData();
    }
}
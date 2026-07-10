package gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import dao.RecordDAO;
import entity.Record;
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
            editRecord(p);
        } else if (src == p.bDelete) {
            deleteRecord(p);
        }
    }

    private void editRecord(HistoryPanel p) {
        Record r = p.getSelectedRecord();
        if (r == null) {
            JOptionPane.showMessageDialog(p, "\u8bf7\u5148\u9009\u62e9\u4e00\u6761\u8bb0\u5f55");
            return;
        }
        String spendStr = JOptionPane.showInputDialog(p,
                "\u4fee\u6539\u91d1\u989d\uff08\u5f53\u524d\uff1a" + r.spend + "\uff09:", r.spend);
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

        r.spend = spend;
        r.comment = comment;
        new RecordDAO().update(r);
        JOptionPane.showMessageDialog(p, "\u4fee\u6539\u6210\u529f");
        p.loadData();
    }

    private void deleteRecord(HistoryPanel p) {
        Record r = p.getSelectedRecord();
        if (r == null) {
            JOptionPane.showMessageDialog(p, "\u8bf7\u5148\u9009\u62e9\u4e00\u6761\u8bb0\u5f55");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(p,
                "\u786e\u8ba4\u5220\u9664\u8be5\u8bb0\u5f55\uff1f\uff08\u91d1\u989d:\uffe5" + r.spend + "\uff09",
                "\u786e\u8ba4\u5220\u9664", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        new RecordDAO().delete(r.id);
        JOptionPane.showMessageDialog(p, "\u5220\u9664\u6210\u529f");
        if (p.htm.getRowCount() == 1 && p.page > 1)
            p.page--;
        p.loadData();
    }
}

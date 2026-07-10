package gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JOptionPane;

import gui.panel.IncomePanel;
import gui.panel.MainPanel;
import gui.panel.SpendPanel;
import service.IncomeService;
import util.GUIUtil;

public class IncomeListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        IncomePanel p = IncomePanel.instance;

        if (!GUIUtil.checkZero(p.tfAmount, "\u91d1\u989d"))
            return;
        int amount = Integer.parseInt(p.tfAmount.getText());
        String source = (String) p.cbSource.getSelectedItem();
        String comment = p.tfComment.getText();
        Date d = p.datepick.getDate();

        new IncomeService().add(amount, source, comment, d);
        JOptionPane.showMessageDialog(p, "\u6dfb\u52a0\u6210\u529f");
        MainPanel.instance.workingPanel.show(SpendPanel.instance);
    }
}

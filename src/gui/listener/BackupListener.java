package gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import gui.model.HistoryTableModel;
import gui.panel.BackupPanel;
import gui.panel.ConfigPanel;
import gui.panel.HistoryPanel;
import gui.panel.MainPanel;
import service.ConfigService;
import util.MysqlUtil;

public class BackupListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        BackupPanel p = BackupPanel.instance;

        if (e.getSource() == p.bExportCSV) {
            exportCSV();
            return;
        }

        String mysqlPath = new ConfigService().get(ConfigService.mysqlPath);
        if (0 == mysqlPath.length()) {
            JOptionPane.showMessageDialog(p, "备份前请事先配置mysql的路径");
            MainPanel.instance.workingPanel.show(ConfigPanel.instance);
            ConfigPanel.instance.tfMysqlPath.grabFocus();
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("acountMIS.sql"));
        fc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return ".sql";
            }

            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".sql");
            }
        });

        int returnVal = fc.showSaveDialog(p);
        File file = fc.getSelectedFile();
        System.out.println(file);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //如果保存的文件名没有以.sql结尾，自动加上.sql
            System.out.println(file);
            if (!file.getName().toLowerCase().endsWith(".sql"))
                file = new File(file.getParent(), file.getName() + ".sql");
            System.out.println(file);

            try {
                MysqlUtil.backup(mysqlPath, file.getAbsolutePath());
                JOptionPane.showMessageDialog(p, "备份成功,备份文件位于:\r\n" + file.getAbsolutePath());
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(p, "备份失败\r\n,错误:\r\n" + e1.getMessage());
            }

        }
    }

    private void exportCSV() {
        HistoryPanel hp = HistoryPanel.instance;
        // 确保日期选择器已初始化（首次访问时可能为 null，loadData 依赖非空日期）
        if (hp.dpStart.getDate() == null) {
            hp.dpStart.setDate(util.DateUtil.monthBegin());
        }
        if (hp.dpEnd.getDate() == null) {
            hp.dpEnd.setDate(util.DateUtil.monthEnd());
        }
        // 确保数据已加载（首次访问时 htm.rows 可能为空）
        if (hp.htm.rows.isEmpty()) {
            hp.loadData();
        }

        java.util.List<HistoryTableModel.Row> rows = hp.htm.rows;
        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(BackupPanel.instance,
                    "没有可导出的数据，请先在历史记录面板中筛选数据");
            return;
        }

        // 使用 AWT 原生 FileDialog，避免 Liquid LNF 的 JFileChooser
        // 与 Java 模块系统冲突（sun.awt.shell.ShellFolder 不可访问）
        java.awt.FileDialog fd = new java.awt.FileDialog(
                (java.awt.Frame) null, "导出 CSV", java.awt.FileDialog.SAVE);
        fd.setFile("账务导出.csv");
        fd.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".csv"));
        fd.setVisible(true);

        String dir = fd.getDirectory();
        String file = fd.getFile();
        if (dir == null || file == null) return;

        String path = dir + file;
        if (!path.toLowerCase().endsWith(".csv"))
            path += ".csv";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try (FileOutputStream fos = new FileOutputStream(path);
             OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
             BufferedWriter bw = new BufferedWriter(osw)) {

            // BOM for Excel UTF-8 recognition
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            bw.write("编号,金额,类型,分类/来源,备注,日期");
            bw.newLine();
            for (HistoryTableModel.Row r : rows) {
                String comment = (r.comment == null || r.comment.isEmpty()) ? "" : r.comment;
                bw.write(r.displayId + "," + r.amount + "," + r.type + ","
                        + r.catName + "," + comment + ","
                        + (r.date != null ? sdf.format(r.date) : ""));
                bw.newLine();
            }
            JOptionPane.showMessageDialog(BackupPanel.instance,
                    "已导出 " + rows.size() + " 条记录到\n" + path);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(BackupPanel.instance,
                    "导出失败: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

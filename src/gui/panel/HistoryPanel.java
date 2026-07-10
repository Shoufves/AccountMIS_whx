package gui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jdesktop.swingx.JXDatePicker;
import dao.IncomeDAO;
import entity.Category;
import entity.Income;
import entity.Record;
import gui.listener.HistoryListener;
import gui.model.HistoryTableModel;
import service.CategoryService;
import service.RecordService;
import util.ColorUtil;
import util.DateUtil;
import util.GUIUtil;

public class HistoryPanel extends WorkingPanel {
    static { GUIUtil.useLNF(); }
    public static HistoryPanel instance = new HistoryPanel();

    // 筛选组件
    public JComboBox<Category> cbCategory = new JComboBox<>();
    public JXDatePicker dpStart = new JXDatePicker();
    public JXDatePicker dpEnd = new JXDatePicker();
    public JButton bSearch = new JButton("\u67e5\u8be2");

    // 表格
    public HistoryTableModel htm = new HistoryTableModel();
    public JTable t = new JTable(htm);

    // 分页
    public JButton bPrev = new JButton("\u4e0a\u4e00\u9875");
    public JButton bNext = new JButton("\u4e0b\u4e00\u9875");
    public JLabel lPage = new JLabel("\u7b2c1\u9875/\u51711\u9875");

    // 操作
    public JButton bEdit = new JButton("\u7f16\u8f91");
    public JButton bDelete = new JButton("\u5220\u9664");

    // 分页状态
    public int page = 1;
    public int total = 0;

    public HistoryPanel() {
        GUIUtil.setColor(ColorUtil.blueColor, bSearch, bPrev, bNext, bEdit, bDelete);

        JPanel pFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pFilter.add(new JLabel("\u5206\u7c7b:"));
        pFilter.add(cbCategory);
        pFilter.add(new JLabel("\u4ece:"));
        pFilter.add(dpStart);
        pFilter.add(new JLabel("\u5230:"));
        pFilter.add(dpEnd);
        pFilter.add(bSearch);

        JPanel pPage = new JPanel();
        pPage.add(bPrev);
        pPage.add(lPage);
        pPage.add(bNext);

        JPanel pAction = new JPanel();
        pAction.add(bEdit);
        pAction.add(bDelete);

        JPanel pSouth = new JPanel(new BorderLayout());
        pSouth.add(pPage, BorderLayout.NORTH);
        pSouth.add(pAction, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(pFilter, BorderLayout.NORTH);
        this.add(new JScrollPane(t), BorderLayout.CENTER);
        this.add(pSouth, BorderLayout.SOUTH);

        addListener();
    }

    public Category getSelectedCategory() {
        return (Category) cbCategory.getSelectedItem();
    }

    public HistoryTableModel.Row getSelectedRow() {
        int row = t.getSelectedRow();
        if (row < 0) return null;
        return htm.getRowAt(row);
    }

    @Override
    public void updateData() {
        Category selected = getSelectedCategory();
        int oldId = (selected != null) ? selected.id : -1;
        cbCategory.removeAllItems();
        Category all = new Category();
        all.id = -1;
        all.name = "\uff08\u5168\u90e8\uff09";
        cbCategory.addItem(all);
        for (Category c : new CategoryService().list())
            cbCategory.addItem(c);
        for (int i = 0; i < cbCategory.getItemCount(); i++) {
            if (cbCategory.getItemAt(i).id == oldId) {
                cbCategory.setSelectedIndex(i);
                break;
            }
        }

        dpStart.setDate(DateUtil.monthBegin());
        dpEnd.setDate(DateUtil.monthEnd());

        page = 1;
        loadData();
    }

    public void loadData() {
        Category sel = getSelectedCategory();
        Integer cid = (sel != null && sel.id != -1) ? sel.id : null;
        Date start = dpStart.getDate();
        Date end = dpEnd.getDate();

        // 查询支出记录（分页）
        RecordService rs = new RecordService();
        total = rs.getTotal(cid, start, end);
        int maxPage = Math.max(1, (total + RecordService.PAGE_SIZE - 1) / RecordService.PAGE_SIZE);
        if (page > maxPage) page = maxPage;
        if (page < 1) page = 1;

        List<Record> expenseList = rs.list(cid, start, end, page);

        // 预加载分类名称映射
        java.util.Map<Integer, String> catMap = new java.util.HashMap<>();
        for (Category c : new CategoryService().list())
            catMap.put(c.id, c.name);

        // 转换为统一Row
        List<HistoryTableModel.Row> allRows = new ArrayList<>();
        for (Record rec : expenseList) {
            HistoryTableModel.Row row = new HistoryTableModel.Row();
            row.displayId = rec.id;
            row.amount = rec.spend;
            row.type = "\u652f\u51fa";
            row.catName = catMap.getOrDefault(rec.cid, "\u672a\u77e5");
            row.comment = rec.comment;
            row.date = rec.date;
            row.isIncome = false;
            row.expenseId = rec.id;
            allRows.add(row);
        }

        // 查询收入记录（日期范围内全部，不分页）
        IncomeDAO incomeDao = new IncomeDAO();
        List<Income> incomeList = incomeDao.list(start, end);
        for (Income inc : incomeList) {
            HistoryTableModel.Row row = new HistoryTableModel.Row();
            row.displayId = inc.id;
            row.amount = inc.amount;
            row.type = "\u6536\u5165";
            row.catName = inc.source;
            row.comment = inc.comment;
            row.date = inc.date;
            row.isIncome = true;
            row.incomeId = inc.id;
            allRows.add(row);
        }
        total += incomeList.size();

        // 按日期降序排列
        allRows.sort(Comparator.comparing((HistoryTableModel.Row r) -> r.date).reversed());

        htm.setData(allRows);
        lPage.setText("\u7b2c" + page + "\u9875/\u5171" + maxPage + "\u9875");
        bPrev.setEnabled(page > 1);
        bNext.setEnabled(page < maxPage);

        boolean hasData = !allRows.isEmpty();
        bEdit.setEnabled(hasData);
        bDelete.setEnabled(hasData);
        if (hasData)
            t.getSelectionModel().setSelectionInterval(0, 0);
    }

    @Override
    public void addListener() {
        HistoryListener l = new HistoryListener();
        bSearch.addActionListener(l);
        bPrev.addActionListener(l);
        bNext.addActionListener(l);
        bEdit.addActionListener(l);
        bDelete.addActionListener(l);
    }
}
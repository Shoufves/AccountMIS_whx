package gui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import entity.Category;
import service.CategoryService;

public class HistoryTableModel extends AbstractTableModel {
    String[] columnNames = new String[]{"\u7f16\u53f7", "\u91d1\u989d", "\u7c7b\u578b", "\u5206\u7c7b/\u6765\u6e90", "\u5907\u6ce8", "\u65e5\u671f"};
    public List<Row> rows = new ArrayList<>();
    private Map<Integer, String> categoryMap = new HashMap<>();

    public static class Row {
        public int displayId;
        public int amount;
        public String type;        // "支出" 或 "收入"
        public String catName;     // 分类名称（支出）或来源（收入）
        public String comment;
        public Date date;
        public boolean isIncome;
        public int expenseId;      // record表id（支出时有效）
        public int incomeId;       // income表id（收入时有效）
    }

    public void setData(List<Row> rows) {
        this.rows = rows;
        categoryMap.clear();
        List<Category> categories = new CategoryService().list();
        for (Category c : categories)
            categoryMap.put(c.id, c.name);
        fireTableDataChanged();
    }

    public Row getRowAt(int rowIndex) {
        return rows.get(rowIndex);
    }

    @Override
    public int getRowCount() { return rows.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }

    @Override
    public String getColumnName(int col) { return columnNames[col]; }

    @Override
    public boolean isCellEditable(int row, int col) { return false; }

    @Override
    public Object getValueAt(int rowIndex, int col) {
        Row r = rows.get(rowIndex);
        switch (col) {
            case 0: return r.displayId;
            case 1: return "\uffe5" + r.amount;
            case 2: return r.type;
            case 3: return (r.catName != null) ? r.catName : "\u2014";
            case 4: return (r.comment == null || r.comment.isEmpty()) ? "\u2014" : r.comment;
            case 5: return r.date;
            default: return null;
        }
    }
}
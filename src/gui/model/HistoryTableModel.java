package gui.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import entity.Category;
import entity.Record;
import service.CategoryService;

public class HistoryTableModel extends AbstractTableModel {
    String[] columnNames = new String[]{"编号", "金额", "分类", "备注", "日期"};
    public List<Record> records = new java.util.ArrayList<Record>();
    private Map<Integer, String> categoryMap = new HashMap<>();

    public void setData(List<Record> records) {
        this.records = records;
        categoryMap.clear();
        List<Category> categories = new CategoryService().list();
        for (Category c : categories)
            categoryMap.put(c.id, c.name);
        fireTableDataChanged();
    }

    public Record getRecordAt(int row) {
        return records.get(row);
    }

    @Override
    public int getRowCount() { return records.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }

    @Override
    public String getColumnName(int col) { return columnNames[col]; }

    @Override
    public boolean isCellEditable(int row, int col) { return false; }

    @Override
    public Object getValueAt(int row, int col) {
        Record r = records.get(row);
        switch (col) {
            case 0: return r.id;
            case 1: return "\uffe5" + r.spend;
            case 2: return categoryMap.getOrDefault(r.cid, "\u672a\u77e5");
            case 3: return (r.comment == null || r.comment.isEmpty()) ? "\u2014" : r.comment;
            case 4: return r.date;
            default: return null;
        }
    }
}

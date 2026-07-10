package service;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import dao.CategoryDAO;
import dao.IncomeDAO;
import dao.RecordDAO;
import entity.Category;
import entity.CategorySpend;
import entity.Income;
import entity.Record;
import util.DateUtil;
 
public class ReportService {
 
    /**
     * 获取某一天的消费金额
     * @param d
     * @param monthRawData
     * @return
     */
    public int getDaySpend(Date d,List<Record> monthRawData){
        int daySpend = 0;
        for (Record record : monthRawData) {
            if(record.date.equals(d))
                daySpend+=record.spend;
        }
        return daySpend;
    }
         
    /**
     * 获取一个月的消费记录集合
     * @return
     */
    public List<Record> listThisMonthRecords() {
        RecordDAO dao= new RecordDAO();
        List<Record> monthRawData= dao.listThisMonth();
        List<Record> result= new ArrayList<>();
        Date monthBegin = DateUtil.monthBegin();
        int monthTotalDay = DateUtil.thisMonthTotalDay();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < monthTotalDay; i++) {
            Record r = new Record();
            c.setTime(monthBegin);
            c.add(Calendar.DATE, i);
            Date eachDayOfThisMonth=c.getTime() ;
            int daySpend = getDaySpend(eachDayOfThisMonth,monthRawData);
            r.spend=daySpend;
            result.add(r);
        }
        return result;
 
    }
 
    /**
     * 获取本月各分类消费统计（用于饼图）
     * @return 分类消费列表（按金额降序）
     */
    public List<CategorySpend> listCategorySpend() {
        List<CategorySpend> result = new ArrayList<>();
        RecordDAO recordDao = new RecordDAO();
        CategoryDAO categoryDao = new CategoryDAO();
        List<Record> monthRecords = recordDao.listThisMonth();

        if (monthRecords.isEmpty())
            return result;

        Map<Integer, Integer> spendMap = new HashMap<>();
        int total = 0;
        for (Record r : monthRecords) {
            spendMap.merge(r.cid, r.spend, Integer::sum);
            total += r.spend;
        }

        Color[] colors = {
            Color.decode("#FF6B6B"), Color.decode("#4ECDC4"),
            Color.decode("#45B7D1"), Color.decode("#96CEB4"),
            Color.decode("#FFEAA7"), Color.decode("#DDA0DD"),
            Color.decode("#98D8C8"), Color.decode("#F7DC6F")
        };
        int ci = 0;
        for (Map.Entry<Integer, Integer> e : spendMap.entrySet()) {
            Category cat = categoryDao.get(e.getKey());
            String name = (cat != null) ? cat.name : "\u672a\u77e5";
            result.add(new CategorySpend(name, e.getValue(),
                    e.getValue() * 100.0 / total, colors[ci % colors.length]));
            ci++;
        }
        return result;
    }

    /**
     * 获取本月每日收入数据（用于月度收支报表）
     * @return 每日收入对应的Record列表（复用spend字段存储收入金额）
     */
    public List<Record> listThisMonthIncomeRecords() {
        IncomeDAO dao = new IncomeDAO();
        List<Income> monthRawData = dao.listThisMonth();
        List<Record> result = new ArrayList<>();
        Date monthBegin = DateUtil.monthBegin();
        int monthTotalDay = DateUtil.thisMonthTotalDay();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < monthTotalDay; i++) {
            Record r = new Record();
            c.setTime(monthBegin);
            c.add(Calendar.DATE, i);
            Date eachDay = c.getTime();
            int dayIncome = 0;
            for (Income inc : monthRawData) {
                if (inc.date != null && inc.date.equals(eachDay))
                    dayIncome += inc.amount;
            }
            r.spend = dayIncome;
            result.add(r);
        }
        return result;
    }
}
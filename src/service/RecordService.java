package service;
 
import java.util.Date;
 
import dao.RecordDAO;
import entity.Category;
import entity.Record;

public class RecordService {
    RecordDAO recordDao = new RecordDAO();

    public static final int PAGE_SIZE = 20;

    public int getTotal(Integer cid, java.util.Date startDate, java.util.Date endDate) {
        return recordDao.getTotal(cid, startDate, endDate);
    }

    public java.util.List<Record> list(Integer cid, java.util.Date startDate, java.util.Date endDate, int page) {
        return recordDao.list(cid, startDate, endDate, (page - 1) * PAGE_SIZE, PAGE_SIZE);
    }

    public void add(int spend, Category c, String comment,Date date){
        Record r = new Record();
        r.spend = spend;
        r.cid = c.id;
        r.comment = comment;
        r.date = date;
        recordDao.add(r);
    }
}
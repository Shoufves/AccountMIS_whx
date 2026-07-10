package dao;
 
import java.sql.Connection;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import entity.Record;
import util.DBUtil;
import util.DateUtil;
  
public class RecordDAO {
  
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select count(*) from record";
  
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
  
            System.out.println("total:" + total);
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return total;
    }
  
    public void add(Record record) {
  
        String sql = "insert into record values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, record.spend);
            ps.setInt(2, record.cid);
            ps.setString(3, record.comment);
            ps.setDate(4, DateUtil.util2sql(record.date));
  
            ps.execute();
  
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                record.id = id;
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public void update(Record record) {
  
        String sql = "update record set spend= ?, cid= ?, comment =?, date = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
  
              ps.setInt(1, record.spend);
              ps.setInt(2, record.cid);
              ps.setString(3, record.comment);
              ps.setDate(4, DateUtil.util2sql(record.date));
              ps.setInt(5, record.id);
  
            ps.execute();
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
  
    }
  
    public void delete(int id) {
  
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "delete from record where id = " + id;
  
            s.execute(sql);
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public Record get(int id) {
        Record record = null;
  
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select * from record where id = " + id;
  
            ResultSet rs = s.executeQuery(sql);
  
            if (rs.next()) {
                record = new Record();
                int spend = rs.getInt("spend");
                int cid = rs.getInt("cid");
                String comment = rs.getString("comment");
                Date date = rs.getDate("date");
                 
                record.spend=spend;
                record.cid=cid;
                record.comment=comment;
                record.date=date;
                record.id = id;
            }
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return record;
    }
  
    public List<Record> list() {
        return list(0, Short.MAX_VALUE);
    }
     
    public List<Record> list(int start, int count) {
        List<Record> records = new ArrayList<Record>();
  
        String sql = "select * from record order by id desc limit ?,? ";
  
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setInt(1, start);
            ps.setInt(2, count);
  
            ResultSet rs = ps.executeQuery();
  
            while (rs.next()) {
                Record record = new Record();
                int id = rs.getInt("id");
                int spend = rs.getInt("spend");
                int cid = rs.getInt("cid");
                 
                 String comment = rs.getString("comment");
                 Date date = rs.getDate("date");
                  
                 record.spend=spend;
                 record.cid=cid;
                 record.comment=comment;
                 record.date=date;
                 record.id = id;
                records.add(record);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return records;
    }
     
    public List<Record> list(int cid) {
        List<Record> records = new ArrayList<Record>();
  
        String sql = "select * from record where cid = ?";
  
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
  
            ps.setInt(1, cid);
  
            ResultSet rs = ps.executeQuery();
  
            while (rs.next()) {
                Record record = new Record();
                int id = rs.getInt("id");
                int spend = rs.getInt("spend");
                 
                 String comment = rs.getString("comment");
                 Date date = rs.getDate("date");
                  
                 record.spend=spend;
                 record.cid=cid;
                 record.comment=comment;
                 record.date=date;
                 record.id = id;
                records.add(record);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return records;
    }    
     
    public List<Record> listToday(){
        return list(DateUtil.today());
    }
     
    public List<Record> list(Date day) {
        List<Record> records = new ArrayList<Record>();
        String sql = "select * from record where date =?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setDate(1, DateUtil.util2sql(day));
 
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Record record = new Record();
                int id = rs.getInt("id");
                int cid = rs.getInt("cid");
                int spend = rs.getInt("spend");
                 
                 String comment = rs.getString("comment");
                 Date date = rs.getDate("date");
                  
                 record.spend=spend;
                 record.cid=cid;
                 record.comment=comment;
                 record.date=date;
                 record.id = id;
                 records.add(record);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return records;
    }            
     
    public List<Record> listThisMonth(){
        return list(DateUtil.monthBegin(),DateUtil.monthEnd());
    }
     
    public List<Record> list(Date start, Date end) {
        List<Record> records = new ArrayList<Record>();
        String sql = "select * from record where date >=? and date <= ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setDate(1, DateUtil.util2sql(start));
            ps.setDate(2, DateUtil.util2sql(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Record record = new Record();
                int id = rs.getInt("id");
                int cid = rs.getInt("cid");
                int spend = rs.getInt("spend");
                 
                 String comment = rs.getString("comment");
                 Date date = rs.getDate("date");
                  
                 record.spend=spend;
                 record.cid=cid;
                 record.comment=comment;
                 record.date=date;
                 record.id = id;
                 records.add(record);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return records;
    }

    /**
     * 获取筛选条件下的记录总数
     * @param cid      分类ID(null=不筛选)
     * @param startDate 起始日期(null=不筛选)
     * @param endDate   截止日期(null=不筛选)
     */
    public int getTotal(Integer cid, Date startDate, Date endDate) {
        int total = 0;
        StringBuilder sql = new StringBuilder("select count(*) from record where 1=1");
        java.util.List<Object> params = new java.util.ArrayList<Object>();

        if (cid != null) {
            sql.append(" and cid = ?");
            params.add(cid);
        }
        if (startDate != null) {
            sql.append(" and date >= ?");
            params.add(DateUtil.util2sql(startDate));
        }
        if (endDate != null) {
            sql.append(" and date <= ?");
            params.add(DateUtil.util2sql(endDate));
        }

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++)
                ps.setObject(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                total = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 多条件筛选 + 分页查询
     * @param cid      分类ID(null=不筛选)
     * @param startDate 起始日期(null=不筛选)
     * @param endDate   截止日期(null=不筛选)
     * @param start    分页起始
     * @param count    每页条数
     */
    public List<Record> list(Integer cid, Date startDate, Date endDate, int start, int count) {
        List<Record> records = new ArrayList<Record>();
        StringBuilder sql = new StringBuilder("select * from record where 1=1");
        java.util.List<Object> params = new java.util.ArrayList<Object>();

        if (cid != null) {
            sql.append(" and cid = ?");
            params.add(cid);
        }
        if (startDate != null) {
            sql.append(" and date >= ?");
            params.add(DateUtil.util2sql(startDate));
        }
        if (endDate != null) {
            sql.append(" and date <= ?");
            params.add(DateUtil.util2sql(endDate));
        }

        sql.append(" order by id desc limit ?,?");

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object p : params)
                ps.setObject(idx++, p);
            ps.setInt(idx++, start);
            ps.setInt(idx, count);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Record r = new Record();
                r.id = rs.getInt("id");
                r.spend = rs.getInt("spend");
                r.cid = rs.getInt("cid");
                r.comment = rs.getString("comment");
                r.date = rs.getDate("date");
                records.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

}
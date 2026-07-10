package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.Income;
import util.DBUtil;
import util.DateUtil;

public class IncomeDAO {

    public void add(Income income) {
        String sql = "insert into income values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, income.amount);
            ps.setString(2, income.source);
            ps.setString(3, income.comment);
            ps.setDate(4, DateUtil.util2sql(income.date));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                income.id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Income> listThisMonth() {
        return list(DateUtil.monthBegin(), DateUtil.monthEnd());
    }

    public List<Income> listToday() {
        Date today = DateUtil.today();
        return list(today, today);
    }

    public List<Income> list(Date start, Date end) {
        List<Income> incomes = new ArrayList<Income>();
        String sql = "select * from income where date >=? and date <= ? order by id desc";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, DateUtil.util2sql(start));
            ps.setDate(2, DateUtil.util2sql(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Income income = new Income();
                income.id = rs.getInt("id");
                income.amount = rs.getInt("amount");
                income.source = rs.getString("source");
                income.comment = rs.getString("comment");
                income.date = rs.getDate("date");
                incomes.add(income);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }

    public int getMonthTotal() {
        int total = 0;
        List<Income> list = listThisMonth();
        for (Income i : list)
            total += i.amount;
        return total;
    }

    public void update(Income income) {
        String sql = "update income set amount=?, source=?, comment=?, date=? where id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, income.amount);
            ps.setString(2, income.source);
            ps.setString(3, income.comment);
            ps.setDate(4, DateUtil.util2sql(income.date));
            ps.setInt(5, income.id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete from income where id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

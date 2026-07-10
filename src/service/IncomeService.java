package service;

import java.util.Date;

import dao.IncomeDAO;
import entity.Income;

public class IncomeService {
    IncomeDAO incomeDao = new IncomeDAO();

    public void add(int amount, String source, String comment, Date date) {
        Income i = new Income();
        i.amount = amount;
        i.source = source;
        i.comment = comment;
        i.date = date;
        incomeDao.add(i);
    }

    public int getMonthIncome() {
        return incomeDao.getMonthTotal();
    }
}

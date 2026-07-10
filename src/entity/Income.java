package entity;

import java.util.Date;

public class Income {
    public int id;
    public int amount;
    public String source;
    public String comment;
    public Date date;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}

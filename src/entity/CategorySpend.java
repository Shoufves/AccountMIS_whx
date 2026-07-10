package entity;

import java.awt.Color;

/** 分类消费统计DTO，用于饼图展示 */
public class CategorySpend {
    public String name;
    public int spend;
    public double percent;
    public Color color;

    public CategorySpend(String name, int spend, double percent, Color color) {
        this.name = name;
        this.spend = spend;
        this.percent = percent;
        this.color = color;
    }
}

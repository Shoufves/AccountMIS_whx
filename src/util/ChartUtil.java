package util;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
 
import javax.swing.*;
 
import com.objectplanet.chart.BarChart;
import com.objectplanet.chart.Chart;
 
import entity.Record;
import entity.CategorySpend;
 
public class ChartUtil {
    private static String[] sampleLabels(List<Record> rs) {
        String[] sampleLabels = new String[rs.size()];
        for (int i = 0; i < sampleLabels.length; i++) {
            if (0 == i % 5)
                sampleLabels[i] = String.valueOf(i + 1 + "日");
        }
 
        return sampleLabels;
 
    }
 
    public static double[] sampleValues(List<Record> rs) {
        double[] sampleValues = new double[rs.size()];
        for (int i = 0; i < sampleValues.length; i++) {
            sampleValues[i] = rs.get(i).spend;
        }
 
        return sampleValues;
    }
 
    public static Image getImage(List<Record> rs, int width, int height) {
        // 根据消费记录得到的样本数据
        double[] sampleValues = sampleValues(rs);
        // 根据消费记录得到的下方日期文本
        String[] sampleLabels = sampleLabels(rs);
        // 样本中的最大值
        int max = max(sampleValues);
 
        // 数据颜色
        Color[] sampleColors = new Color[] { ColorUtil.blueColor };
 
        // 柱状图
        BarChart chart = new BarChart();
 
        // 设置样本个数
        chart.setSampleCount(sampleValues.length);
        // 设置样本数据
        chart.setSampleValues(0, sampleValues);
        // 设置文字
        chart.setSampleLabels(sampleLabels);
        // 设置样本颜色
        chart.setSampleColors(sampleColors);
        // 设置取值范围
        chart.setRange(0, max * 1.2);
        // 显示背景横线
        chart.setValueLinesOn(true);
        // 显示文字
        chart.setSampleLabelsOn(true);
        // 把文字显示在下方
        chart.setSampleLabelStyle(Chart.BELOW);
 
        // 样本值的字体
        chart.setFont("rangeLabelFont", new Font("Arial", Font.BOLD, 12));
        // 显示图例说明
        chart.setLegendOn(true);
        // 把图例说明放在左侧
        chart.setLegendPosition(Chart.LEFT);
        // 图例说明中的文字
        chart.setLegendLabels(new String[] { "月消费报表" });
        // 图例说明的字体
        chart.setFont("legendFont", new Font("Dialog", Font.BOLD, 13));
        // 下方文字的字体
        chart.setFont("sampleLabelFont", new Font("Dialog", Font.BOLD, 13));
        // 图表中间背景颜色
        chart.setChartBackground(Color.white);
        // 图表整体背景颜色
        chart.setBackground(ColorUtil.backgroundColor);
        // 把图表转换为Image类型
        Image im = chart.getImage(width, height);
        return im;
    }
 
    public static int max(double[] sampleValues) {
        int max = 0;
        for (double v : sampleValues) {
            if (v > max)
                max = (int) v;
        }
        return max;
 
    }
 
    private static String[] sampleLabels() {
        String[] sampleLabels = new String[30];
 
        for (int i = 0; i < sampleLabels.length; i++) {
            if (0 == i % 5)
                sampleLabels[i] = String.valueOf(i + 1 + "日");
        }
        return sampleLabels;
    }
 
    public static Image getImage(int width, int height) {
        // 模拟样本数据
        double[] sampleValues = sampleValues();
        // 下方显示的文字
        String[] sampleLabels = sampleLabels();
        // 样本中的最大值
        int max = max(sampleValues);
 
        // 数据颜色
        Color[] sampleColors = new Color[] { ColorUtil.blueColor };
 
        // 柱状图
        BarChart chart = new BarChart();
 
        // 设置样本个数
        chart.setSampleCount(sampleValues.length);
        // 设置样本数据
        chart.setSampleValues(0, sampleValues);
        // 设置文字
        chart.setSampleLabels(sampleLabels);
        // 设置样本颜色
        chart.setSampleColors(sampleColors);
        // 设置取值范围
        chart.setRange(0, max * 1.2);
        // 显示背景横线
        chart.setValueLinesOn(true);
        // 显示文字
        chart.setSampleLabelsOn(true);
        // 把文字显示在下方
        chart.setSampleLabelStyle(Chart.BELOW);
 
        // 样本值的字体
        chart.setFont("rangeLabelFont", new Font("Arial", Font.BOLD, 12));
        // 显示图例说明
        chart.setLegendOn(true);
        // 把图例说明放在左侧
        chart.setLegendPosition(Chart.LEFT);
        // 图例说明中的文字
        chart.setLegendLabels(new String[] { "月消费报表" });
        // 图例说明的字体
        chart.setFont("legendFont", new Font("Dialog", Font.BOLD, 13));
        // 下方文字的字体
        chart.setFont("sampleLabelFont", new Font("Dialog", Font.BOLD, 13));
        // 图表中间背景颜色
        chart.setChartBackground(Color.white);
        // 图表整体背景颜色
        chart.setBackground(ColorUtil.backgroundColor);
        // 把图表转换为Image类型
        Image im = chart.getImage(width, height);
        return im;
    }
 
    private static double[] sampleValues() {
 
        double[] result = new double[30];
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) (Math.random() * 300);
        }
        return result;
 
    }
 
    public static void main(String[] args) {
        JPanel p = new JPanel();
        JLabel l = new JLabel();
        Image img = ChartUtil.getImage(400, 300);
        Icon icon = new ImageIcon(img);
        l.setIcon(icon);
        p.add(l);
        GUIUtil.showPanel(p);
    }

    /**
     * 生成分类消费占比饼图
     * @param data  分类消费数据列表
     * @param width 图片宽度
     * @param height 图片高度
     * @return 饼图Image
     */
    public static Image getPieImage(List<CategorySpend> data, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(ColorUtil.backgroundColor);
        g.fillRect(0, 0, width, height);

        if (data == null || data.isEmpty()) {
            g.setColor(ColorUtil.grayColor);
            g.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 16));
            String msg = "\u6682\u65e0\u6570\u636e";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(msg, (width - fm.stringWidth(msg)) / 2, height / 2);
            g.dispose();
            return img;
        }

        int pieX = 20, pieY = 20;
        int pieD = Math.min(height - 40, 180);
        int legendX = pieX + pieD + 30;

        // draw pie
        int startAngle = 90;
        for (CategorySpend cs : data) {
            int arcAngle = (int) Math.round(cs.percent / 100.0 * 360);
            if (arcAngle <= 0) continue;
            g.setColor(cs.color);
            g.fillArc(pieX, pieY, pieD, pieD, startAngle, -arcAngle);
            startAngle -= arcAngle;
        }
        // white center circle
        g.setColor(Color.WHITE);
        int innerD = pieD * 2 / 5;
        g.fillOval(pieX + (pieD - innerD) / 2, pieY + (pieD - innerD) / 2, innerD, innerD);

        // legend
        g.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 11));
        FontMetrics fm = g.getFontMetrics();
        int legendStart = Math.max(10, (pieY + pieD - data.size() * 16) / 2);
        for (int i = 0; i < data.size(); i++) {
            CategorySpend cs = data.get(i);
            int y = legendStart + i * 16;
            g.setColor(cs.color);
            g.fillRect(legendX, y, 12, 12);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(legendX, y, 12, 12);
            String label = cs.name + " " + String.format("%.1f%%", cs.percent);
            g.setColor(Color.BLACK);
            g.drawString(label, legendX + 16, y + 10);
        }

        g.dispose();
        return img;
    }
 
}
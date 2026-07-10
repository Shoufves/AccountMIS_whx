package gui.panel;
 
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.Date;
 
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
 
import org.jdesktop.swingx.JXDatePicker;
 
import entity.Category;
import gui.listener.RecordListener;
import gui.model.CategoryComboBoxModel;
import service.CategoryService;
import util.ColorUtil;
import util.GUIUtil;
 
public class RecordPanel extends WorkingPanel {
    static{
        GUIUtil.useLNF();
    }
    public static RecordPanel instance = new RecordPanel();
 
    JLabel lSpend = new JLabel("花费(￥)");
    JLabel lComment = new JLabel("备注");
    JLabel lDate = new JLabel("日期");
 
    public JTextField tfSpend = new JTextField("0");
 
    public CategoryComboBoxModel cbModel = new CategoryComboBoxModel();
    public JComboBox<Category> cbCategory = new JComboBox<>(cbModel);
    public JComboBox<String> cbSource = new JComboBox<>(
            new String[]{"\u5de5\u8d44", "\u517c\u804c", "\u7406\u8d22", "\u5176\u4ed6"});
    public JTextField tfComment = new JTextField();
    public JXDatePicker datepick = new JXDatePicker(new Date());
     
    JButton bSubmit = new JButton("记一笔");
 
    JRadioButton rbExpense = new JRadioButton("\u652f\u51fa", true);
    JRadioButton rbIncome  = new JRadioButton("\u6536\u5165");
    CardLayout cardLayout = new CardLayout();
    JPanel pCatSrcCard = new JPanel(cardLayout);
 
    public RecordPanel() {
        GUIUtil.setColor(ColorUtil.grayColor, lSpend,lComment,lDate);
        GUIUtil.setColor(ColorUtil.blueColor, bSubmit);
 
        // 支出/收入 单选按钮
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbExpense);
        bg.add(rbIncome);
        JPanel pType = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 2));
        pType.add(rbExpense);
        pType.add(rbIncome);
 
        // CardLayout: 支出→分类下拉框，收入→来源下拉框
        JPanel pCatCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pCatCard.add(cbCategory);
        JPanel pSrcCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pSrcCard.add(cbSource);
        pCatSrcCard.add(pCatCard, "expense");
        pCatSrcCard.add(pSrcCard, "income");
 
        rbExpense.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                cardLayout.show(pCatSrcCard, "expense");
        });
        rbIncome.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                cardLayout.show(pCatSrcCard, "income");
        });
 
        JPanel pInput =new JPanel();
        JPanel pSubmit = new JPanel();
        int gap = 40;
        pInput.setLayout(new GridLayout(4,2,gap,gap));
         
        pInput.add(lSpend);
        pInput.add(tfSpend);
        pInput.add(pType);
        pInput.add(pCatSrcCard);
        pInput.add(lComment);
        pInput.add(tfComment);
        pInput.add(lDate);
        pInput.add(datepick);
         
        pSubmit.add(bSubmit);
         
        this.setLayout(new BorderLayout());
        this.add(pInput,BorderLayout.NORTH);
        this.add(pSubmit,BorderLayout.CENTER);
         
        addListener();
    }
 
    public boolean isIncomeMode() {
        return rbIncome.isSelected();
    }
 
    public static void main(String[] args) {
        GUIUtil.showPanel(RecordPanel.instance);
    }
 
    public Category getSelectedCategory(){
        return (Category) cbCategory.getSelectedItem();
    }
 
    @Override
    public void updateData() {
        cbModel.cs = new CategoryService().list();
        cbCategory.updateUI();
        resetInput();
        tfSpend.grabFocus();
    }
     
    public void resetInput(){
        tfSpend.setText("0");
        tfComment.setText("");
        if(0!=cbModel.cs.size())
            cbCategory.setSelectedIndex(0);
        cbSource.setSelectedIndex(0);
        datepick.setDate(new Date());
    }  
 
    @Override
    public void addListener() {
        RecordListener listener = new RecordListener();
        bSubmit.addActionListener(listener);
    }
 
}
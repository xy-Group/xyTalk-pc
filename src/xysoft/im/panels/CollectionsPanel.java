package xysoft.im.panels;

import xysoft.im.components.Colors;

import javax.swing.*;
import java.awt.*;


public class CollectionsPanel extends ParentAvailablePanel
{
    /**
	 * 第三个Tab，工作台
	 */
	private static final long serialVersionUID = -5113591788842017322L;
	private JLabel tipLabel;

    public CollectionsPanel(JPanel parent)
    {
        super(parent);

        initComponents();
        initView();
    }

    private void initComponents()
    {
        tipLabel = new JLabel("企业工作台");
        tipLabel.setForeground(Colors.FONT_GRAY);
    }

    private void initView()
    {
        this.setBackground(Colors.DARK);
        setLayout(new FlowLayout());
        add(tipLabel);
    }
}

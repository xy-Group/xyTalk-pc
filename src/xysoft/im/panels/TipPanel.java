package xysoft.im.panels;


import xysoft.im.components.GBC;
import xysoft.im.utils.IconUtil;

import javax.swing.*;
import java.awt.*;


public class TipPanel extends ParentAvailablePanel
{
    /**
	 * 默认首次进入的界面
	 */
	private static final long serialVersionUID = -8943855187877504201L;
	private JLabel imageLabel;

    public TipPanel(JPanel parent)
    {
        super(parent);
        initComponents();
        initView();
    }

    private void initComponents()
    {
        imageLabel = new JLabel();
        imageLabel.setIcon(IconUtil.getIcon(this, "/image/bg.png",64,64));
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(imageLabel, new GBC(0,0).setAnchor(GBC.CENTER).setInsets(0,0,50,0));
    }

}

package xysoft.im.panels;

import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.ImageTitlePanel;
import xysoft.im.utils.IconUtil;

import javax.swing.*;
import java.awt.*;


public class CollectionsPanel extends ParentAvailablePanel
{
    /**
	 * 第三个Tab，企业工作台
	 */
	private static final long serialVersionUID = -5113591788842017322L;
	private JLabel tipLabel;
	private ImageTitlePanel appOA;
	private ImageTitlePanel appTSQ;
	private ImageTitlePanel appFile;
	private ImageTitlePanel appOrg;
	private ImageTitlePanel appNotice;
	private ImageTitlePanel appHRM;

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
        
        appOA = new ImageTitlePanel();
        appOA.setTitle("待办事宜");
        appOA.setIcon(IconUtil.getIcon(this, "/image/app.png",32,32));
        
        appTSQ = new ImageTitlePanel();
        appTSQ.setTitle("同事圈");
        appTSQ.setIcon(IconUtil.getIcon(this, "/image/audio.png",32,32));
        
        appFile = new ImageTitlePanel();
        appFile.setTitle("文档库");
        appFile.setIcon(IconUtil.getIcon(this, "/image/compress.png",32,32));
        
        appOrg = new ImageTitlePanel();
        appOrg.setTitle("组织架构");
        appOrg.setIcon(IconUtil.getIcon(this, "/image/image.png",32,32));
        appOrg.setBackground(Colors.ITEM_SELECTED);
        
        appNotice = new ImageTitlePanel();
        appNotice.setTitle("企业通告");
        appNotice.setIcon(IconUtil.getIcon(this, "/image/ms_word.png",32,32));
        appNotice.setBackground(Colors.ITEM_SELECTED);
        
        appHRM = new ImageTitlePanel();
        appHRM.setTitle("人力系统");
        appHRM.setIcon(IconUtil.getIcon(this, "/image/ms_powerpoint.png",32,32));
        appHRM.setBackground(Colors.ITEM_SELECTED);
        
        
    }

    private void initView()
    {
        this.setBackground(Colors.DARK);
        //setLayout(new GridBagLayout());
        //setLayout(new GridLayout(0,20));
        setLayout(new FlowLayout());

        add(appOA);
        add(appTSQ);
        add(appFile);
        add(appOrg);
        add(appHRM);
        add(appNotice);

//        //add(tipLabel, new GBC(0,0).setAnchor(GBC.CENTER).setInsets(0,0,20,0));
//        add(appOA, new GBC(0,1).setAnchor(GBC.EAST));//.setInsets(0,0,20,0));
//        add(appTSQ, new GBC(0,2).setAnchor(GBC.NORTHWEST).setFill(GBC.BOTH).setInsets(0,0,20,50));
//        add(appFile, new GBC(0,3).setAnchor(GBC.NORTHWEST).setFill(GBC.BOTH).setInsets(0,0,20,50));
    }
}

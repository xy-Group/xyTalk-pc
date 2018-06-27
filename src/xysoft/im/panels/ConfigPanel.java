package xysoft.im.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.RCButton;
import xysoft.im.utils.IconUtil;

public class ConfigPanel extends JPanel
{

	/**
	 * 系统设置
	 */
	private static final long serialVersionUID = -6440556966568490047L;
	private JLabel infoLabel;
    private RCButton button;

    public ConfigPanel()
    {
        initComponents();
        initView();
        setListeners();
    }

    private void setListeners()
    {
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {}
        });
    }

    
    private void initComponents()
    {
        infoLabel = new JLabel("待开发...");
        button = new RCButton("设置", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        button.setPreferredSize(new Dimension(150, 35));
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        button.setToolTipText("");
    }


   

    private void initView()
    {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 150));
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(button, BorderLayout.CENTER);

        this.setLayout(new GridBagLayout());
        add(panel, new GBC(0, 0).setAnchor(GBC.NORTH).setFill(GBC.HORIZONTAL).setInsets(-200, 0, 0, 0));
    }


}

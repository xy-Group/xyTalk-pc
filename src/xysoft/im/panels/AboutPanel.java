package xysoft.im.panels;

import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.utils.FontUtil;
import xysoft.im.utils.IconUtil;

import javax.swing.*;
import java.awt.*;


public class AboutPanel extends JPanel
{
    /**
	 * 关于
	 */
	private static final long serialVersionUID = 2113429402109287181L;
	private JLabel imageLabel;
    private JLabel versionLabel;
    private JLabel javaLabel;

    public AboutPanel()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        imageLabel = new JLabel();
        ImageIcon icon  = IconUtil.getIcon(this, "/image/ic_launcher.png", 100,100);
        imageLabel.setIcon(icon);

        versionLabel = new JLabel();
        versionLabel.setText("XyTalk v" + Launcher.APP_VERSION);
        versionLabel.setFont(FontUtil.getDefaultFont(20));
        versionLabel.setForeground(Colors.FONT_GRAY_DARKER);
        
        javaLabel = new JLabel();
        javaLabel.setText("JAVA版本：" + System.getProperty("java.version"));
        javaLabel.setFont(FontUtil.getDefaultFont(20));
        javaLabel.setForeground(Colors.FONT_GRAY_DARKER);
        
        
    }

    private void initView()
    {
        this.setLayout(new GridBagLayout());

        JPanel avatarNamePanel = new JPanel();
//        avatarNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
//        avatarNamePanel.add(imageLabel, BorderLayout.NORTH);
//        avatarNamePanel.add(versionLabel, BorderLayout.CENTER);
//        avatarNamePanel.add(javaLabel, BorderLayout.SOUTH);
        GridLayout grid = new GridLayout(6,1);
        grid.setHgap(20);
        avatarNamePanel.setLayout(grid);
        avatarNamePanel.add(imageLabel);//, new GBC(0, 1).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(20, 10, 0, 0));
        avatarNamePanel.add(versionLabel);// new GBC(0, 2).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(20, 10, 0, 0));
        avatarNamePanel.add(javaLabel); //new GBC(0, 3).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(20, 10, 0, 0));

        add(avatarNamePanel, new GBC(0,0).setWeight(1,1).setAnchor(GBC.CENTER).setFill(GBC.BOTH).setInsets(50,150,0,0));
    }
}

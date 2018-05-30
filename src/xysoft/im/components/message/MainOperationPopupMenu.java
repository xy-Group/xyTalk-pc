package xysoft.im.components.message;

import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.RCMainOperationMenuItemUI;
import xysoft.im.frames.CreateGroupDialog;
import xysoft.im.frames.MainFrame;
import xysoft.im.frames.SystemConfigDialog;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MainOperationPopupMenu extends JPopupMenu
{
    public MainOperationPopupMenu()
    {
        initMenuItem();
    }

    private void initMenuItem()
    {
        JMenuItem itemNewMUC = new JMenuItem("创建群组");
        JMenuItem itemConfig = new JMenuItem("设置");
        JMenuItem itemExit = new JMenuItem("退出");

        itemNewMUC.setUI(new RCMainOperationMenuItemUI());
        itemNewMUC.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showCreateGroupDialog();
            }
        });
        ImageIcon icon1 = new ImageIcon(getClass().getResource("/image/chat.png"));
        icon1.setImage(icon1.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        itemNewMUC.setIcon(icon1);
        itemNewMUC.setIconTextGap(5);


        itemConfig.setUI(new RCMainOperationMenuItemUI());
        itemConfig.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //System.out.println("系统设置");
                SystemConfigDialog dialog = new SystemConfigDialog(MainFrame.getContext(), true);
                dialog.setVisible(true);
            }
        });
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/image/setting.png"));
        icon2.setImage(icon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        itemConfig.setIcon(icon2);
        itemConfig.setIconTextGap(5);

        itemExit.setUI(new RCMainOperationMenuItemUI());
        itemExit.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	Launcher.connection.disconnect();
                System.exit(1);
            }
        });
        ImageIcon icon3 = new ImageIcon(getClass().getResource("/image/logout.png"));
        icon3.setImage(icon3.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        itemExit.setIcon(icon3);
        itemExit.setIconTextGap(5);


        this.add(itemNewMUC);
        this.add(itemConfig);
        this.add(itemExit);

        setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
        setBackground(Colors.FONT_WHITE);
    }

    /**
     * 弹出创建群聊窗口
     */
    private void showCreateGroupDialog()
    {
        CreateGroupDialog dialog = new CreateGroupDialog(null, true);
        dialog.setVisible(true);

        /*ShadowBorderDialog shadowBorderDialog = new ShadowBorderDialog(MainFrame.getContext(), true, dialog);
        shadowBorderDialog.setVisible(true);*/
    }
}

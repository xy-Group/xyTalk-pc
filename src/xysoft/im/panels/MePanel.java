package xysoft.im.panels;

import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.RCButton;
import xysoft.im.components.VerticalFlowLayout;
import xysoft.im.db.model.CurrentUser;
import xysoft.im.db.service.CurrentUserService;
import xysoft.im.frames.MainFrame;
import xysoft.im.utils.AvatarUtil;
import xysoft.im.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by song on 26/06/2017.
 */
public class MePanel extends JPanel
{
    private JPanel contentPanel;
    private JLabel imageLabel;
    private JLabel nameLabel;
    private RCButton button;
    private CurrentUserService currentUserService = Launcher.currentUserService;
    private CurrentUser currentUser;

    public MePanel()
    {
        currentUser = currentUserService.findAll().get(0);
        initComponents();
        initView();
        setListeners();
    }

    private void initComponents()
    {
        contentPanel = new JPanel();
        contentPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 20, true, false));

        imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUser.getUsername()).getScaledInstance(100,100, Image.SCALE_SMOOTH));
        imageLabel.setIcon(icon);

        nameLabel = new JLabel();
        nameLabel.setText(currentUser.getUsername());
        nameLabel.setFont(FontUtil.getDefaultFont(20));

        button = new RCButton("退出登录", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        button.setBackground(Colors.PROGRESS_BAR_START);
        button.setPreferredSize(new Dimension(200, 35));
        button.setFont(FontUtil.getDefaultFont(16));

    }

    private void initView()
    {
        this.setLayout(new GridBagLayout());

        JPanel avatarNamePanel = new JPanel();
        avatarNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        avatarNamePanel.add(imageLabel, BorderLayout.WEST);
        avatarNamePanel.add(nameLabel, BorderLayout.CENTER);

        contentPanel.add(avatarNamePanel);
        contentPanel.add(button);

        add(contentPanel, new GBC(0,0).setWeight(1,1).setAnchor(GBC.CENTER).setInsets(0,0,250,0));
    }


    private void setListeners()
    {
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int ret = JOptionPane.showConfirmDialog(MainFrame.getContext(), "确认退出登录？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION)
                {
                    JOptionPane.showMessageDialog(MainFrame.getContext(), "退出登录", "退出登录", JOptionPane.INFORMATION_MESSAGE);
                }

                super.mouseClicked(e);
            }
        });
    }

}

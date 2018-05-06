package xysoft.im.panels;

import xysoft.im.app.Launcher;
import xysoft.im.cache.UserCache;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.message.MainOperationPopupMenu;
import xysoft.im.db.service.CurrentUserService;
import xysoft.im.frames.MainFrame;
import xysoft.im.frames.SystemConfigDialog;
import xysoft.im.listener.AbstractMouseListener;
import xysoft.im.utils.AvatarUtil;
import xysoft.im.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MyInfoPanel extends ParentAvailablePanel
{
    /**
	 * 我的头像区
	 */
	private static final long serialVersionUID = 7114022185994391328L;

	private static MyInfoPanel context;

    private JLabel avatar;
    private JLabel username;
    private JLabel menuIcon;
    private CurrentUserService currentUserService = Launcher.currentUserService;

    MainOperationPopupMenu mainOperationPopupMenu;
    private String currentUsername;


    public MyInfoPanel(JPanel parent)
    {
        super(parent);
        context = this;

        initComponents();
        setListeners();
        initView();
    }


    private void initComponents()
    {

        //GImage.setBorder(new SubtleSquareBorder(true));
        currentUsername = currentUserService.findAll().get(0).getUsername();
        avatar = new JLabel();
        avatar.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(50,50,Image.SCALE_SMOOTH)));

        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setCursor(new Cursor(Cursor.HAND_CURSOR));


        username = new JLabel();
        username.setText(currentUsername);
        username.setFont(FontUtil.getDefaultFont(16));
        username.setForeground(Colors.FONT_WHITE);


        menuIcon = new JLabel();
        menuIcon.setIcon(new ImageIcon(getClass().getResource("/image/options.png")));
        menuIcon.setForeground(Colors.FONT_WHITE);
        menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        mainOperationPopupMenu = new MainOperationPopupMenu();
    }

    private void setListeners()
    {
        menuIcon.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    Component component = e.getComponent();
                    mainOperationPopupMenu.show(component, -112, 50);
                    super.mouseClicked(e);
                }

            }
        });

        avatar.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    SystemConfigDialog dialog = new SystemConfigDialog(MainFrame.getContext(), true);
                    dialog.setVisible(true);
                    super.mouseClicked(e);
                }
            }
        });
    }

    private void initView()
    {
        this.setBackground(Colors.DARK);
        this.setLayout(new GridBagLayout());

        add(avatar, new GBC(0, 0).setFill(GBC.NONE).setWeight(2, 1));
        add(username, new GBC(1, 0).setFill(GBC.BOTH).setWeight(7, 1));
        add(menuIcon, new GBC(2, 0).setFill(GBC.BOTH).setWeight(1, 1));
    }

    public void reloadAvatar()
    {
        currentUsername = UserCache.CurrentUserName;// currentUserService.findAll().get(0).getUsername();
        //Image image = AvatarUtil.createOrLoadUserAvatar(currentUsername);
        //avatar.setImage(image);
        avatar.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(50,50,Image.SCALE_SMOOTH)));

        avatar.revalidate();
        avatar.repaint();
    }

    public static MyInfoPanel getContext()
    {
        return context;
    }
}

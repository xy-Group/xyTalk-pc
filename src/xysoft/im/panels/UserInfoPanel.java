package xysoft.im.panels;


import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.RCButton;
import xysoft.im.components.VerticalFlowLayout;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.db.model.Room;
import xysoft.im.db.service.ContactsUserService;
import xysoft.im.db.service.RoomService;
import xysoft.im.frames.MainFrame;
import xysoft.im.utils.AvatarUtil;
import xysoft.im.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class UserInfoPanel extends ParentAvailablePanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5439149022809519880L;
	private JPanel contentPanel;
    private JLabel imageLabel;
    private JLabel nameLabel;
    private JLabel infoLabel;

    private RCButton button;
    private RCButton moreButton;

    private String username;
    private RoomService roomService = Launcher.roomService;
    private ContactsUserService contactsUserService = Launcher.contactsUserService;

    public UserInfoPanel(JPanel parent)
    {
        super(parent);
        initComponents();
        initView();
        setListeners();
    }

    private void initComponents()
    {
        contentPanel = new JPanel();
        contentPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 20, true, false));

        imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(AvatarUtil.createOrLoadUserAvatar("song").getScaledInstance(100,100, Image.SCALE_SMOOTH));
        imageLabel.setIcon(icon);

        nameLabel = new JLabel();
        nameLabel.setFont(FontUtil.getDefaultFont(20));
        
        infoLabel = new JLabel();
        infoLabel.setFont(FontUtil.getDefaultFont(14));


        button = new RCButton("发送消息", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        button.setBackground(Colors.PROGRESS_BAR_START);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(FontUtil.getDefaultFont(16));
        
        moreButton = new RCButton("查看更多", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        moreButton.setBackground(Colors.PROGRESS_BAR_START);
        moreButton.setPreferredSize(new Dimension(150, 40));
        moreButton.setFont(FontUtil.getDefaultFont(16));
        
        

    }

    private void initView()
    {
        this.setLayout(new GridBagLayout());

        JPanel avatarNamePanel = new JPanel();
        avatarNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        avatarNamePanel.add(imageLabel, BorderLayout.WEST);
        avatarNamePanel.add(nameLabel, BorderLayout.CENTER);
        avatarNamePanel.add(infoLabel, BorderLayout.CENTER);

        //add(avatarNamePanel, new GBC(0,0).setAnchor(GBC.CENTER).setWeight(1,1).setInsets(0,0,0,0));
        //add(button, new GBC(0,1).setAnchor(GBC.CENTER).setWeight(1,1).setInsets(0,0,0,0));
        contentPanel.add(avatarNamePanel);
        contentPanel.add(button);
        contentPanel.add(moreButton);

        //add(contentPanel, new GBC(0,0).setWeight(1,1).setAnchor(GBC.CENTER).setInsets(0,0,250,0));
        add(avatarNamePanel, new GBC(0,0).setWeight(1,1));
        add(contentPanel, new GBC(0,1).setWeight(1,1));
    }

    public void setUsername(String username)
    {
    	ContactsUser cu = contactsUserService.findByUsername(username);
    	
        this.username = username;
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(cu.getName());     
        sb.append("</html>");
        
        nameLabel.setText(sb.toString());
        StringBuilder sbInfo = new StringBuilder();
        sbInfo.append("<html>登录名:");
        sbInfo.append(username);     
        sbInfo.append("<p><br>手机:");
        sbInfo.append(cu.getPhone());     
        sbInfo.append("<p><br>邮箱:");
        sbInfo.append(cu.getMail());     
        sbInfo.append("<p><br>办公位置:");
        sbInfo.append(cu.getLocation());     
        sbInfo.append("</html>");
             
        infoLabel.setText(sbInfo.toString());

        ImageIcon icon = new ImageIcon(AvatarUtil.createOrLoadUserAvatar(username).getScaledInstance(100,100, Image.SCALE_SMOOTH));
        imageLabel.setIcon(icon);
    }

    private void setListeners()
    {
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                openOrCreateDirectChat();
                super.mouseClicked(e);
            }
        });
        
        moreButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
            	more();
                super.mouseClicked(e);
            }
        });
    }

    private void openOrCreateDirectChat()
    {
        ContactsUser user  = contactsUserService.find("username", username).get(0);
        String userId = user.getUserId()+"@"+Launcher.DOMAIN;
        Room room = roomService.findRelativeRoomIdByUserId(userId);

        // 房间已存在，直接打开，否则发送请求创建房间
        if (room != null)
        {
            ChatPanel.getContext().enterRoom(room.getRoomId());
        }else
        {
            createDirectChat(user.getName());
        }
    }

    /**
     * 创建直接聊天
     *
     * @param username
     */
    private void createDirectChat(String username){
        JOptionPane.showMessageDialog(MainFrame.getContext(), "发起聊天", "发起聊天", JOptionPane.INFORMATION_MESSAGE);
    }

    private void more() {
        JOptionPane.showMessageDialog(MainFrame.getContext(), "在门户查看更多资料", "在门户查看更多资料", JOptionPane.INFORMATION_MESSAGE);
	}

}

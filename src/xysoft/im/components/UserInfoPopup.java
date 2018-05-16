package xysoft.im.components;

import xysoft.im.app.Launcher;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.db.model.Room;
import xysoft.im.forms.ImageViewerFrame;
import xysoft.im.frames.MainFrame;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.ContactsPanel;
import xysoft.im.panels.RoomsPanel;
import xysoft.im.utils.AvatarUtil;
import xysoft.im.utils.FontUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static xysoft.im.app.Launcher.contactsUserService;
import static xysoft.im.app.Launcher.roomService;


public class UserInfoPopup extends JPopupMenu
{
    /**
	 * 用户信息弹出窗
	 */
	private static final long serialVersionUID = -5251311301957204281L;
	private JPanel contentPanel;
    private JLabel avatarLabel;
    private JLabel usernameLabel;
    private JButton sendButton;
    private String username;

    public UserInfoPopup(String username)
    {
        this.username = username;
        initComponents();
        initView();
        setListener();

        // 更新对方头像
        updateAvatar();
    }

    private void updateAvatar()
    {
        ContactsPanel.getContext().getUserAvatar(this.username, true);
    }

    private void initComponents()
    {
        setBackground(Colors.WINDOW_BACKGROUND_LIGHT);


        contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(200, 200));
        contentPanel.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);

        avatarLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon();
        imageIcon.setImage(AvatarUtil.createOrLoadUserAvatar(username).getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        avatarLabel.setIcon(imageIcon);

        usernameLabel = new JLabel();
        usernameLabel.setText(username);

        /*sendButton = new RCButton("发消息");
        sendButton.setPreferredSize(new Dimension(180, 40));
        sendButton.setForeground(Colors.FONT_BLACK);*/
        sendButton = new RCButton("发消息", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        sendButton.setBackground(Colors.PROGRESS_BAR_START);
        sendButton.setPreferredSize(new Dimension(180, 35));
        sendButton.setFont(FontUtil.getDefaultFont(15));
    }

    private void initView()
    {
        add(contentPanel);

        contentPanel.setLayout(new GridBagLayout());

        JPanel avatarUsernamePanel = new JPanel();
        avatarUsernamePanel.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);

        avatarUsernamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        avatarUsernamePanel.add(avatarLabel);
        avatarUsernamePanel.add(usernameLabel);
        avatarUsernamePanel.setPreferredSize(new Dimension(180, 80));


        JPanel sendButtonPanel = new JPanel();
        sendButtonPanel.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
        sendButtonPanel.add(sendButton);

        contentPanel.add(avatarUsernamePanel, new GBC(0,0).setWeight(1,1));
        contentPanel.add(sendButtonPanel, new GBC(0,1).setWeight(1,1));

    }

    private void setListener()
    {
        sendButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                openOrCreateDirectChat();
                super.mouseClicked(e);
            }
        });

        avatarLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setVisible(false);

                ImageIcon icon = new ImageIcon(AvatarUtil.createOrLoadUserAvatar(username));
                Image image = icon.getImage().getScaledInstance(200,200,Image.SCALE_SMOOTH);
                ImageViewerFrame imageViewerFrame = new ImageViewerFrame(image);
                imageViewerFrame.setVisible(true);
                imageViewerFrame.toFront();
                super.mouseClicked(e);
            }
        });
    }

    private void openOrCreateDirectChat()
    {
        ContactsUser user  = contactsUserService.find("username", username).get(0);
        String userId = user.getUserId();
        Room room = roomService.findRelativeRoomIdByUserId(userId);

        // 房间已存在，直接打开，否则发送请求创建房间
        if (room != null)
        {
            ChatPanel.getContext().enterRoom(room.getRoomId());
        }else
        {
        	createNewRoom();       	
        }

        this.setVisible(false);

    }
    
    //新建一个联系人
	private void createNewRoom() {
		Room room = new Room();
		room.setLastMessage("新建联系人");
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(1);
		room.setName(Launcher.contactsUserService.findByUsername(username).getName());
		room.setRoomId(username + "@"+Launcher.DOMAIN);
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("d"); //单聊
		room.setUnreadCount(1);
		Launcher.roomService.insertOrUpdate(room);
		
		RoomsPanel.getContext().notifyDataSetChanged(false);	
		ChatPanel.getContext().enterRoom(room.getRoomId());
	}

}

package xysoft.im.frames;

import xysoft.im.adapter.ContactsItemsAdapter;
import xysoft.im.app.Launcher;
import xysoft.im.cache.UserCache;
import xysoft.im.components.*;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.db.model.Room;
import xysoft.im.db.service.ContactsUserService;
import xysoft.im.db.service.RoomService;
import xysoft.im.entity.ContactsItem;
import xysoft.im.entity.SelectUserData;
import xysoft.im.panels.SelectUserPanel;
import xysoft.im.service.MucChatService;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.FontUtil;
import xysoft.im.utils.JID;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static xysoft.im.app.Launcher.roomService;

public class CreateGroupDialog extends JDialog {
	/**
	 * 创建群组UI
	 */
	private static final long serialVersionUID = 7698690365922458008L;
	private static CreateGroupDialog context;
	private JPanel editorPanel;
	private RCTextField searchTextField;
	private RCTextField groupNameTextField;
	private JCheckBox privateCheckBox;
	private JLabel info;

	private SelectUserPanel selectUserPanel;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;
	private List<SelectUserData> userList = new ArrayList<>();
	private List<SelectUserData> userListClone = new ArrayList<>();;

	private ContactsUserService contactsUserService = Launcher.contactsUserService;
	private RoomService roomService = Launcher.roomService;

	public static final int DIALOG_WIDTH = 630;
	public static final int DIALOG_HEIGHT = 530;

	public CreateGroupDialog(Frame owner, boolean modal) {
		super(owner, modal);
		context = this;

		initComponents();
		initAllData();
		initView();
		setListeners();	
		preData();  

	}

	
	// 展示全部已存在数据库中的用户
	private void initAllData() {
		userList.clear();
		List<Room> singleUser = roomService.findByType("s");
		for (Room room : singleUser) {
			userList.add(new SelectUserData(JID.usernameByJid(room.getRoomId()) + "-" + room.getName(), false));
		}

		selectUserPanel = new SelectUserPanel(DIALOG_WIDTH, DIALOG_HEIGHT - 100, userList);
	}


	public void preData() {
		CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
			public String get() {
				try {
					List<ContactsUser> contactsUsers = contactsUserService.findAll();
					
					for (ContactsUser contact : contactsUsers) {
						if (!contact.getUsername().equals(UserCache.CurrentUserName)){//过滤自己，无需邀请
							userListClone.add(new SelectUserData(contact.getUsername() + "-" + contact.getName(), false));							
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "initAllDataOK";
			}
		}, Launcher.executor);

		resultCompletableFuture.thenAcceptAsync(new Consumer<String>() {  
		    @Override  
		    public void accept(String t) {  
		    	DebugUtil.debug(t);  
		    	DebugUtil.debug("群组选人器回调线程："+Thread.currentThread().getName());  

		    }  
		}, Launcher.executor);
	}

	private void initComponents() {
		int posX = MainFrame.getContext().getX();
		int posY = MainFrame.getContext().getY();

		posX = posX + (MainFrame.getContext().currentWindowWidth - DIALOG_WIDTH) / 2;
		posY = posY + (MainFrame.getContext().currentWindowHeight - DIALOG_HEIGHT) / 2;
		setBounds(posX, posY, DIALOG_WIDTH, DIALOG_HEIGHT);
		setUndecorated(true);

		getRootPane().setBorder(new LineBorder(Colors.DIALOG_BORDER));

		/*
		 * if (OSUtil.getOsType() != OSUtil.Mac_OS) { // 边框阴影，但是会导致字体失真
		 * AWTUtilities.setWindowOpaque(this, false);
		 * //getRootPane().setOpaque(false);
		 * getRootPane().setBorder(ShadowBorder.newInstance()); }
		 */

		// 输入面板
		editorPanel = new JPanel();
		groupNameTextField = new RCTextField();
		groupNameTextField.setPlaceholder("群名称");
		groupNameTextField.setPreferredSize(new Dimension(DIALOG_WIDTH / 2, 35));
		groupNameTextField.setFont(FontUtil.getDefaultFont(14));
		groupNameTextField.setForeground(Colors.FONT_BLACK);
		groupNameTextField.setMargin(new Insets(0, 15, 0, 0));

		privateCheckBox = new JCheckBox("私有");
		privateCheckBox.setFont(FontUtil.getDefaultFont(14));
		privateCheckBox.setToolTipText("私有密码保护群组");
		privateCheckBox.setSelected(true);

		info = new JLabel();
		info.setText("勾选添加↓");
		// info.setPreferredSize(new Dimension(DIALOG_WIDTH / 4, 35));

		searchTextField = new RCTextField();
		searchTextField.setPlaceholder("搜索人员");
		searchTextField.setPreferredSize(new Dimension(DIALOG_WIDTH / 4, 35));
		searchTextField.setFont(FontUtil.getDefaultFont(14));
		searchTextField.setForeground(Colors.FONT_BLACK);
		searchTextField.setMargin(new Insets(0, 15, 0, 0));

		// 按钮组
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

		cancelButton = new RCButton("取消");
		cancelButton.setForeground(Colors.FONT_BLACK);

		okButton = new RCButton("创建", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
		okButton.setBackground(Colors.PROGRESS_BAR_START);
	}

	private void initView() {
		editorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		editorPanel.add(searchTextField);
		// editorPanel.add(privateCheckBox);
		editorPanel.add(info);
		editorPanel.add(groupNameTextField);

		buttonPanel.add(cancelButton, new GBC(0, 0).setWeight(1, 1).setInsets(15, 0, 0, 0));
		buttonPanel.add(okButton, new GBC(1, 0).setWeight(1, 1));

		add(editorPanel, BorderLayout.NORTH);
		add(selectUserPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void searchUsers(String key) {
		 if (key == null || key.isEmpty())
	        {
	           
	            selectUserPanel.notifyDataSetChanged(null);
	            return;
	        }

	        key = key.toLowerCase();
	        List<SelectUserData> users = new ArrayList<>();

	        for (SelectUserData item : userListClone)
	        {
	            if (item.getUserName().toLowerCase().indexOf(key) > -1 
	            		&& (!selectUserPanel.getSelectedUser().contains(item)))
	            {
	                users.add(item);
	            }
	        }

	        selectUserPanel.notifyDataSetChanged(users);
	}

	private void setListeners() {
		searchTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (searchTextField.getText().length()>1 || searchTextField.getText().length()==0){
					searchUsers(searchTextField.getText());
				}
				
				super.keyTyped(e);
			}

		});

		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);

				super.mouseClicked(e);
			}
		});

		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (okButton.isEnabled()) {
					okButton.setEnabled(false);

					String roomName = groupNameTextField.getText();
					if (roomName == null || roomName.isEmpty()) {
						JOptionPane.showMessageDialog(null, "请输入群聊名称", "请输入群聊名称", JOptionPane.WARNING_MESSAGE);
						groupNameTextField.requestFocus();
						okButton.setEnabled(true);
						return;
					}

					checkRoomExists(roomName);
					try {
						List<String> selectedUsers = new ArrayList<String>();
						
						for (SelectUserData item : selectUserPanel.getSelectedUser()){
							selectedUsers.add(item.getUserName().split("-")[0]+ "@" + Launcher.DOMAIN);
						}
//						selectedUsers.add("test3@vm_0_4_centos");
//						selectedUsers.add("xuanji@vm_0_4_centos");

						MultiUserChat muc = MucChatService.createChatRoom(groupNameTextField.getText(), selectedUsers,
								UserCache.CurrentUserName + "-" + UserCache.CurrentUserRealName);
						// 设置创建者为owner
						List<Jid> owners = new ArrayList<Jid>();
						owners.add(JidCreate.from(UserCache.CurrentBareJid));
						muc.grantOwnership(owners);

						muc.join(Resourcepart.from(UserCache.CurrentUserName + "-" + UserCache.CurrentUserRealName));
						
						setVisible(false);

					} catch (Exception e1) {

						e1.printStackTrace();
					}
				}

				super.mouseClicked(e);
			}
		});
	}

	private void checkRoomExists(String name) {
		if (roomService.findByName(name) != null) {
			showRoomExistMessage(name);
			okButton.setEnabled(true);
		} else {
			List<SelectUserData> list = selectUserPanel.getSelectedUser();
			String[] usernames = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {
				usernames[i] = list.get(i).getUserName();
			}

			createChannelOrGroup(name, privateCheckBox.isSelected(), usernames);
		}
	}

	/**
	 * 创建Channel或Group
	 *
	 * @param name
	 * @param privateGroup
	 * @param usernames
	 */
	private void createChannelOrGroup(String name, boolean privateGroup, String[] usernames) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < usernames.length; i++) {
			sb.append("\"" + usernames[i] + "\"");
			if (i < usernames.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");

		JOptionPane.showMessageDialog(MainFrame.getContext(), "创建群组", "创建群组", JOptionPane.INFORMATION_MESSAGE);
	}

	public static CreateGroupDialog getContext() {
		return context;
	}

	public void showRoomExistMessage(String roomName) {
		JOptionPane.showMessageDialog(null, "群组\"" + roomName + "\"已存在", "群组已存在", JOptionPane.WARNING_MESSAGE);
		groupNameTextField.setText("");
		groupNameTextField.requestFocus();
	}

}

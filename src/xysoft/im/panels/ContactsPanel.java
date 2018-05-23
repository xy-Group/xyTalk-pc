package xysoft.im.panels;

import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.impl.JidCreate;

import xysoft.im.adapter.ContactsItemsAdapter;
import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.RCListView;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.db.service.ContactsUserService;
import xysoft.im.db.service.CurrentUserService;
import xysoft.im.entity.ContactsItem;
import xysoft.im.utils.AvatarUtil;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.GraphicUtils;
import xysoft.im.utils.ImageUtil;

public class ContactsPanel extends ParentAvailablePanel {
	/**
	 * 右侧浮动联系人列表
	 */
	private static final long serialVersionUID = -4052544555492218547L;

	private static ContactsPanel context;
	private RCListView contactsListView;
	private List<ContactsItem> contactsItemList = new ArrayList<>();
	private ContactsUserService contactsUserService = Launcher.contactsUserService;
	private CurrentUserService currentUserService = Launcher.currentUserService;
	private String currentUsername;

	public ContactsPanel(JPanel parent) {
		super(parent);
		context = this;

		initComponents();
		initView();
		initData();
		contactsListView.setAdapter(new ContactsItemsAdapter(contactsItemList));

		// TODO: 从服务器获取通讯录后，调用下面方法更新UI
		notifyDataSetChanged();
	}

	private void initComponents() {
		contactsListView = new RCListView();
	}

	private void initView() {
		setLayout(new GridBagLayout());
		contactsListView.setContentPanelBackground(Colors.DARK);
		add(contactsListView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
	}

	private void initData() {
		contactsItemList.clear();

		List<ContactsUser> contactsUsers = contactsUserService.findAll();
		for (ContactsUser contactsUser : contactsUsers) {
			ContactsItem item = new ContactsItem(contactsUser.getUserId(), contactsUser.getUsername(), "s");

			contactsItemList.add(item);
		}

	}

	public void notifyDataSetChanged() {
		initData();
		((ContactsItemsAdapter) contactsListView.getAdapter()).processData();
		contactsListView.notifyDataSetChanged(false);

		// 通讯录更新后，获取头像
		getContactsUserAvatar();
	}

	public static ContactsPanel getContext() {
		return context;
	}

	/**
	 * 获取通讯录中用户的头像
	 */
	private void getContactsUserAvatar() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (ContactsItem user : contactsItemList) {
					if (!AvatarUtil.customAvatarExist(user.getName())) {
						final String username = user.getName();
						// logger.debug("获取头像:" + username);
						getUserAvatar(username, true);
					}
				}

				// 自己的头像每次启动都去获取
				currentUsername = currentUserService.findAll().get(0).getUsername();
				getUserAvatar(currentUsername, true);
			}
		}).start();

	}

	/**
	 * 更新指定用户头像
	 * 
	 * @param username
	 *            用户名
	 * @param hotRefresh
	 *            是否热更新，hotRefresh = true， 将刷新该用户的头像缓存
	 */
	public void getUserAvatar(String username, boolean hotRefresh) {

		try {
			BufferedImage image = null;

			if (hotRefresh) {// TODO: 服务器获取头像，这里从资源文件夹中获取
				VCard vcard;
				try {

					vcard = VCardManager.getInstanceFor(Launcher.connection)
							.loadVCard(JidCreate.entityBareFrom(username + "@" + Launcher.DOMAIN));
					// getAvatarIcon的第二个参数如果为true,则对头像缩放
					ImageIcon imageIcon = AvatarUtil.getAvatarIcon(vcard, false);
					if (imageIcon ==null){
						image = AvatarUtil.getCachedImageBufferedAvatar(username);
					}else{
						DebugUtil.debug("从服务器获取头像:" + username);
						image = GraphicUtils.convert(imageIcon.getImage());						
					}

				} catch (NoResponseException | XMPPErrorException | NotConnectedException | InterruptedException e) {
					image = AvatarUtil.getCachedImageBufferedAvatar(username);
				}
	
			} else {// 从缓存中读取
				image = AvatarUtil.getCachedImageBufferedAvatar(username);
			}

			processAvatarData(image, username);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (hotRefresh) {
			AvatarUtil.refreshUserAvatarCache(username);

			if (username.equals(currentUsername)) {
				MyInfoPanel.getContext().reloadAvatar();
			}
		}
	}

	/**
	 * 处理头像数据
	 * 
	 * @param image
	 * @param username
	 */
	private void processAvatarData(BufferedImage image, String username) {
		if (image != null) {
			AvatarUtil.saveAvatar(image, username);
		} else {
			AvatarUtil.deleteCustomAvatar(username);
		}
	}

}

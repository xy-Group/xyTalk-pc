package xysoft.im.panels;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import xysoft.im.app.Launcher;
import xysoft.im.cache.UserCache;
import xysoft.im.components.Colors;
import xysoft.im.components.RCButton;
import xysoft.im.components.RCTextField;
import xysoft.im.components.VerticalFlowLayout;
import xysoft.im.frames.MainFrame;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.FontUtil;
import xysoft.im.utils.IconUtil;

public class ChangeVcardPanel extends JPanel {

	/**
	 * 修改用户心情签名
	 */
	private static final long serialVersionUID = 1077075679985677197L;
	private static ChangeVcardPanel context;
	private RCTextField signField; // 心情签名
	private RCButton okButton;
	private JPanel contentPanel;
	private JLabel statusLabel;

	public ChangeVcardPanel() {
		context = this;

		initComponents();
		initView();
		setListener();
		signField.requestFocus();
	}

	private void initComponents() {
		signField = new RCTextField();
		signField.setPlaceholder("心情签名");
		signField.setPreferredSize(new Dimension(300, 35));
		signField.setFont(FontUtil.getDefaultFont(14));
		signField.setForeground(Colors.FONT_BLACK);
		signField.setMargin(new Insets(0, 15, 0, 0));

		okButton = new RCButton("保存", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
		okButton.setPreferredSize(new Dimension(100, 35));

		statusLabel = new JLabel();
		statusLabel.setForeground(Colors.FONT_GRAY_DARKER);
		// statusLabel.setVisible(false);

		contentPanel = new JPanel();
	}

	private void initView() {
		contentPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 10, true, false));
		contentPanel.add(signField);
		contentPanel.add(okButton);
		contentPanel.add(statusLabel);

		add(contentPanel);
	}

	private void setListener() {

		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				doSave();

				super.mouseClicked(e);
			}
		});

		KeyListener keyListener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doSave();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		};
		signField.addKeyListener(keyListener);

	}

	private void doSave() {

		String sign = new String(signField.getText());

		if (sign.isEmpty()) {
			showErrorMessage("请输入心情签名");
			signField.requestFocus();
			return;
		}
		saveVCard(sign);
		JOptionPane.showMessageDialog(MainFrame.getContext(), "心情签名修改完毕", "修改完毕", JOptionPane.INFORMATION_MESSAGE);

	}

	public void saveVCard(String sign) {
		DebugUtil.debug("保存Vcard信息-心情签名");

		VCard vcard = null;
		try {
			try {
				vcard = VCardManager.getInstanceFor(Launcher.connection)
						.loadVCard(JidCreate.entityBareFrom(UserCache.CurrentBareJid));
			} catch (XmppStringprepException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoResponseException | XMPPErrorException | NotConnectedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// vcard节点
		// vcard.setFirstName(personalPanel.getFirstName());
		// vcard.setLastName(personalPanel.getLastName());
		// vcard.setEmailHome(personalPanel.getEmailAddress());
		// vcard.setOrganization(personalPanel.getCompany());
		// vcard.setAddressFieldWork("STREET",
		// personalPanel.getStreetAddress());
		// vcard.setAddressFieldWork("REGION", personalPanel.getState());
		// vcard.setAddressFieldWork("CTRY", personalPanel.getCountry());
		// vcard.setField("TITLE", personalPanel.getJobTitle());
		// vcard.setPhoneWork("VOICE", personalPanel.getPhone());
		// vcard.setPhoneWork("CELL", personalPanel.getMobile());

		try {
			vcard.setField("TITLE", sign);
			VCardManager.getInstanceFor(Launcher.connection).saveVCard(vcard);

		} catch (NoResponseException | NotConnectedException | InterruptedException | XMPPException e) {
			JOptionPane.showMessageDialog(null, "服务器不支持VCards。 无法保存你的VCard。", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void showSuccessMessage() {
		statusLabel.setText("修改成功");
		statusLabel.setIcon(IconUtil.getIcon(this, "/image/check.png"));
		statusLabel.setVisible(true);
	}

	public void showErrorMessage(String message) {
		statusLabel.setText(message);
		statusLabel.setIcon(new ImageIcon(
				IconUtil.getIcon(this, "/image/fail.png").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		statusLabel.setVisible(true);
	}

	public static ChangeVcardPanel getContext() {
		return context;
	}
}
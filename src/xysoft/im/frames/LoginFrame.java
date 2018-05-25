package xysoft.im.frames;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;

import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.RCButton;
import xysoft.im.components.RCPasswordField;
import xysoft.im.components.RCTextField;
import xysoft.im.components.VerticalFlowLayout;
import xysoft.im.db.model.CurrentUser;
import xysoft.im.db.service.CurrentUserService;
import xysoft.im.listener.AbstractMouseListener;
import xysoft.im.service.login.XmppLogin;
import xysoft.im.utils.DbUtils;
import xysoft.im.utils.Encryptor;
import xysoft.im.utils.FilesIO;
import xysoft.im.utils.FontUtil;
import xysoft.im.utils.IconUtil;
import xysoft.im.utils.OSUtil;
import xysoft.im.utils.PasswordUtil;
import xysoft.im.utils.SwingAnimation;


public class LoginFrame extends JFrame {
	/**
	 * 登陆UI
	 */
	private static final long serialVersionUID = -800160041683434428L;
	private static final int windowWidth = 300;
	private static final int windowHeight = 350;

	private JPanel controlPanel;
	private JLabel closeLabel;
	private JPanel editPanel;
	private RCTextField usernameField;
	private RCPasswordField passwordField;
	private RCButton loginButton;
	private JLabel statusLabel;
	private JLabel titleLabel;
	private JLabel downloadLabel;
	private JCheckBox remberPsw;
	
	private static Point origin = new Point();

	private SqlSession sqlSession;
	private CurrentUserService currentUserService;
	//private String username;

	public LoginFrame() {
		initService();
		initComponents();
		initView();
		centerScreen();
		setListeners();

	}

	public LoginFrame(String username) {
		this();
		//this.username = username;
		if (username != null && !username.isEmpty()) {
			usernameField.setText(username);
		}
	}

	private void initService() {
		sqlSession = DbUtils.getSqlSession();
		currentUserService = new CurrentUserService(sqlSession);
	}

	private void initComponents() {
		Dimension windowSize = new Dimension(windowWidth, windowHeight);
		setMinimumSize(windowSize);
		setMaximumSize(windowSize);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		// controlPanel.setBounds(0,5, windowWidth, 30);

		closeLabel = new JLabel();
		closeLabel.setIcon(IconUtil.getIcon(this, "/image/close.png"));
		closeLabel.setHorizontalAlignment(JLabel.CENTER);
		// closeLabel.setPreferredSize(new Dimension(30,30));
		closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		titleLabel = new JLabel();
		titleLabel.setText("登录 XyTalk");
		titleLabel.setFont(FontUtil.getDefaultFont(16));

		downloadLabel = new JLabel();
		downloadLabel.setText("下载客户端软件");
		downloadLabel.setFont(FontUtil.getDefaultFont(14));
		
		remberPsw = new JCheckBox("记住密码", true);
		remberPsw.setFont(FontUtil.getDefaultFont(14));

		editPanel = new JPanel();
		editPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));

		Dimension textFieldDimension = new Dimension(200, 35);
		usernameField = new RCTextField();
		usernameField.setPlaceholder("用户名");
		usernameField.setPreferredSize(textFieldDimension);
		usernameField.setFont(FontUtil.getDefaultFont(14));
		usernameField.setForeground(Colors.FONT_BLACK);
		usernameField.setMargin(new Insets(0, 15, 0, 0));

		passwordField = new RCPasswordField();
		passwordField.setPreferredSize(textFieldDimension);
		passwordField.setPlaceholder("密码");
		// passwordField.setBorder(new RCBorder(RCBorder.BOTTOM,
		// Colors.LIGHT_GRAY));
		passwordField.setFont(FontUtil.getDefaultFont(14));
		passwordField.setForeground(Colors.FONT_BLACK);
		passwordField.setMargin(new Insets(0, 15, 0, 0));

		loginButton = new RCButton("登 录", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
		loginButton.setFont(FontUtil.getDefaultFont(14));
		loginButton.setPreferredSize(new Dimension(300, 40));

		statusLabel = new JLabel();
		statusLabel.setForeground(Colors.RED);
		statusLabel.setText("密码不正确");
		statusLabel.setVisible(false);
		
		usernameField.setText(readName());
		passwordField.setText(readPassword());
	}

	private String readPassword() {
		String path = Launcher.appFilesBasePath + System.getProperty("file.separator") + "setting"
				+  System.getProperty("file.separator") + "password";
		String password = FilesIO.fileRead(path);
		if (password == null || password.isEmpty())
			return "";
		else
			return Encryptor.decrypt(password);
	}

	private String readName() {
		String path = Launcher.appFilesBasePath + System.getProperty("file.separator") + "setting"
				+  System.getProperty("file.separator") + "username";
		String username = FilesIO.fileRead(path);
		if (username == null || username.isEmpty())
			return "";
		else
			return username;
	}

	private void initView() {
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new LineBorder(Colors.LIGHT_GRAY));
		contentPanel.setLayout(new GridBagLayout());

		controlPanel.add(closeLabel);

		if (OSUtil.getOsType() != OSUtil.Mac_OS) {
			setUndecorated(true);
			contentPanel.add(controlPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1).setInsets(5, 0, 0, 0));
		}

		JPanel titlePanel = new JPanel();
		titlePanel.add(titleLabel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		//buttonPanel.add(loginButton, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(2, 1).setInsets(10, 0, 0, 0));		
		buttonPanel.add(remberPsw, new GBC(0, 1).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(20, 10, 0, 0));
		buttonPanel.add(downloadLabel, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(20, 10, 0, 0));
		editPanel.add(usernameField);
		editPanel.add(passwordField);
		editPanel.add(loginButton);
		editPanel.add(buttonPanel);
		editPanel.add(statusLabel);
		add(contentPanel);
		contentPanel.add(titlePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(2, 1).setInsets(10, 10, 0, 10));
		contentPanel.add(editPanel, new GBC(0, 2).setFill(GBC.BOTH).setWeight(2, 10).setInsets(10, 10, 0, 10));
	
        Toolkit tk = Toolkit.getDefaultToolkit();
        Launcher.currentWindowWidth = tk.getScreenSize().width;
        Launcher.currentWindowHeight = tk.getScreenSize().height;
	}

	/**
	 * 使窗口在屏幕中央显示
	 */
	private void centerScreen() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		this.setLocation((tk.getScreenSize().width - windowWidth) / 2, (tk.getScreenSize().height - windowHeight) / 2);
	}

	private void setListeners() {
		// 加入鼠标动画
		usernameField.addMouseListener(SwingAnimation.backAnimationMouse(usernameField));
		passwordField.addMouseListener(SwingAnimation.backAnimationMouse(passwordField));
		downloadLabel.addMouseListener(SwingAnimation.foreAnimationMouse(downloadLabel));

		closeLabel.addMouseListener(new AbstractMouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(1);
				super.mouseClicked(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				closeLabel.setBackground(Colors.LIGHT_GRAY);
				super.mouseEntered(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeLabel.setBackground(Colors.WINDOW_BACKGROUND);
				super.mouseExited(e);
			}
		});

		if (OSUtil.getOsType() != OSUtil.Mac_OS) {
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					// 当鼠标按下的时候获得窗口当前的位置
					origin.x = e.getX();
					origin.y = e.getY();
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					// 当鼠标拖动时获取窗口当前位置
					Point p = LoginFrame.this.getLocation();
					// 设置窗口的位置
					LoginFrame.this.setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
				}
			});
		}

		loginButton.addMouseListener(new AbstractMouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (loginButton.isEnabled()) {
					doLogin();
				}

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
					doLogin();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		};
		usernameField.addKeyListener(keyListener);
		passwordField.addKeyListener(keyListener);
	}

	private void doLogin() {
		// 登录逻辑
		
		final String name = usernameField.getText().trim();
		final String pwd = new String(passwordField.getPassword());

		if (name == null || name.isEmpty()) {
			showMessage("请输入用户名");
		} else if (pwd == null || pwd.isEmpty()) {
			showMessage("请输入密码");
		} else {
			statusLabel.setVisible(false);
			
			SwingWorker<?, ?> aWorker = new SwingWorker<Object, Object>() 
	    	{
			@Override
			protected Object doInBackground() throws Exception {
				XmppLogin login = new XmppLogin();
				login.setUsername(name);
				login.setPassword(pwd);
				String flag = login.login();
				SaveUsernameTofile(usernameField.getText());
				if (remberPsw.isSelected()){
					SavePasswordTofile(passwordField.getText());
				}
				
				if (flag.equals("ok")) {					
					MainFrame frame = new MainFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
				else{
					statusLabel.setVisible(true);
					statusLabel.setText(flag);
				}
				return true;
			}
			@Override
	        protected void done() {
	            boolean done = false;
	            try {
	            	done = (boolean) get();
	                // Update UI
	                if (done)
	                	hideMe();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	           
	        }
	    	};
	    	aWorker.execute();
	    	statusLabel.setVisible(true);
	    	statusLabel.setText("加载数据......");
			 
		}

	}

	private void hideMe() {
		this.dispose();
	}

	@SuppressWarnings("unused")
	private void processLoginResult(JSONObject ret) {
		if (ret.get("status").equals("success")) {

			JSONObject data = ret.getJSONObject("data");
			String authToken = data.getString("authToken");
			String userId = data.getString("userId");

			CurrentUser currentUser = new CurrentUser();
			currentUser.setUserId(userId);
			currentUser.setAuthToken(authToken);
			currentUser.setRawPassword(new String(passwordField.getPassword()));
			currentUser.setPassword(PasswordUtil.encryptPassword(currentUser.getRawPassword()));
			currentUser.setUsername(usernameField.getText());
			currentUserService.insertOrUpdate(currentUser);

			hideMe();
			

			MainFrame frame = new MainFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} else {
			showMessage("用户不存在或密码错误");
			loginButton.setEnabled(true);
			usernameField.setEditable(true);
			passwordField.setEditable(true);
		}

	}

	private void showMessage(String message) {
		if (!statusLabel.isVisible()) {
			statusLabel.setVisible(true);
		}

		statusLabel.setText(message);
	}
	
	private static void SaveUsernameTofile(String username)
    {
        try
        {
            String path = Launcher.appFilesBasePath + System.getProperty("file.separator") + "setting";
            File dir = new File(path);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            File file = new File(path + System.getProperty("file.separator") + "username");
            if (!file.exists())
            {
            	file.createNewFile();
            }

           FilesIO.fileWrite(file.getPath(), username,false);
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	
	private static void SavePasswordTofile(String password)
    {
        try
        {
            String path = Launcher.appFilesBasePath + System.getProperty("file.separator") + "setting";
            File dir = new File(path);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            File file = new File(path + System.getProperty("file.separator") + "password");
            if (!file.exists())
            {
            	file.createNewFile();
            }

           try {
			FilesIO.fileWrite(file.getPath(),Encryptor.encrypt(password) ,false);
		} catch (Exception e) {
			
		}
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

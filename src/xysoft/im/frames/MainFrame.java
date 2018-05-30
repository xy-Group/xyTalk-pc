package xysoft.im.frames;


import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.panels.LeftPanel;
import xysoft.im.panels.RightPanel;
import xysoft.im.utils.ClipboardUtil;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.FontUtil;
import xysoft.im.utils.IconUtil;
import xysoft.im.utils.OSUtil;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;


@SuppressWarnings("restriction")
public class MainFrame extends JFrame
{
    /**
	 * 主窗体
	 */
	private static final long serialVersionUID = 6120827660367438712L;
	public static int DEFAULT_WIDTH = 900;
    public static int DEFAULT_HEIGHT = 650;

    public int currentWindowWidth = DEFAULT_WIDTH;
    public int currentWindowHeight = DEFAULT_HEIGHT;

    private LeftPanel leftPanel;
    private RightPanel rightPanel;

    private static MainFrame context;//单例模式
    private Image normalTrayIcon; // 正常时的任务栏图标
    private Image emptyTrayIcon; // 闪动时的任务栏图标
    private TrayIcon trayIcon;
    private boolean trayFlashing = false;
	private AudioStream messageSound; //消息到来时候的提示间

    public MainFrame()
    {
        context = this;
        initComponents();
        initView();
        initResource();
    }

    private void initResource()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                initTray();
            }
        }).start();

    }


    /**
     * 播放消息提示间
     */
	public void playMessageSound()
    {
        try
        {
            InputStream inputStream = getClass().getResourceAsStream("/wav/msg.wav");
            messageSound = new AudioStream(inputStream);
            AudioPlayer.player.start(messageSound);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 初始化系统托盘图标
     */
    private void initTray()
    {
        SystemTray systemTray = SystemTray.getSystemTray();//获取系统托盘
        try
        {
            if (OSUtil.getOsType() == OSUtil.Mac_OS)
            {
                normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher.png", 20, 20).getImage();
            }
            else
            {
                normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher.png", 20, 20).getImage();
            }

            emptyTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher_empty.png", 20, 20).getImage();

            trayIcon = new TrayIcon(normalTrayIcon, "XyTalk");
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    // 显示主窗口
                    setVisible(true);

                    // 任务栏图标停止闪动
                    if (trayFlashing)
                    {
                        trayFlashing = false;
                        trayIcon.setImage(normalTrayIcon);
                    }

                    super.mouseClicked(e);
                }
            });

            PopupMenu menu = new PopupMenu();

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    clearClipboardCache();
                    System.exit(1);
                }
            });

//            MenuItem showItem = new MenuItem("打开XyTalk");
//            showItem.addActionListener(new ActionListener()
//            {
//                @Override
//                public void actionPerformed(ActionEvent e)
//                {
//                    setVisible(true);
//                }
//            });
//            menu.add(showItem);
            menu.add(exitItem);

            trayIcon.setPopupMenu(menu);
            systemTray.add(trayIcon);

        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 清除剪切板缓存文件
     */
    private void clearClipboardCache()
    {
        ClipboardUtil.clearCache();
    }

    /**
     * 设置任务栏图标闪动
     */
    public void setTrayFlashing()
    {
        trayFlashing = true;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (trayFlashing)
                {
                    try
                    {
                        trayIcon.setImage(emptyTrayIcon);
                        Thread.sleep(800);

                        trayIcon.setImage(normalTrayIcon);
                        Thread.sleep(800);

                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public boolean isTrayFlashing()
    {
        return trayFlashing;
    }


    public static MainFrame getContext()
    {
        return context;
    }


    private void initComponents()
    {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // 任务栏图标
        if (OSUtil.getOsType() != OSUtil.Mac_OS)
        {
            setIconImage(IconUtil.getIcon(this, "/image/ic_launcher.png").getImage());
        }

        UIManager.put("Label.font", FontUtil.getDefaultFont());
        UIManager.put("Panel.font", FontUtil.getDefaultFont());
        UIManager.put("TextArea.font", FontUtil.getDefaultFont());
        UIManager.put("Panel.background", Colors.WINDOW_BACKGROUND);
        UIManager.put("CheckBox.background", Colors.WINDOW_BACKGROUND);

        leftPanel = new LeftPanel();
        leftPanel.setPreferredSize(new Dimension(260, currentWindowHeight));

        rightPanel = new RightPanel();
    }

    private void initView()
    {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));


        if (OSUtil.getOsType() != OSUtil.Mac_OS)
        {
            // 隐藏标题栏
            setUndecorated(true);

            String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            try
            {
            	UIManager.setLookAndFeel(windows);
                //UIManager.setLookAndFeel(new DarculaLaf());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        setListeners();


        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        centerScreen();
    }


    /**
     * 使窗口在屏幕中央显示
     */
    private void centerScreen()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation((tk.getScreenSize().width - currentWindowWidth) / 2,
                (tk.getScreenSize().height - currentWindowHeight) / 2);
        Launcher.currentWindowWidth = tk.getScreenSize().width;
        Launcher.currentWindowHeight = tk.getScreenSize().height;
        DebugUtil.debug("Launcher.currentWindowWidth:"+Launcher.currentWindowWidth);
        DebugUtil.debug("Launcher.currentWindowHeight:"+Launcher.currentWindowHeight);
    }

    private void setListeners()
    {
        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                currentWindowWidth = (int) e.getComponent().getBounds().getWidth();
                currentWindowHeight = (int) e.getComponent().getBounds().getHeight();

                Launcher.currentWindowWidth = currentWindowWidth;
                Launcher.currentWindowHeight = currentWindowHeight;
                DebugUtil.debug("Resize Launcher.currentWindowWidth:"+Launcher.currentWindowWidth);
                DebugUtil.debug("Resize Launcher.currentWindowHeight:"+Launcher.currentWindowHeight);
            }
        });
    }

    @Override
    public void dispose()
    {
        // 移除托盘图标
        SystemTray.getSystemTray().remove(trayIcon);
        super.dispose();
    }
}


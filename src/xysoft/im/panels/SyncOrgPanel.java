package xysoft.im.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.RCButton;
import xysoft.im.utils.IconUtil;

public class SyncOrgPanel  extends JPanel
{
	/**
	 * 组织架构同步
	 */
	private static final long serialVersionUID = -4776816333848152119L;
	private JLabel infoLabel;
    private RCButton clearButton;

    public SyncOrgPanel()
    {
        initComponents();
        initView();
        setListeners();
    }

    private void setListeners()
    {
        clearButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (clearButton.isEnabled())
                {
                    clearButton.setText("同步中...");
                    clearButton.setIcon(IconUtil.getIcon(this, "/image/loading_small.gif"));
                    clearButton.setEnabled(false);
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                deleteAllDBs();
                                clearButton.setText("同步完成！");
                                clearButton.setIcon(IconUtil.getIcon(this, "/image/check.png"));
                                infoLabel.setText("同步完成！");
                            }
                            catch (Exception e)
                            {
                                clearButton.setText("同步失败");
                            }

                            clearButton.setEnabled(true);
                        }

						
                    }).start();
                }

                super.mouseClicked(e);
            }
        });
    }
    private void deleteAllDBs() {
		// TODO Auto-generated method stub
    	Launcher.roomService.deleteAll();
    	Launcher.messageService.deleteAll();
    	Launcher.fileAttachmentService.deleteAll();
    	Launcher.imageAttachmentService.deleteAll();
    	RoomsPanel.getContext().notifyDataSetChanged(false);

        RightPanel.getContext().getTitlePanel().showAppTitle("联系人已同步为最新");
        RightPanel.getContext().showPanel(RightPanel.TIP);
	}
    
    private void initComponents()
    {
        infoLabel = new JLabel("当前联系人...");
        clearButton = new RCButton("同步联系人", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        clearButton.setPreferredSize(new Dimension(150, 35));
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        clearButton.setToolTipText("同步联系人与服务器数据保持一致");

        calculateDB();
    }

    private void calculateDB()
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                infoLabel.setText("当前联系人数量：" + Launcher.contactsUserService.count());
            }
        }).start();
    }
   

    private void initView()
    {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 150));
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(clearButton, BorderLayout.CENTER);

        this.setLayout(new GridBagLayout());
        add(panel, new GBC(0, 0).setAnchor(GBC.NORTH).setFill(GBC.HORIZONTAL).setInsets(-200, 0, 0, 0));
    }


}

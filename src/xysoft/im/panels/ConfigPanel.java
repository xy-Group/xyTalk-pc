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

public class ConfigPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6440556966568490047L;
	private JLabel infoLabel;
    private RCButton clearButton;

    public ConfigPanel()
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
                    clearButton.setText("清除中...");
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
                                clearButton.setText("数据库清理完成！");
                                clearButton.setIcon(IconUtil.getIcon(this, "/image/check.png"));
                                infoLabel.setText("数据库清理完成！");
                            }
                            catch (Exception e)
                            {
                                clearButton.setText("清除失败");
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

        RightPanel.getContext().getTitlePanel().showAppTitle("数据库已清除");
        RightPanel.getContext().showPanel(RightPanel.TIP);
	}
    
    private void initComponents()
    {
        infoLabel = new JLabel("当前数据库使用情况...");
        clearButton = new RCButton("清除数据库", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        clearButton.setPreferredSize(new Dimension(150, 35));
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        clearButton.setToolTipText("清除联系人、聊天消息记录、群组等与登陆者相关的所有数据。");

        calculateDB();
    }

    private void calculateDB()
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                infoLabel.setText("当前数据库聊天消息记录数量：" + Launcher.messageService.findAll().size());
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

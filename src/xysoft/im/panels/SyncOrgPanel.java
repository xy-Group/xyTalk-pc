package xysoft.im.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ToolTipManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xysoft.im.app.Launcher;
import xysoft.im.components.Colors;
import xysoft.im.components.GBC;
import xysoft.im.components.RCButton;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.entity.SearchResultItem;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.IconUtil;
import xysoft.im.utils.JsonUtil;

public class SyncOrgPanel  extends JPanel
{
	/**
	 * 组织架构同步
	 */
	private static final long serialVersionUID = -4776816333848152119L;
	private JLabel infoLabel;
    private RCButton clearButton;
    private JProgressBar progressBar;

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
                    clearButton.setText("同步中,关闭此窗不影响同步");
                    clearButton.setIcon(IconUtil.getIcon(this, "/image/loading_small.gif"));
                    clearButton.setEnabled(false);
                    
                    CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            			public String get() {
            				try {
            					try
                                {
                                	syncAllUser();
                                    clearButton.setText("同步完成！");
                                    clearButton.setIcon(IconUtil.getIcon(this, "/image/check.png"));
                                    infoLabel.setText("同步完成！");
                                }
                                catch (Exception e)
                                {
                                    clearButton.setText("同步失败");
                                }
            				} catch (Exception e) {
            					// TODO Auto-generated catch block
            					e.printStackTrace();
            				}
            				return "ok";
            			}
            		}, Launcher.executor);

            		resultCompletableFuture.thenAcceptAsync(new Consumer<String>() {  
            		    @Override  
            		    public void accept(String t) {  

            		    }  
            		}, Launcher.executor);  
            		
//                    new Thread(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            
//
//                            
//                        }
//
//						
//                    }).start();
                }

                super.mouseClicked(e);
            }
        });
    }
    private void syncAllUser() {
		// TODO Auto-generated method stub
    	Launcher.contactsUserService.deleteAll();

    	long startTime = System.currentTimeMillis();

		JSONArray jsons;
		try {
			jsons = JsonUtil.readJsonsFromUrl(Launcher.ORGUSERURL);
			int count = jsons.length();
			double step = count/100;
			int i = 0;

			Iterator<Object> it = jsons.iterator();
            List<ContactsUser> list=new ArrayList<ContactsUser>();
            while (it.hasNext()) {
                JSONObject ob = (JSONObject) it.next();
                ContactsUser model = null;
                if(ob.getString("n")!=null){
                    model=new ContactsUser();
                    model.setName(ob.getString("n"));
                }
                if(ob.getString("u")!=null){
                    model.setUsername(ob.getString("u"));
                    model.setUserId(ob.getString("u"));
                }
                if(ob.getString("p")!=null){
                    model.setPhone(ob.getString("p"));
                }
                if(ob.getString("s")!=null){
                    model.setSp(ob.getString("s"));
                }
                if(ob.getString("d")!=null){
                    model.setDept(ob.getString("d"));
                }
                if(ob.getString("e")!=null){
                    model.setMail(ob.getString("e"));
                }
                if(ob.getString("l")!=null){
                    model.setLocation(ob.getString("l"));
                }
                i++;
                if(model!=null){
                    list.add(model);
                    Launcher.contactsUserService.insert(model);
                    int prec = i/(int)step;
                    progressBar.setValue(prec);
                    infoLabel.setText(prec + "%");
                }
            }			 

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis() - startTime  ;
	    DebugUtil.debug("同步用户查询耗时（毫秒）："+ endTime );

        RightPanel.getContext().getTitlePanel().showAppTitle("联系人已同步为最新");
        RightPanel.getContext().showPanel(RightPanel.TIP);
	}
    
    private void initComponents()
    {
        infoLabel = new JLabel("当前联系人...");
        clearButton = new RCButton("同步联系人", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        clearButton.setPreferredSize(new Dimension(250, 35));
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        clearButton.setToolTipText("同步联系人与服务器数据保持一致");
        
        progressBar = new JProgressBar();

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
        panel.add(progressBar, BorderLayout.SOUTH);
        
        this.setLayout(new GridBagLayout());
        add(panel, new GBC(0, 0).setAnchor(GBC.NORTH).setFill(GBC.HORIZONTAL).setInsets(-200, 0, 0, 0));
    }


}

package xysoft.im.components;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xysoft.im.listener.AbstractMouseListener;
import xysoft.im.utils.FontUtil;

public class ImageTitlePanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8207575332420579783L;
	//private Image backgroundImage;
    private final JLabel titleLabel = new JLabel();
    private final JLabel iconLabel = new JLabel();
    private final GridBagLayout gridBagLayout = new GridBagLayout();


    /**
     * Creates a new ImageTitlePanel object.
     */
    public ImageTitlePanel() {
        //backgroundImage = IconUtil.getIcon(this, "/image/fail.png").getImage();

        init();
    }

    public void paintComponent(Graphics g) {
//        double scaleX = getWidth() / (double)backgroundImage.getWidth(null);
//        double scaleY = getHeight() / (double)backgroundImage.getHeight(null);
//        AffineTransform xform = AffineTransform.getScaleInstance(scaleX, scaleY);
//        ((Graphics2D)g).drawImage(backgroundImage, xform, this);
    }

    private void init() {
        setLayout(gridBagLayout);
        add(iconLabel, new GBC(0,0).setAnchor(GBC.WEST).setFill(GBC.BOTH).setInsets(5,8,5,0));
        add(titleLabel, new GBC(0,0).setAnchor(GBC.WEST).setFill(GBC.BOTH).setInsets(5,60,5,50));
        iconLabel.setBorder(new RCBorder(RCBorder.BOTTOM, Colors.DARKER));
		//titleLabel.setBorder(new RCBorder(RCBorder.BOTTOM, Colors.SCROLL_BAR_TRACK_LIGHT));
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        //Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        //this.setCursor(handCursor);
        
        this.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
            	
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            	cursorSet(handCursor);
            }

			

            @Override
            public void mouseExited(MouseEvent e)
            {
            	
            }
        });


    }
    
    public void cursorSet(Cursor defaultCursor) {
		this.setCursor(defaultCursor);
		//titleLabel.setForeground(Colors.FONT_WHITE);
	}

    public void setTitle(String title) {
        titleLabel.setText(title);
        titleLabel.setFont(FontUtil.getDefaultFont(14));
        titleLabel.setForeground(Colors.FONT_WHITE);
        titleLabel.setPreferredSize(new Dimension(100,30));
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }


    public void setTitleFont(Font font) {
        titleLabel.setFont(font);
    }


    public void setIcon(ImageIcon icon) {

        iconLabel.setIcon(icon);

    }
}

